package com.safetynet.controller;


import com.safetynet.model.*;
import com.safetynet.service.ServiceURL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceURL serviceURL;

    @Test
    public void should_get_emails_by_city_successfully() throws Exception {
        TreeSet<String> eMails = new TreeSet<>();
        eMails.add("alan.crug@jmail.com");
        eMails.add("tess.berg@jmail.com");
        when(serviceURL.getEmailsByCity("Dax")).thenReturn(eMails);
        mockMvc.perform(get("/communityEmail?city={city}", "Dax"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", hasItem("alan.crug@jmail.com")))
                .andExpect(jsonPath("$", hasItem("tess.berg@jmail.com")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_phones_by_fireStation_successfully() throws Exception {
        TreeSet<String> phones = new TreeSet<>();
        phones.add("111-222-3333");
        phones.add("000-000-0011");
        when(serviceURL.getPhonesByFireStation("5")).thenReturn(phones);
        mockMvc.perform(get("/phoneAlert?firestation={firestation}", "5"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", hasItem("111-222-3333")))
                .andExpect(jsonPath("$", hasItem("000-000-0011")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_infos_by_person_successfully() throws Exception {
        List<String> medications = new ArrayList<>();
        medications.add("amol:1g");
        medications.add("lugan:6mg");
        List<String> allergies = new ArrayList<>();
        allergies.add("nuts");
        allergies.add("fish");
        InfosByPerson infosByPersonAlan = new InfosByPerson("Alan", "CRUG",
                "1st street", "44", "alan.crug@jmail.com", medications, allergies);
        when(serviceURL.getInfosByPerson("Alan", "CRUG")).thenReturn(infosByPersonAlan);
        mockMvc.perform(get("/personInfo?firstName={firstName}&lastName={lastName}",
                        "Alan", "CRUG"))
                .andExpect(jsonPath("$.firstName").value("Alan"))
                .andExpect(jsonPath("$.lastName").value("CRUG"))
                .andExpect(jsonPath("$.address").value("1st street"))
                .andExpect(jsonPath("$.age").value("44"))
                .andExpect(jsonPath("$.email").value("alan.crug@jmail.com"))
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
    public void should_get_homes_by_fireStation_successfully() throws Exception {
        List<String> medicationsAlan = new ArrayList<>();
        medicationsAlan.add("amol:1g");
        medicationsAlan.add("lugan:6mg");
        List<String> allergiesAlan = new ArrayList<>();
        allergiesAlan.add("nuts");
        allergiesAlan.add("fish");
        HomePerson homePersonAlan = new HomePerson("Alan", "CRUG", "111-222-3333",
                "44", medicationsAlan, allergiesAlan);
        List<String> medicationsIda = new ArrayList<>();
        medicationsIda.add("cyclaz:50mg");
        medicationsIda.add("xadin:3mg");
        List<String> allergiesIda = new ArrayList<>();
        allergiesIda.add("lacilan");
        allergiesIda.add("milk");
        HomePerson homePersonIda = new HomePerson("Ida", "ROLA", "011-022-0333",
                "21", medicationsIda, allergiesIda);
        List<HomePerson> homePersonsAlanIda = new ArrayList<>();
        homePersonsAlanIda.add(homePersonAlan);
        homePersonsAlanIda.add(homePersonIda);
        List<String> medicationsJoey = new ArrayList<>();
        medicationsJoey.add("triclaz:150mg");
        List<String> allergiesJoey = new ArrayList<>();
        allergiesJoey.add("gluten");
        allergiesJoey.add("lactose");
        HomePerson homePersonJoey = new HomePerson("Joey", "ZULO", "001-002-0033",
                "10", medicationsJoey, allergiesJoey);
        List<HomePerson> homePersonsJoey = new ArrayList<>();
        homePersonsJoey.add(homePersonJoey);
        FireStation fireStation1st = new FireStation("1st street", "3");
        FireStation fireStation88th = new FireStation("88th avenue", "3");
        TreeSet<String> addresses = new TreeSet<>();
        addresses.add("1st street");
        addresses.add("88th avenue");
        List<HomeByAddress> homesByAddresses = new ArrayList<>();
        HomeByAddress homeByAddress1st = new HomeByAddress("1st street", homePersonsAlanIda);
        HomeByAddress homeByAddress88th = new HomeByAddress("88th avenue", homePersonsJoey);
        homesByAddresses.add(homeByAddress1st);
        homesByAddresses.add(homeByAddress88th);
        HomesByFireStation homesByFireStationExpected = new HomesByFireStation("3", homesByAddresses);
        when(serviceURL.getHomesByFireStation("3")).thenReturn(homesByFireStationExpected);
        mockMvc.perform(get("/flood/stations?stations={stations}", "3"))
                .andExpect(jsonPath("$.fireStation").value("3"))
                .andExpect(jsonPath("$.homesByAddress").isArray())
                .andExpect(jsonPath("$.homesByAddress", hasSize(2)))
                .andExpect(jsonPath("$.homesByAddress[0].address").value("1st street"))
                .andExpect(jsonPath("$.homesByAddress[0].homePersons[0].firstName").value("Alan"))
                .andExpect(jsonPath("$.homesByAddress[0].homePersons[0].phone")
                        .value("111-222-3333"))
                .andExpect(jsonPath("$.homesByAddress[0].homePersons[0].medications").isArray())
                .andExpect(jsonPath("$.homesByAddress[0].homePersons[0].medications", hasSize(2)))
                .andExpect(jsonPath("$.homesByAddress[0].homePersons[0].medications", hasItem("amol:1g")))
                .andExpect(jsonPath("$.homesByAddress[0].homePersons[0].medications", hasItem("lugan:6mg")))
                .andExpect(jsonPath("$.homesByAddress[0].homePersons[1].lastName").value("ROLA"))
                .andExpect(jsonPath("$.homesByAddress[0].homePersons[1].age").value("21"))
                .andExpect(jsonPath("$.homesByAddress[1].address").value("88th avenue"))
                .andExpect(jsonPath("$.homesByAddress[1].homePersons[0].lastName").value("ZULO"))
                .andExpect(jsonPath("$.homesByAddress[1].homePersons[0].allergies").isArray())
                .andExpect(jsonPath("$.homesByAddress[1].homePersons[0].allergies", hasSize(2)))
                .andExpect(jsonPath("$.homesByAddress[1].homePersons[0].allergies", hasItem("gluten")))
                .andExpect(jsonPath("$.homesByAddress[1].homePersons[0].allergies", hasItem("lactose")))
                .andExpect(status().isOk());
    }

}
