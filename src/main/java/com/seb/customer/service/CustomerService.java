package com.seb.customer.service;

import com.seb.customer.entity.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Customer create(Customer customer);

    Optional<Customer> get(UUID id);

    List<Customer> getAll();
}
