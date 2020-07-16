package com.seb.customer.store;

import com.seb.customer.entity.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CustomerStoreTest {

    private static final UUID CUSTOMER_ID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

    private CustomerStore customerStore;

    @Before
    public void setUp() {
        customerStore = new CustomerStore();
    }

    @Test
    public void storeShouldReturnCustomer() {
        Optional<Customer> result = customerStore.get(CUSTOMER_ID);

        assertFalse(result.isPresent());
    }

    @Test
    public void getShouldReturnEmptyWhenNotInStore() {
        Optional<Customer> result = customerStore.get(CUSTOMER_ID);

        assertFalse(result.isPresent());
    }

    @Test
    public void getShouldReturnCustomerWhenInStore() {
        Customer customer = Customer.builder().id(CUSTOMER_ID).build();
        customerStore.store(customer);

        Optional<Customer> result = customerStore.get(CUSTOMER_ID);

        assertTrue(result.isPresent());
        assertEquals(customer.getId(), CUSTOMER_ID);
    }
}