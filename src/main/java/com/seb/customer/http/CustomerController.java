package com.seb.customer.http;

import com.seb.customer.entity.Customer;
import com.seb.customer.exception.CustomerNotFound;
import com.seb.customer.http.entity.ExternalCustomerCreate;
import com.seb.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("authenticated()")
    @PostMapping
    public Customer create(@Valid @RequestBody ExternalCustomerCreate customer) {
        log.debug("received create(), customer = {}", customer);

        return customerService.create(customer.toInternal());
    }

    @GetMapping
    public List<Customer> findAll() {
        log.debug("received find()");

        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable(value = "id") UUID id) {
        log.debug("received get(), id = {}", id);

        return customerService.get(id).orElseThrow(() -> new CustomerNotFound("player not found"));
    }
}
