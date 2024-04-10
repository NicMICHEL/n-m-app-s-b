package com.safetynet.service;

import com.safetynet.model.FireStation;
import com.safetynet.repository.FireStationRepository;
import com.safetynet.repository.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {

    @Mock
    private FireStationRepository fireStationRepository;

    @InjectMocks
    private FireStationService fireStationService;

    @Test
    public void should_save_fireStation_successfully() {
        FireStation fireStation = new FireStation();
        FireStation savedFireStation = new FireStation();
        when(fireStationRepository.save(fireStation)).thenReturn(savedFireStation);
        fireStationService.save(fireStation);
        verify(fireStationRepository).save(fireStation);
    }

    @Test
    public void should_update_fireStation_successfully() throws NotFoundException {
        FireStation fireStation = new FireStation();
        FireStation updatedFirestation = new FireStation();
        when(fireStationRepository.update(fireStation)).thenReturn(updatedFirestation);
        fireStationService.update(fireStation);
        verify(fireStationRepository).update(fireStation);
    }

    @Test
    public void should_delete_fireStation_successfully() throws NotFoundException {
        doNothing().when(fireStationRepository).delete("99th road");
        fireStationService.delete("99th road");
        verify(fireStationRepository).delete("99th road");
    }

    @Test
    public void should_get_fireStation_successfully() throws NotFoundException {
        FireStation fireStation = new FireStation();
        when(fireStationRepository.findByAddress("99th road")).thenReturn(fireStation);
        fireStationService.get("99th road");
        verify(fireStationRepository).findByAddress("99th road");
    }

}
