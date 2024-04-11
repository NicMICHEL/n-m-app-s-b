package com.safetynet.model;

import java.util.List;

public record HomePersonsByAddress(String address, String fireStation, List<HomePerson> homePersons) {
    public HomePersonsByAddress() {
        this("", "", null);
    }
}
