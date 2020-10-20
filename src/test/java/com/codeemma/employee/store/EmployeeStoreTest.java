package com.codeemma.employee.store;

import com.codeemma.employee.model.Employee;
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
public class EmployeeStoreTest {

    private static final UUID EMPLOYEE_ID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

    private EmployeeStore employeeStore;

    @Before
    public void setUp() {
        employeeStore = new EmployeeStore();
    }

    @Test
    public void storeShouldReturnCustomer() {
        Optional<Employee> result = employeeStore.get(EMPLOYEE_ID);

        assertFalse(result.isPresent());
    }

    @Test
    public void getShouldReturnEmptyWhenNotInStore() {
        Optional<Employee> result = employeeStore.get(EMPLOYEE_ID);

        assertFalse(result.isPresent());
    }

    @Test
    public void getShouldReturnCustomerWhenInStore() {
        Employee employee = Employee.builder().id(EMPLOYEE_ID).build();
        employeeStore.store(employee);

        Optional<Employee> result = employeeStore.get(EMPLOYEE_ID);

        assertTrue(result.isPresent());
        assertEquals(employee.getId(), EMPLOYEE_ID);
    }

    @Test
    public void removeShouldDeleteItemFromStore() {
        Employee employee = Employee.builder().id(EMPLOYEE_ID).build();
        employeeStore.store(employee);

        employeeStore.remove(EMPLOYEE_ID);

        assertFalse(employeeStore.get(EMPLOYEE_ID).isPresent());
    }
}