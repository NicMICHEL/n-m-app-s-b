package com.safetynet.model;

import java.util.List;

public record PersonsByFireStation(String fireStation, String childrenCount, String adultsCount,
                                   List<LastNameFirstNamePhoneByAddress> lastNameFirstNamePhoneByAddresses) {
}
