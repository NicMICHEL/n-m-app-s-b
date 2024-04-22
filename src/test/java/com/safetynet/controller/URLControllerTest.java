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

import java.util.*;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ControllerURL.class)
public class ControllerURLTest {

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
        mockMvc.perform(get("/flood/station?station={station}", "3"))
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

    @Test
    public void should_get_home_persons_by_address_successfully() throws Exception {
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
        HomePersonsByAddress homePersonsByAddressExpected = new HomePersonsByAddress("1st street", "3",
                homePersonsAlanIda);
        when(serviceURL.getHomePersonsByAddress("1st street")).thenReturn(homePersonsByAddressExpected);
        mockMvc.perform(get("/fire?address={address}", "1st street"))
                .andExpect(jsonPath("$.fireStation").value("3"))
                .andExpect(jsonPath("$.homePersons").isArray())
                .andExpect(jsonPath("$.homePersons", hasSize(2)))
                .andExpect(jsonPath("$.homePersons[0].firstName").value("Alan"))
                .andExpect(jsonPath("$.homePersons[0].phone")
                        .value("111-222-3333"))
                .andExpect(jsonPath("$.homePersons[0].medications").isArray())
                .andExpect(jsonPath("$.homePersons[0].medications", hasSize(2)))
                .andExpect(jsonPath("$.homePersons[0].medications", hasItem("amol:1g")))
                .andExpect(jsonPath("$.homePersons[0].medications", hasItem("lugan:6mg")))
                .andExpect(jsonPath("$.homePersons[1].lastName").value("ROLA"))
                .andExpect(jsonPath("$.homePersons[1].age").value("21"))
                .andExpect(jsonPath("$.homePersons[1].allergies").isArray())
                .andExpect(jsonPath("$.homePersons[1].allergies", hasSize(2)))
                .andExpect(jsonPath("$.homePersons[1].allergies", hasItem("lacilan")))
                .andExpect(jsonPath("$.homePersons[1].allergies", hasItem("milk")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_persons_by_firestation_successfully() throws Exception {
        TreeSet<String> addresses = new TreeSet<>();
        addresses.add("1st street");
        addresses.add("88th avenue");
        Person personAlan = new Person("Alan", "CRUG", "1st street",
                "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        Person personIda = new Person("Ida", "ROLA", "1st street",
                "Bordeaux", "33000", "011-022-0333", "ida.rola@omail.com");
        Person personJoey = new Person("Joey", "ZULO", "88th avenue",
                "Bayonne", "64000", "001-002-0033", "joey.zulo@ymail.com");
        Map<String, Person> personsByAddressAlanIda = new HashMap<>();
        personsByAddressAlanIda.put("ROLAIda", personIda);
        personsByAddressAlanIda.put("CRUGAlan", personAlan);
        Map<String, Person> personsByAddressJoey = new HashMap<>();
        personsByAddressJoey.put("ZULOJoey", personJoey);
        LastNameFirstNamePhone lastNameFirstNamePhoneAlan = new LastNameFirstNamePhone(
                "CRUG", "Alan", "111-222-3333");
        LastNameFirstNamePhone lastNameFirstNamePhoneIda = new LastNameFirstNamePhone(
                "ROLA", "Ida", "011-022-0333");
        LastNameFirstNamePhone lastNameFirstNamePhoneJoey = new LastNameFirstNamePhone(
                "ZULO", "Joey", "001-002-0033");
        List<LastNameFirstNamePhone> lastNameFirstNamePhonesAlanIda = new ArrayList<>();
        lastNameFirstNamePhonesAlanIda.add(lastNameFirstNamePhoneAlan);
        lastNameFirstNamePhonesAlanIda.add(lastNameFirstNamePhoneIda);
        List<LastNameFirstNamePhone> lastNameFirstNamePhonesJoey = new ArrayList<>();
        lastNameFirstNamePhonesJoey.add(lastNameFirstNamePhoneJoey);
        LastNameFirstNamePhoneByAddress lastNameFirstNamePhoneByAddressAlanIda = new LastNameFirstNamePhoneByAddress(
                "1st street", lastNameFirstNamePhonesAlanIda);
        LastNameFirstNamePhoneByAddress lastNameFirstNamePhoneByAddressJoey = new LastNameFirstNamePhoneByAddress(
                "88th avenue", lastNameFirstNamePhonesJoey);
        List<LastNameFirstNamePhoneByAddress> lastNameFirstNamePhoneByAddresses = new ArrayList<>();
        lastNameFirstNamePhoneByAddresses.add(lastNameFirstNamePhoneByAddressAlanIda);
        lastNameFirstNamePhoneByAddresses.add(lastNameFirstNamePhoneByAddressJoey);
        PersonsByFireStation personsByFireStationExpected = new PersonsByFireStation("3", "1",
                "2", lastNameFirstNamePhoneByAddresses);
        when(serviceURL.getPersonsByFireStation("3")).thenReturn(personsByFireStationExpected);
        mockMvc.perform(get("/firestation?stationNumber={stationNumber}", "3"))
                .andExpect(jsonPath("$.fireStation").value("3"))
                .andExpect(jsonPath("$.childrenCount").value("1"))
                .andExpect(jsonPath("$.adultsCount").value("2"))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses").isArray())
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses", hasSize(2)))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[0].address")
                        .value("1st street"))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[0].lastNameFirstNamePhones")
                        .isArray())
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[0].lastNameFirstNamePhones"
                        , hasSize(2)))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[0].lastNameFirstNamePhones[0]" +
                        ".lastName").value("CRUG"))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[0].lastNameFirstNamePhones[0]" +
                        ".phone").value("111-222-3333"))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[0].lastNameFirstNamePhones[1]" +
                        ".firstName").value("Ida"))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[1].address")
                        .value("88th avenue"))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[1].lastNameFirstNamePhones")
                        .isArray())
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[1].lastNameFirstNamePhones"
                        , hasSize(1)))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[1].lastNameFirstNamePhones[0]" +
                        ".lastName").value("ZULO"))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[1].lastNameFirstNamePhones[0]" +
                        ".phone").value("001-002-0033"))
                .andExpect(jsonPath("$.lastNameFirstNamePhoneByAddresses[1].lastNameFirstNamePhones[0]" +
                        ".firstName").value("Joey"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_children_by_address_successfully() throws Exception {
        Person personJoey = new Person("Joey", "ZULO", "88th avenue",
                "Bayonne", "64000", "001-002-0033", "joey.zulo@ymail.com");
        Person personTess = new Person("Tess", "BERG", "88th avenue",
                "Bayonne", "64000", "000-000-0011", "tess.berg@jmail.com");
        Person personHenry = new Person("Henry", "DERRA", "88th avenue",
                "Bayonne", "64000", "000-000-0022", "henry.derra@omail.com");
        Map<String, Person> personsByAddressJoeyTessHenry = new HashMap<>();
        personsByAddressJoeyTessHenry.put("ZULOJoey", personJoey);
        personsByAddressJoeyTessHenry.put("BERGTess", personTess);
        personsByAddressJoeyTessHenry.put("DERRAHenry", personHenry);
        List<Child> childrenJoeyTess = new ArrayList<>();
        Child childTess = new Child("BERG", "Tess", "4");
        childrenJoeyTess.add(childTess);
        Child childJoey = new Child("ZULO", "Joey", "10");
        childrenJoeyTess.add(childJoey);
        List<LastNameFirstName> adultsHenry = new ArrayList<>();
        LastNameFirstName lastNameFirstNameHenry = new LastNameFirstName("DERRA", "Henry");
        adultsHenry.add(lastNameFirstNameHenry);
        ChildrenByAddress childrenByAddressExpected = new ChildrenByAddress("88th avenue",
                childrenJoeyTess, adultsHenry);
        when(serviceURL.getChildrenByAddress("88th avenue")).thenReturn(childrenByAddressExpected);
        mockMvc.perform(get("/childAlert?address={address}", "88th avenue"))
                .andExpect(jsonPath("$.address").value("88th avenue"))
                .andExpect(jsonPath("$.children").isArray())
                .andExpect(jsonPath("$.children", hasSize(2)))
                .andExpect(jsonPath("$.children[0].lastName").value("BERG"))
                .andExpect(jsonPath("$.children[0].age").value("4"))
                .andExpect(jsonPath("$.children[1].firstName").value("Joey"))
                .andExpect(jsonPath("$.adults").isArray())
                .andExpect(jsonPath("$.adults", hasSize(1)))
                .andExpect(jsonPath("$.adults[0].lastName").value("DERRA"))
                .andExpect(jsonPath("$.adults[0].firstName").value("Henry"))
                .andExpect(status().isOk());
    }

}
