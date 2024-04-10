package com.safetynet.model;

public record FireStation(String address, String station) {
    public FireStation() {
        this("", "");
    }
}
