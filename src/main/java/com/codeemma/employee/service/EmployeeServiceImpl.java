package com.codeemma.employee.service;

import com.codeemma.employee.exception.EmployeeNotFound;
import com.codeemma.employee.model.Employee;
import com.codeemma.employee.store.EmployeeStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeStore employeeStore;

    public EmployeeServiceImpl(EmployeeStore employeeStore) {
        this.employeeStore = employeeStore;
    }

    @Override
    public Employee create(Employee employee) {
        employee = employee.toBuilder()
                .id(UUID.randomUUID())
                .build();
        employeeStore.store(employee);

        return employee;
    }

    @Override
    public Optional<Employee> get(UUID id) {
        return employeeStore.get(id);
    }

    @Override
    public List<Employee> getAll() {
        return employeeStore.getAll();
    }

    @Override
    public Employee update(UUID id, Employee employee) {
        Employee existing = get(id).orElseThrow(() -> new EmployeeNotFound("employee does not exist"));

        employee = employee.toBuilder()
                .id(existing.getId())
                .build();
        employeeStore.store(employee);
        return employee;
    }

    @Override
    public void delete(UUID id) {
        employeeStore.remove(id);
    }
//
//    @Override
//    public Flux<Employee> getAllRx() {
//        return employeeStore.getAllRx();
//    }
}
