package com.codeemma.employee.http.entity;

import com.codeemma.employee.http.validate.ValidPhoneNumber;
import com.codeemma.employee.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ExternalEmployeeCreate {
    @NotBlank
    @NotNull
    private String firstname;
    @NotBlank
    @NotNull
    private String surname;
    @NotNull
    private LocalDate dateOfBirth;
    @ValidPhoneNumber
    @NotNull
    private String phone;
    @Email
    @NotNull
    private String email;

    @Override
    public String toString() {
        return "{\"ExternalCustomerCreate\":{"
                + "\"firstname\":\"" + firstname + "\""
                + ", \"surname\":\"" + surname + "\""
                + ", \"dateOfBirth\":" + dateOfBirth
                + ", \"phone\":\"" + phone + "\""
                + ", \"email\":\"" + email + "\""
                + "}}";
    }

    public Employee toInternal() {
        return Employee.builder()
                .firstname(firstname)
                .surname(surname)
                .dateOfBirth(dateOfBirth)
                .email(email)
                .phone(phone)
                .build();
    }

    public ExternalEmployeeCreateBuilder toBuilder() {
        return builder()
                .firstname(firstname)
                .surname(surname)
                .dateOfBirth(dateOfBirth)
                .email(email)
                .phone(phone);
    }
}
