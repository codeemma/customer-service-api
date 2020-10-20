package com.codeemma.employee.http;

import com.codeemma.employee.exception.EmployeeNotFound;
import com.codeemma.employee.http.entity.ExternalEmployeeCreate;
import com.codeemma.employee.model.Employee;
import com.codeemma.employee.service.EmployeeService;
import com.codeemma.employee.store.EmployeeStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
//
//    @Autowired
//    private EmployeeStore employeeStore;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void createShouldReturnSuccess() throws Exception {
        when(employeeService.create(any(Employee.class)))
                .thenReturn(employee().toInternal().toBuilder().id(UUID.randomUUID()).build());

        mockMvc.perform(post("/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstname", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.phone", is("+37060111111")))
                .andExpect(jsonPath("$.email", is("email@test.com")))
                .andExpect(jsonPath("$.dateOfBirth", is("2000-01-10")));

        verify(employeeService).create(any(Employee.class));
    }

    @Test
    public void createWithEmptyNamesShouldReturn422() throws Exception {
        mockMvc.perform(post("/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ExternalEmployeeCreate.builder().firstname(" ").surname(" ").build())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createWithNullfieldsShouldReturn422() throws Exception {
        mockMvc.perform(post("/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ExternalEmployeeCreate.builder().build())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createWithBadPhoneShouldReturn422() throws Exception {
        mockMvc.perform(post("/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee().toBuilder().phone("000000").build())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getWithNonExistingIdShouldReturn404() throws Exception {
        mockMvc.perform(get("/employees/{0}", "38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getWithInvalidUUIdShouldReturn400() throws Exception {
        mockMvc.perform(get("/employees/{0}", "38400000-8cf0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getWithValidExistingIdShouldReturnSuccess() throws Exception {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        when(employeeService.get(id))
                .thenReturn(Optional.of(
                        employee()
                                .toInternal()
                                .toBuilder()
                                .id(id)
                                .build())
                );

        mockMvc.perform(get("/employees/{0}", "38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("38400000-8cf0-11bd-b23e-10b96e4ef00d")))
                .andExpect(jsonPath("$.firstname", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.phone", is("+37060111111")))
                .andExpect(jsonPath("$.email", is("email@test.com")))
                .andExpect(jsonPath("$.dateOfBirth", is("2000-01-10")));

        verify(employeeService).get(id);
    }

    @Test
    public void getAllShouldReturnSuccess() throws Exception {
        when(employeeService.getAll())
                .thenReturn(Collections.singletonList(
                        employee()
                                .toInternal()
                                .toBuilder()
                                .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                                .build())
                );

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("38400000-8cf0-11bd-b23e-10b96e4ef00d")))
                .andExpect(jsonPath("$[0].firstname", is("John")))
                .andExpect(jsonPath("$[0].surname", is("Doe")))
                .andExpect(jsonPath("$[0].phone", is("+37060111111")))
                .andExpect(jsonPath("$[0].email", is("email@test.com")))
                .andExpect(jsonPath("$[0].dateOfBirth", is("2000-01-10")));

        verify(employeeService).getAll();
    }

    @Test
    public void updateShouldReturnSuccess() throws Exception {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        Employee employee = employee()
                .toInternal()
                .toBuilder()
                .id(id)
                .build();
        when(employeeService.update(eq(id), any(Employee.class)))
                .thenAnswer(arg ->
                        ((Employee) arg.getArgument(1)).toBuilder().id(arg.getArgument(0)).build());

        mockMvc.perform(put("/employees/{0}", "38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("38400000-8cf0-11bd-b23e-10b96e4ef00d")))
                .andExpect(jsonPath("$.firstname", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.phone", is("+37060111111")))
                .andExpect(jsonPath("$.email", is("email@test.com")))
                .andExpect(jsonPath("$.dateOfBirth", is("2000-01-10")));

        verify(employeeService).update(eq(id), any(Employee.class));
    }

    @Test
    public void updateShouldReturn404WhenIdNotFound() throws Exception {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        when(employeeService.update(eq(id), any(Employee.class)))
                .thenThrow(new EmployeeNotFound("employee does not exist"));

        mockMvc.perform(put("/employees/{0}", "38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee())))
                .andExpect(status().isNotFound());

        verify(employeeService).update(eq(id), any(Employee.class));
    }

    @Test
    public void deleteShouldReturnSuccess() throws Exception {
        UUID id = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");

        mockMvc.perform(delete("/employees/{0}", "38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .andExpect(status().isOk());

        verify(employeeService).delete(id);
    }

    private ExternalEmployeeCreate employee() {
        return ExternalEmployeeCreate.builder()
                .firstname("John")
                .surname("Doe")
                .phone("+37060111111")
                .email("email@test.com")
                .dateOfBirth(LocalDate.parse("2000-01-10"))
                .build();
    }
}