package com.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.Person;
import com.safetynet.repository.NotFoundException;
import com.safetynet.service.PersonService;
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
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void should_create_person_successfully() throws Exception {
        Person savedPerson = new Person("Alan", "CRUG", "1st street",
                "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        when(personService.save(savedPerson)).thenReturn(savedPerson);
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(savedPerson);
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("CRUG"))
                .andExpect(jsonPath("$.address").value("1st street"))
                .andExpect(jsonPath("$.city").value("Dax"))
                .andExpect(jsonPath("$.zip").value("40100"))
                .andExpect(jsonPath("$.phone").value("111-222-3333"))
                .andExpect(jsonPath("$.email").value("alan.crug@jmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_update_person_successfully() throws Exception {
        Person updatedPerson = new Person("Alan", "CRUG", "1st street",
                "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        when(personService.update(updatedPerson)).thenReturn(updatedPerson);
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(updatedPerson);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("CRUG"))
                .andExpect(jsonPath("$.address").value("1st street"))
                .andExpect(jsonPath("$.city").value("Dax"))
                .andExpect(jsonPath("$.zip").value("40100"))
                .andExpect(jsonPath("$.phone").value("111-222-3333"))
                .andExpect(jsonPath("$.email").value("alan.crug@jmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastName_corresponding_to_person_to_update_is_not_found()
            throws Exception {
        Person updatedPerson = new Person("Al", "UG", "1st street",
                "Dax", "40100", "111-222-3333", "al.ug@jmail.com");
        when(personService.update(updatedPerson)).thenThrow(new NotFoundException("Unable to find " +
                "and update person corresponding to LastName UG and FistName Al"));
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(updatedPerson);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Unable to find " +
                        "and update person corresponding to LastName UG and FistName Al")));
    }

    @Test
    public void should_get_person_successfully() throws Exception {
        Person testPerson = new Person("Alan", "CRUG", "1st street",
                "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        when(personService.get("Alan", "CRUG")).thenReturn(testPerson);
        mockMvc.perform(get("/person/{firstName}/{lastName}", "Alan", "CRUG"))
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("CRUG"))
                .andExpect(jsonPath("$.address").value("1st street"))
                .andExpect(jsonPath("$.city").value("Dax"))
                .andExpect(jsonPath("$.zip").value("40100"))
                .andExpect(jsonPath("$.phone").value("111-222-3333"))
                .andExpect(jsonPath("$.email").value("alan.crug@jmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastName_corresponding_to_person_to_get_is_not_found()
            throws Exception {
        when(personService.get("Al", "UG")).thenThrow(new NotFoundException("Unable to find person corresponding" +
                " to LastName UG and FistName Al"));
        mockMvc.perform(get("/person/{firstName}/{lastName}", "Al", "UG"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Unable to find person corresponding" +
                        " to LastName UG and FistName Al")));
    }

    @Test
    public void should_delete_person_successfully() throws Exception {
        doNothing().when(personService).delete("Alan", "CRUG");
        mockMvc.perform(delete("/person/{firstName}/{lastName}", "Alan", "CRUG"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastName_corresponding_to_person_to_delete_is_not_found()
            throws Exception {
        doThrow(new NotFoundException("Unable to find and delete person corresponding to LastName UG" +
                " and FistName Al")).when(personService).delete("Al", "UG");
        mockMvc.perform(delete("/person/{firstName}/{lastName}", "Al", "UG"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Unable to find and delete person" +
                        " corresponding to LastName UG and FistName Al")));
    }

}
