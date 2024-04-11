package com.safetynet.model;

import java.util.List;

public record HomesByFireStation(String fireStation, List<HomeByAddress> homesByAddress) {
}
