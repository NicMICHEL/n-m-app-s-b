package com.safetynet.service;

import com.safetynet.model.FireStation;
import com.safetynet.repository.FireStationRepository;
import com.safetynet.repository.NotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class FireStationService {

    @Autowired
    private FireStationRepository fireStationRepository;

    public FireStation save(FireStation fireStation) {
        FireStation savedFireStation = fireStationRepository.save(fireStation);
        return savedFireStation;
    }

    public FireStation update(FireStation fireStation) throws NotFoundException {
        FireStation updatedFirestation = fireStationRepository.update(fireStation);
        return updatedFirestation;
    }

    public void delete(String address) throws NotFoundException {
        fireStationRepository.delete(address);
    }

    public FireStation get(String address) throws NotFoundException {
        return fireStationRepository.findByAddress(address);
    }

}
