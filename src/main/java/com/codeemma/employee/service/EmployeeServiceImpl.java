package com.codeemma.employee.service;

import com.codeemma.employee.exception.EmployeeNotFound;
import com.codeemma.employee.model.Employee;
import com.codeemma.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(Employee employee) {
        employee = employee.toBuilder()
                .id(UUID.randomUUID())
                .build();
        employeeRepository.save(employee);

        return employee;
    }

    @Override
    public Optional<Employee> get(UUID id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee update(UUID id, Employee employee) {
        Employee existing = get(id).orElseThrow(() -> new EmployeeNotFound("employee does not exist"));

        employee = employee.toBuilder()
                .id(existing.getId())
                .build();
        employeeRepository.save(employee);
        return employee;
    }

    @Override
    public void delete(UUID id) {
        employeeRepository.deleteById(id);
    }
//
//    @Override
//    public Flux<Employee> getAllRx() {
//        return employeeStore.getAllRx();
//    }
}
