package com.seb.customer.service;

import com.seb.customer.entity.Customer;
import com.seb.customer.store.CustomerStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerStore customerStore;

    public CustomerServiceImpl(CustomerStore customerStore) {
        this.customerStore = customerStore;
    }

    @Override
    public Customer create(Customer customer) {
        customer = customer.toBuilder()
                .id(UUID.randomUUID())
                .build();
        customerStore.store(customer);

        return customer;
    }

    @Override
    public Optional<Customer> get(UUID id) {
        return customerStore.get(id);
    }

    @Override
    public List<Customer> getAll() {
        return customerStore.getAll();
    }
}
