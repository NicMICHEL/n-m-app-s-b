package com.safetynet.model;

import java.util.List;

public record LastNameFirstNamePhoneByAddress(String address, List<LastNameFirstNamePhone> lastNameFirstNamePhones) {
}
