package com.seb.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Customer {
    private UUID id;
    private String firstname;
    private String surname;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;

    public CustomerBuilder toBuilder() {
        return builder()
                .id(id)
                .firstname(firstname)
                .surname(surname)
                .dateOfBirth(dateOfBirth)
                .email(email)
                .phone(phone);
    }
}