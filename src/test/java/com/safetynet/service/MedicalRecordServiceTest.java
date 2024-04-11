package com.safetynet.service;


import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.MedicalRecordRepository;
import com.safetynet.repository.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {


    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @Test
    public void should_save_medicalRecord_successfully() throws NotFoundException {
        MedicalRecord medicalRecord = new MedicalRecord();
        MedicalRecord savedMedicalRecord = new MedicalRecord();
        when(medicalRecordRepository.save(medicalRecord)).thenReturn(savedMedicalRecord);
        medicalRecordService.save(medicalRecord);
        verify(medicalRecordRepository).save(medicalRecord);
    }

    @Test
    public void should_update_medicalRecord_successfully() throws NotFoundException {
        MedicalRecord medicalRecord = new MedicalRecord();
        MedicalRecord updatedMedicalRecord = new MedicalRecord();
        when(medicalRecordRepository.update(medicalRecord)).thenReturn(updatedMedicalRecord);
        medicalRecordService.update(medicalRecord);
        verify(medicalRecordRepository).update(medicalRecord);
    }

    @Test
    public void should_delete_medicalRecord_successfully() throws NotFoundException {
        doNothing().when(medicalRecordRepository).delete("Ali", "BABA");
        medicalRecordService.delete("Ali", "BABA");
        verify(medicalRecordRepository).delete("Ali", "BABA");
    }

    @Test
    public void should_get_medicalRecord_successfully() throws NotFoundException {
        MedicalRecord medicalRecord = new MedicalRecord();
        when(medicalRecordRepository.findMedicalRecordByFirstNameLastName("Ali", "BABA")).thenReturn(medicalRecord);
        medicalRecordService.get("Ali", "BABA");
        verify(medicalRecordRepository).findMedicalRecordByFirstNameLastName("Ali", "BABA");
    }

}
