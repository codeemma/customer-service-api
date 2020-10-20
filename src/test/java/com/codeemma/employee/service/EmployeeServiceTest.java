package com.codeemma.employee.service;

import com.codeemma.employee.exception.EmployeeNotFound;
import com.codeemma.employee.store.EmployeeStore;
import com.codeemma.employee.model.Employee;
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
public class EmployeeServiceTest {

    @Mock
    private EmployeeStore employeeStore;

    private EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
        employeeService = new EmployeeServiceImpl(employeeStore);
    }

    @Test
    public void testCreateShouldAddId() {
        Employee result = employeeService.create(employee());

        verify(employeeStore).store(any(Employee.class));
        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void getWhenNotInstoreShouldReturnEmpty() {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        when(employeeStore.get(id)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.get(id);

        verify(employeeStore).get(id);
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void getWhenEmployeeIsInStoreShouldReturnCustomer() {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        when(employeeStore.get(id)).thenReturn(Optional.of(mock(Employee.class)));

        Optional<Employee> result = employeeService.get(id);

        verify(employeeStore).get(id);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void getAllWhenStoreIsEmptyShouldReturnEmpty() {
        List<Employee> result = employeeService.getAll();

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void getAllWhenStoreIsNotEmptyShouldReturnAll() {
        when(employeeService.getAll())
                .thenReturn(singletonList(
                        employee().toBuilder().id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d")).build()));

        List<Employee> result = employeeService.getAll();

        verify(employeeStore).getAll();
        assertThat(result).hasSize(1);
    }

    @Test
    public void updateWhenEmployeeIsInStoreShouldUpdate() {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        Employee employee = employee().toBuilder().id(id).build();
        when(employeeStore.get(id)).thenReturn(Optional.of(employee));

        Employee result = employeeService.update(id, employee());

        verify(employeeStore).get(id);
        verify(employeeStore).store(any(Employee.class));
        assertThat(result).isEqualToComparingFieldByField(employee);
    }

    @Test(expected = EmployeeNotFound.class)
    public void updateWhenEmployeeIsInStoreShouldThrowException() {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        when(employeeStore.get(id)).thenReturn(Optional.empty());

        Employee result = employeeService.update(id, employee());
    }

    @Test
    public void deleteShouldRemoveEmployee() {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        employeeService.delete(id);

        verify(employeeStore).remove(id);
    }



    private Employee employee() {
        return Employee.builder()
                .firstname("John")
                .surname("Doe")
                .phone("+37060111111")
                .email("email@test.com")
                .dateOfBirth(LocalDate.parse("2000-01-10"))
                .build();
    }
}