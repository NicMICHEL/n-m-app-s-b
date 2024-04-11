package com.safetynet.service;

import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.MedicalRecordRepository;
import com.safetynet.repository.NotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public MedicalRecord save(MedicalRecord medicalRecord) {
        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);
        return savedMedicalRecord;
    }

    public MedicalRecord update(MedicalRecord medicalRecord) throws NotFoundException {
        MedicalRecord updatedMedicalRecord = medicalRecordRepository.update(medicalRecord);
        return updatedMedicalRecord;
    }

    public void delete(String firstName, String lastName) throws NotFoundException {
        medicalRecordRepository.delete(firstName, lastName);
    }

    public MedicalRecord get(String firstName, String lastName) throws NotFoundException {
        return medicalRecordRepository.findMedicalRecordByFirstNameLastName(firstName, lastName);
    }

}
