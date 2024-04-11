package com.safetynet.model;

import java.util.List;

public record HomePerson(String firstName, String lastName, String phone, String age, List<String> medications,
                         List<String> allergies) {
    public HomePerson() {
        this("", "", "", "", null, null);
    }
}
