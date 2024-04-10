package com.safetynet.model;

public record Person(String firstName, String lastName, String address, String city,
                     String zip, String phone, String email) {
    public Person() {
        this("", "", "", "", "", "", "");
    }
}
