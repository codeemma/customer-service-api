package com.seb.customer.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb.customer.entity.Customer;
import com.seb.customer.http.entity.ExternalCustomerCreate;
import com.seb.customer.store.CustomerStore;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerStore customerStore;

    @Test
    public void createShouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(customer())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstname", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.phone", is("+37060111111")))
                .andExpect(jsonPath("$.email", is("email@test.com")))
                .andExpect(jsonPath("$.dateOfBirth", is("2000-01-10")));
    }

    @Test
    public void createWithEmptyNamesShouldReturn422() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ExternalCustomerCreate.builder().firstname(" ").surname(" ").build())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createWithNullfieldsShouldReturn422() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ExternalCustomerCreate.builder().build())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createWithBadPhoneShouldReturn422() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(customer().toBuilder().phone("000000").build())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getWithNonExistingIdShouldReturn404() throws Exception {
        mockMvc.perform(get("/customers/{0}", "38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getWithInvalidUUIdShouldReturn400() throws Exception {
        mockMvc.perform(get("/customers/{0}", "38400000-8cf0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getWithValidExistingIdShouldReturnSuccess() throws Exception {
        customerStore.store(
                customer()
                        .toInternal()
                        .toBuilder()
                        .id(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                        .build()
        );

        mockMvc.perform(get("/customers/{0}", "38400000-8cf0-11bd-b23e-10b96e4ef00d"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("38400000-8cf0-11bd-b23e-10b96e4ef00d")))
                .andExpect(jsonPath("$.firstname", is("John")))
                .andExpect(jsonPath("$.surname", is("Doe")))
                .andExpect(jsonPath("$.phone", is("+37060111111")))
                .andExpect(jsonPath("$.email", is("email@test.com")))
                .andExpect(jsonPath("$.dateOfBirth", is("2000-01-10")));
    }

    private ExternalCustomerCreate customer() {
        return ExternalCustomerCreate.builder()
                .firstname("John")
                .surname("Doe")
                .phone("+37060111111")
                .email("email@test.com")
                .dateOfBirth(LocalDate.parse("2000-01-10"))
                .build();
    }
}