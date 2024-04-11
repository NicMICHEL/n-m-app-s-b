package com.safetynet.model;

import java.util.List;

public record HomeByAddress(String address, List<HomePerson> homePersons) {
}
