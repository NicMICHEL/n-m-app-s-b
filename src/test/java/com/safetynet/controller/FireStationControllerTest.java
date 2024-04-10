package com.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.FireStation;
import com.safetynet.repository.NotFoundException;
import com.safetynet.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(FireStationController.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    @Test
    public void should_create_fireStation_successfully() throws Exception {
        FireStation savedTestStation = new FireStation("95 rue Coutel", "555");
        when(fireStationService.save(savedTestStation)).thenReturn(savedTestStation);
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(savedTestStation);
        mockMvc.perform(post("/fireStation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(jsonPath("$.address").value("95 rue Coutel"))
                .andExpect(jsonPath("$.station").value("555"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_update_fireStation_successfully() throws Exception {
        FireStation updatedFirestation = new FireStation("95 rue Coutel", "555");
        when(fireStationService.update(updatedFirestation)).thenReturn(updatedFirestation);
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(updatedFirestation);
        mockMvc.perform(put("/fireStation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(jsonPath("$.address").value("95 rue Coutel"))
                .andExpect(jsonPath("$.station").value("555"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_address_corresponding_to_firestation_to_update_is_not_found()
            throws Exception {
        FireStation updatedFirestation = new FireStation("5 rue Bois", "222");
        when(fireStationService.update(updatedFirestation)).thenThrow
                (new NotFoundException("Unable to find and update fireStation corresponding to address 5 rue Bois"));
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(updatedFirestation);
        mockMvc.perform(put("/fireStation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString
                        ("Unable to find and update fireStation corresponding to address 5 rue Bois")));
    }

    @Test
    public void should_get_fireStation_successfully() throws Exception {
        FireStation testStation = new FireStation("95 rue Coutel", "555");
        when(fireStationService.get("95 rue Coutel")).thenReturn(testStation);
        mockMvc.perform(get("/fireStation/{address}", "95 rue Coutel"))
                .andExpect(jsonPath("$.station").value("555"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_address_corresponding_to_firestation_to_get_is_not_found()
            throws Exception {
        when(fireStationService.get("5 rue Bois")).thenThrow
                (new NotFoundException("Unable to find fireStation corresponding to address 5 rue Bois"));
        mockMvc.perform(get("/fireStation/{address}", "5 rue Bois"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString
                        ("Unable to find fireStation corresponding to address 5 rue Bois")));
    }

    @Test
    public void should_delete_fireStation_successfully() throws Exception {
        doNothing().when(fireStationService).delete("95 rue Coutel");
        mockMvc.perform(delete("/fireStation/{address}", "95 rue Coutel"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_address_corresponding_to_firestation_to_delete_is_not_found()
            throws Exception {
        doThrow(new NotFoundException("Unable to find and delete fireStation corresponding to address 5 rue Bois"))
                .when(fireStationService).delete("5 rue Bois");
        mockMvc.perform(delete("/fireStation/{address}", "5 rue Bois"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString
                        ("Unable to find and delete fireStation corresponding to address 5 rue Bois")));
    }

}
