package com.safetynet.model;

import java.util.List;

public record InfosByPerson(String firstName, String lastName, String address, String age,
                            String email, List<String> medications, List<String> allergies) {

    public InfosByPerson() {
        this("", "", "", "", "", null, null);
    }
}
