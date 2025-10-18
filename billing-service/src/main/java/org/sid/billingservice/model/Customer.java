package org.sid.billingservice.model;

import lombok.Data;

@Data
public class Customer {
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private String address;
    private String city;
    private String country;
}
