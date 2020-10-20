package com.codeemma.employee.http;

import com.codeemma.employee.model.Employee;
import com.codeemma.employee.exception.EmployeeNotFound;
import com.codeemma.employee.http.entity.ExternalEmployeeCreate;
import com.codeemma.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("authenticated()")
    @PostMapping
    public Employee create(@Valid @RequestBody ExternalEmployeeCreate employeeCreate) {
        log.debug("received create(), employeeCreate = {}", employeeCreate);

        return employeeService.create(employeeCreate.toInternal());
    }

    @GetMapping
    public List<Employee> findAll() {
        log.debug("received find()");

        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public Employee get(@PathVariable(value = "id") UUID id) {
        log.debug("received get(), id = {}", id);

        return employeeService.get(id).orElseThrow(() -> new EmployeeNotFound("player not found"));
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable(value = "id") UUID id,
                           @Valid @RequestBody ExternalEmployeeCreate body) {
        log.debug("received update(), id = {}, body = {}", id, body);

        return employeeService.update(id, body.toInternal());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") UUID id) {
        log.debug("received delete(), id = {}", id);

        employeeService.delete(id);
    }

//
//    @GetMapping("/rx")
//    public Flux<Employee> findAllRx() {
//        log.debug("received find()");
//
//        return employeeService.getAllRx();
//    }
}
