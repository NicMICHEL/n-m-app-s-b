package com.safetynet.model;

import java.util.List;

public record MedicalRecord(String firstName, String lastName, String birthdate,
                            List<String> medications, List<String> allergies) {
    public MedicalRecord() {
        this("", "", "", null, null);
    }
}
