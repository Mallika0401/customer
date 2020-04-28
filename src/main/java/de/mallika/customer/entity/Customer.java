package de.mallika.customer.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDateTime dob;
}
