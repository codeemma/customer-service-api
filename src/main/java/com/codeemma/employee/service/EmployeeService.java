package com.codeemma.employee.service;

import com.codeemma.employee.model.Employee;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {

    Employee create(Employee employee);

    Optional<Employee> get(UUID id);

    List<Employee> getAll();

    Employee update(UUID id, Employee employee);

    void delete(UUID id);

//    Flux<Employee> getAllRx();
}
