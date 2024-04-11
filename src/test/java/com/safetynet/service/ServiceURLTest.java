package com.safetynet.service;


import com.safetynet.model.*;
import com.safetynet.repository.FireStationRepository;
import com.safetynet.repository.MedicalRecordRepository;
import com.safetynet.repository.NotFoundException;
import com.safetynet.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private FireStationRepository fireStationRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @InjectMocks
    private ServiceURL serviceURL;


    @Test
    public void should_get_phones_by_fireStation_successfully() throws Exception {
        String firestation = "1";
        TreeSet<String> addresses = new TreeSet<>();
        addresses.add("644 Gershwin Cir");
        addresses.add("908 73rd St");
        addresses.add("947 E. Rose Dr");
        TreeSet<String> phones644 = new TreeSet<>();
        phones644.add("841-874-6512");
        TreeSet<String> phones908 = new TreeSet<>();
        phones908.add("841-874-8547");
        phones908.add("841-874-7462");
        TreeSet<String> phones947 = new TreeSet<>();
        phones947.add("841-874-7784");
        Set<String> phonesExpected = new TreeSet<>();
        phonesExpected.add("841-874-6512");
        phonesExpected.add("841-874-7462");
        phonesExpected.add("841-874-7784");
        phonesExpected.add("841-874-8547");
        when(fireStationRepository.getAddressesByFireStation(firestation)).thenReturn(addresses);
        when(personRepository.getPhonesByAddress("644 Gershwin Cir")).thenReturn(phones644);
        when(personRepository.getPhonesByAddress("908 73rd St")).thenReturn(phones908);
        when(personRepository.getPhonesByAddress("947 E. Rose Dr")).thenReturn(phones947);
        Set<String> phones = serviceURL.getPhonesByFireStation(firestation);
        assertEquals(4, phones.size());
        assertEquals(phonesExpected, phones);
    }

    @Test
    public void should_get_infos_by_person_successfully() throws NotFoundException {
        List<String> medications = new ArrayList<>();
        medications.add("amol:1g");
        medications.add("lugan:6mg");
        List<String> allergies = new ArrayList<>();
        allergies.add("nuts");
        allergies.add("fish");
        Person alan = new Person("Alan", "CRUG", "1st street",
                "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        InfosByPerson infosByPersonExpected = new InfosByPerson("Alan", "CRUG",
                "1st street", "44", "alan.crug@jmail.com", medications, allergies);
        when(personRepository.findPersonByFirstNameLastName("Alan", "CRUG")).thenReturn(alan);
        when(medicalRecordRepository.getAgeByLastNameFirstName("Alan", "CRUG")).thenReturn(44);
        when(medicalRecordRepository.getMedicationsByLastNameFirstName("Alan", "CRUG")).thenReturn(medications);
        when(medicalRecordRepository.getAllergiesByLastNameFirstName("Alan", "CRUG")).thenReturn(allergies);
        InfosByPerson infosByPerson = serviceURL.getInfosByPerson("Alan", "CRUG");
        assertEquals(infosByPersonExpected, infosByPerson);
    }

    @Test
    public void should_get_homes_by_fireStation_successfully() throws NotFoundException {
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
        Person personAlan = new Person("Alan", "CRUG", "1st street",
                "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        Person personIda = new Person("Ida", "ROLA", "1st street",
                "Bordeaux", "33000", "011-022-0333", "ida.rola@omail.com");
        Person personJoey = new Person("Joey", "ZULO", "88th avenue",
                "Bayonne", "64000", "001-002-0033", "joey.zulo@ymail.com");
        TreeSet<String> addresses = new TreeSet<>();
        addresses.add("1st street");
        addresses.add("88th avenue");
        List<HomeByAddress> homesByAddresses = new ArrayList<>();
        HomeByAddress homeByAddress1st = new HomeByAddress("1st street", homePersonsAlanIda);
        HomeByAddress homeByAddress88th = new HomeByAddress("88th avenue", homePersonsJoey);
        homesByAddresses.add(homeByAddress1st);
        homesByAddresses.add(homeByAddress88th);
        HomesByFireStation homesByFireStationExpected = new HomesByFireStation("3", homesByAddresses);
        Map<String, Person> personsByAddressAlanIda = new HashMap<>();
        personsByAddressAlanIda.put("ROLAIda", personIda);
        personsByAddressAlanIda.put("CRUGAlan", personAlan);
        Map<String, Person> personsByAddressJoey = new HashMap<>();
        personsByAddressJoey.put("ZULOJoey", personJoey);
        when(fireStationRepository.getAddressesByFireStation("3")).thenReturn(addresses);
        when(personRepository.getPersonsByAddress("1st street")).thenReturn(personsByAddressAlanIda);
        when(personRepository.getPersonsByAddress("88th avenue")).thenReturn(personsByAddressJoey);
        when(medicalRecordRepository.getAgeByLastNameFirstName("Alan", "CRUG")).thenReturn(44);
        when(medicalRecordRepository.getAgeByLastNameFirstName("Ida", "ROLA")).thenReturn(21);
        when(medicalRecordRepository.getAgeByLastNameFirstName("Joey", "ZULO")).thenReturn(10);
        when(medicalRecordRepository.getMedicationsByLastNameFirstName("Alan", "CRUG")).thenReturn(medicationsAlan);
        when(medicalRecordRepository.getMedicationsByLastNameFirstName("Ida", "ROLA")).thenReturn(medicationsIda);
        when(medicalRecordRepository.getMedicationsByLastNameFirstName("Joey", "ZULO")).thenReturn(medicationsJoey);
        when(medicalRecordRepository.getAllergiesByLastNameFirstName("Alan", "CRUG")).thenReturn(allergiesAlan);
        when(medicalRecordRepository.getAllergiesByLastNameFirstName("Ida", "ROLA")).thenReturn(allergiesIda);
        when(medicalRecordRepository.getAllergiesByLastNameFirstName("Joey", "ZULO")).thenReturn(allergiesJoey);
        HomesByFireStation homesByFireStation = serviceURL.getHomesByFireStation("3");
        assertEquals(homesByFireStationExpected, homesByFireStation);
    }

    @Test
    public void should_get_home_by_address_successfully() throws NotFoundException {
        Person personAlan = new Person("Alan", "CRUG", "1st street",
                "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        Person personIda = new Person("Ida", "ROLA", "1st street",
                "Bordeaux", "33000", "011-022-0333", "ida.rola@omail.com");
        Map<String, Person> personsByAddress = new HashMap<>();
        personsByAddress.put("ROLAIda", personIda);
        personsByAddress.put("CRUGAlan", personAlan);
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
        List<HomePerson> homePersons = new ArrayList<>();
        homePersons.add(homePersonAlan);
        homePersons.add(homePersonIda);
        HomeByAddress homeByAddressExpected = new HomeByAddress("1st street", homePersons);
        when(medicalRecordRepository.getAgeByLastNameFirstName("Alan", "CRUG")).thenReturn(44);
        when(medicalRecordRepository.getAgeByLastNameFirstName("Ida", "ROLA")).thenReturn(21);
        when(medicalRecordRepository.getMedicationsByLastNameFirstName("Alan", "CRUG")).thenReturn(medicationsAlan);
        when(medicalRecordRepository.getMedicationsByLastNameFirstName("Ida", "ROLA")).thenReturn(medicationsIda);
        when(medicalRecordRepository.getAllergiesByLastNameFirstName("Alan", "CRUG")).thenReturn(allergiesAlan);
        when(medicalRecordRepository.getAllergiesByLastNameFirstName("Ida", "ROLA")).thenReturn(allergiesIda);
        when(personRepository.getPersonsByAddress("1st street")).thenReturn(personsByAddress);
        HomeByAddress homeByAddress = serviceURL.getHomeByAddress("1st street");
        assertEquals(homeByAddressExpected, homeByAddress);
    }

    @Test
    public void should_get_home_person_successfully() throws NotFoundException {
        Person personAlan = new Person("Alan", "CRUG", "1st street",
                "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        Person personIda = new Person("Ida", "ROLA", "1st street",
                "Bordeaux", "33000", "011-022-0333", "ida.rola@omail.com");
        Map<String, Person> personsByAddress = new HashMap<>();
        personsByAddress.put("ROLAIda", personIda);
        personsByAddress.put("CRUGAlan", personAlan);
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
        List<HomePerson> homePersonsExpected = new ArrayList<>();
        homePersonsExpected.add(homePersonAlan);
        homePersonsExpected.add(homePersonIda);
        when(medicalRecordRepository.getAgeByLastNameFirstName("Alan", "CRUG")).thenReturn(44);
        when(medicalRecordRepository.getAgeByLastNameFirstName("Ida", "ROLA")).thenReturn(21);
        when(medicalRecordRepository.getMedicationsByLastNameFirstName("Alan", "CRUG")).thenReturn(medicationsAlan);
        when(medicalRecordRepository.getMedicationsByLastNameFirstName("Ida", "ROLA")).thenReturn(medicationsIda);
        when(medicalRecordRepository.getAllergiesByLastNameFirstName("Alan", "CRUG")).thenReturn(allergiesAlan);
        when(medicalRecordRepository.getAllergiesByLastNameFirstName("Ida", "ROLA")).thenReturn(allergiesIda);
        List<HomePerson> homePersons = serviceURL.getHomePerson(personsByAddress);
        assertEquals(homePersonsExpected, homePersons);
    }

}
