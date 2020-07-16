package com.seb.customer.http.entity;

import com.seb.customer.entity.Customer;
import com.seb.customer.http.validate.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ExternalCustomerCreate {
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

    public Customer toInternal() {
        return Customer.builder()
                .firstname(firstname)
                .surname(surname)
                .dateOfBirth(dateOfBirth)
                .email(email)
                .phone(phone)
                .build();
    }

    public ExternalCustomerCreateBuilder toBuilder() {
        return builder()
                .firstname(firstname)
                .surname(surname)
                .dateOfBirth(dateOfBirth)
                .email(email)
                .phone(phone);
    }
}
