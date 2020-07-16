package com.seb.customer.store;

import com.seb.customer.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerStore {

    private final Map<UUID, Customer> customerMap = new ConcurrentHashMap<>();

    public void store(Customer customer) {
        customerMap.put(customer.getId(), customer);
    }

    public Optional<Customer> get(UUID id) {
        return Optional.ofNullable(customerMap.get(id));
    }

    public List<Customer> getAll() {
        return new ArrayList<>(customerMap.values());
    }
}
