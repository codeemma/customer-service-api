package com.seb.customer.service;

import com.seb.customer.entity.Customer;
import com.seb.customer.store.CustomerStore;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private CustomerStore customerStore;

    private CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        customerService = new CustomerServiceImpl(customerStore);
    }

    @Test
    public void testCreateShouldAddId() {
        Customer result = customerService.create(customer());

        verify(customerStore).store(any(Customer.class));
        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void getWhenNotInstoreShouldReturnEmpty() {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        when(customerStore.get(id)).thenReturn(Optional.empty());

        Optional<Customer> result = customerService.get(id);

        verify(customerStore).get(id);
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void getWhenCustomerIsInStoreShouldReturnCustomer() {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        when(customerStore.get(id)).thenReturn(Optional.of(mock(Customer.class)));

        Optional<Customer> result = customerService.get(id);

        verify(customerStore).get(id);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void getAllWhenStoreIsEmptyShouldReturnEmpty() {
        List<Customer> result = customerService.getAll();

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void getAllWhenStoreIsNotEmptyShouldReturnAll() {
        when(customerService.getAll())
                .thenReturn(singletonList(
                        customer().toBuilder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d")).build()));

        List<Customer> result = customerService.getAll();

        verify(customerStore).getAll();
        assertThat(result).hasSize(1);
    }


    private Customer customer() {
        return Customer.builder()
                .firstname("John")
                .surname("Doe")
                .phone("+37060111111")
                .email("email@test.com")
                .dateOfBirth(LocalDate.parse("2000-01-10"))
                .build();
    }
}