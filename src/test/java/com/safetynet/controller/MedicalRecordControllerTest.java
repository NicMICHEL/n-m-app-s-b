package com.safetynet.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.NotFoundException;
import com.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MedicalRecordService medicalRecordService;

    @Test
    public void should_create_MedicalRecord_successfully() throws Exception {
        List<String> medications = new ArrayList<>();
        medications.add("amol:1g");
        medications.add("lugan:6mg");
        List<String> allergies = new ArrayList<>();
        allergies.add("nuts");
        allergies.add("fish");
        MedicalRecord savedMedicalRecord = new MedicalRecord("Alan", "CRUG", "03/01/1980",
                medications, allergies );
        when(medicalRecordService.save(savedMedicalRecord)).thenReturn(savedMedicalRecord);
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(savedMedicalRecord);
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("CRUG"))
                .andExpect(jsonPath("$.birthdate").value("03/01/1980"))
                .andExpect(jsonPath("$.medications").isArray())
                .andExpect(jsonPath("$.medications", hasSize(2)))
                .andExpect(jsonPath("$.medications", hasItem("amol:1g")))
                .andExpect(jsonPath("$.medications", hasItem("lugan:6mg")))
                .andExpect(jsonPath("$.allergies").isArray())
                .andExpect(jsonPath("$.allergies", hasSize(2)))
                .andExpect(jsonPath("$.allergies", hasItem("nuts")))
                .andExpect(jsonPath("$.allergies", hasItem("fish")))
                .andExpect(status().isOk());
    }

    @Test
    public  void should_update_MedicalRecord_successfully() throws Exception {
        List<String> medications = new ArrayList<>();
        medications.add("amol:1g");
        List<String> allergies = new ArrayList<>();
        allergies.add("milk");
        allergies.add("seafood");
        MedicalRecord updatedMedicalRecord = new MedicalRecord("Alan", "CRUG", "03/01/1980",
                medications, allergies );
        when(medicalRecordService.update(updatedMedicalRecord)).thenReturn(updatedMedicalRecord);
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(updatedMedicalRecord);
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("CRUG"))
                .andExpect(jsonPath("$.birthdate").value("03/01/1980"))
                .andExpect(jsonPath("$.medications").isArray())
                .andExpect(jsonPath("$.medications", hasSize(1)))
                .andExpect(jsonPath("$.medications", hasItem("amol:1g")))
                .andExpect(jsonPath("$.allergies").isArray())
                .andExpect(jsonPath("$.allergies", hasSize(2)))
                .andExpect(jsonPath("$.allergies", hasItem("milk")))
                .andExpect(jsonPath("$.allergies", hasItem("seafood")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_Lastname_corresponding_to_MedicalRecord_to_update_is_not_found()
            throws Exception {
        List<String> medications = new ArrayList<>();
        medications.add("amol:1g");
        medications.add("lugan:6mg");
        List<String> allergies = new ArrayList<>();
        allergies.add("nuts");
        allergies.add("fish");
        MedicalRecord updatedMedicalRecord = new MedicalRecord("Al", "UG", "03/01/1980",
                medications, allergies );
        when(medicalRecordService.update(updatedMedicalRecord)).thenThrow(new NotFoundException("Unable to find " +
                "and update medicalRecord corresponding to LastName UG and FistName Al"));
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(updatedMedicalRecord);
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Unable to find " +
                        "and update medicalRecord corresponding to LastName UG and FistName Al")));
    }

    @Test
    public void should_get_MedicalRecord_successfully() throws Exception {
        List<String> medications = new ArrayList<>();
        medications.add("amol:1g");
        medications.add("lugan:6mg");
        List<String> allergies = new ArrayList<>();
        allergies.add("nuts");
        allergies.add("fish");
        MedicalRecord testMedicalRecord = new MedicalRecord("Alan", "CRUG", "03/01/1980",
                medications, allergies );
        when(medicalRecordService.get("Alan","CRUG")).thenReturn(testMedicalRecord);
        mockMvc.perform(get("/medicalRecord/{firstName}/{lastName}", "Alan","CRUG"))
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("CRUG"))
                .andExpect(jsonPath("$.birthdate").value("03/01/1980"))
                .andExpect(jsonPath("$.medications").isArray())
                .andExpect(jsonPath("$.medications", hasSize(2)))
                .andExpect(jsonPath("$.medications", hasItem("amol:1g")))
                .andExpect(jsonPath("$.medications", hasItem("lugan:6mg")))
                .andExpect(jsonPath("$.allergies").isArray())
                .andExpect(jsonPath("$.allergies", hasSize(2)))
                .andExpect(jsonPath("$.allergies", hasItem("nuts")))
                .andExpect(jsonPath("$.allergies", hasItem("fish")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_Lastname_corresponding_to_MedicalRecord_to_get_is_not_found()
            throws Exception {
        when(medicalRecordService.get("Al","UG")).thenThrow(new NotFoundException("Unable to find medicalRecord"
                + " corresponding to LastName UG and FistName Al"));
        mockMvc.perform(get("/medicalRecord/{firstName}/{lastName}", "Al","UG"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Unable to find medicalRecord corresponding" +
                        " to LastName UG and FistName Al")));
    }

    @Test
    public void should_delete_MedicalRecord_successfully() throws Exception {
        doNothing().when(medicalRecordService).delete("Alan","CRUG");
        mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", "Alan","CRUG"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_Lastname_corresponding_to_MedicalRecord_to_delete_is_not_found()
            throws Exception {
        doThrow(new NotFoundException("Unable to find and delete medicalRecord corresponding to LastName UG" +
                " and FistName Al")).when(medicalRecordService).delete("Al","UG");
        mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", "Al","UG"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Unable to find and delete medicalRecord" +
                        " corresponding to LastName UG and FistName Al")));
    }

}
