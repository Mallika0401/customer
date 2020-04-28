package de.mallika.customer.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDateTime dob;
}
