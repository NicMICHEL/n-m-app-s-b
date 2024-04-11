package com.safetynet.repository;

import com.safetynet.model.FireStation;
import com.safetynet.util.RawData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Repository
public class FireStationRepository {

    public FireStationRepository() {
    }

    private static final Logger logger = LogManager.getLogger(FireStationRepository.class);

    // Convert the data file data.json to a list of fire stations
    public static List<FireStation> fireStationDeserializer() throws IOException {
        return RawData.rawDataDeserializer().firestations;
    }

    public static List<FireStation> fireStations;

    static {
        try {
            fireStations = fireStationDeserializer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, FireStation> indexByAddress(List<FireStation> fireStations) {
        Map<String, FireStation> fireStationsByAddress = new HashMap<>();
        for (FireStation station : fireStations) {
            fireStationsByAddress.put(station.address(), station);
        }
        return fireStationsByAddress;
    }

    public static Map<String, FireStation> fireStationsByAddress = indexByAddress(fireStations);

    public FireStation save(FireStation fireStation) {
        logger.debug("Creating new fireStation {}", fireStation);
        fireStationsByAddress.put(fireStation.address(), fireStation);
        return fireStationsByAddress.get(fireStation.address());
    }

    public FireStation update(FireStation fireStation) throws NotFoundException {
        if (fireStationsByAddress.get(fireStation.address()) != null) {
            logger.debug("Updating fireStation corresponding to address {}", fireStation.address());
            fireStationsByAddress.put(fireStation.address(), fireStation);
            return fireStationsByAddress.get(fireStation.address());
        } else {
            logger.error("Unable to find and update fireStation corresponding to address {}", fireStation.address());
            throw new NotFoundException(String.format("Unable to find and update fireStation corresponding to address %s", fireStation.address()));
        }
    }

    public void delete(String address) throws NotFoundException {
        if (fireStationsByAddress.get(address) != null) {
            logger.debug("Deleting fireStation corresponding to address {}", address);
            fireStationsByAddress.remove(address);
        } else {
            logger.error("Unable to find and delete fireStation corresponding to address {}", address);
            throw new NotFoundException(String.format("Unable to find and delete fireStation corresponding to address %s", address));
        }
    }

    public FireStation findByAddress(String address) throws NotFoundException {
        if (fireStationsByAddress.get(address) != null) {
            logger.debug("Getting fireStation corresponding to address {}", address);
            return fireStationsByAddress.get(address);
        } else {
            logger.error("Unable to find fireStation corresponding to address {}", address);
            throw new NotFoundException(String.format("Unable to find fireStation corresponding to address %s", address));
        }
    }

    public TreeSet<String> getAddressesByFireStation(String firestation) throws NotFoundException {
        TreeSet<String> addresses = new TreeSet<>();
        for (Map.Entry<String, FireStation> entry : fireStationsByAddress.entrySet()) {
            if ((entry.getValue().station()).equals(firestation)) {
                addresses.add(entry.getValue().address());
            }
        }
        if (!addresses.isEmpty()) {
            return addresses;
        } else {
            logger.error("Firestation {} not found in database", firestation);
            throw new NotFoundException(String.format("Firestation %s not found in database", firestation));
        }
    }

}
