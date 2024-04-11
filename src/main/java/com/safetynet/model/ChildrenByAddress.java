package com.safetynet.model;

import java.util.List;

public record ChildrenByAddress(String address, List<Child> children, List<LastNameFirstName> adults) {
}
