package com.codeemma.employee.store;

import com.codeemma.employee.model.Employee;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class EmployeeStore {

    private final ConcurrentMap<UUID, Employee> employeeMap = new ConcurrentHashMap<>();

    public void store(Employee employee) {
        employeeMap.put(employee.getId(), employee);
    }

    public Optional<Employee> get(UUID id) {
        return Optional.ofNullable(employeeMap.get(id));
    }

    public List<Employee> getAll() {
        return new ArrayList<>(employeeMap.values());
    }

    public void remove(UUID id) {
        employeeMap.remove(id);
    }
//
//    public Flux<Employee> getAllRx() {
////        return Flux.fromArray(employeeMap.values());
//    }
}
