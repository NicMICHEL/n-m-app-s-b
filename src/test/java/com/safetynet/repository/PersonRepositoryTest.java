package com.safetynet.repository;


import com.safetynet.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PersonRepositoryTest {

    private PersonRepository personRepository;

    private List<Person> personsForTest = new ArrayList<>();

    private Map<String, Person> personsByLastNameFirstNameForTest = new HashMap<>();


    @BeforeEach
    public void setUpPerTest() {
        personRepository = new PersonRepository();
        Person personAlan = new Person("Alan", "CRUG",
                "1st street", "Dax", "40100", "111-222-3333", "alan.crug@jmail.com");
        Person personIda = new Person("Ida", "ROLA", "42e avenue",
                "Blagnac", "31600", "011-022-0333", "ida.rola@omail.com");
        Person personJoey = new Person("Joey", "ZULO", "8 blue street",
                "Bayonne", "64000", "001-002-0033", "joey.zulo@ymail.com");
        Person personTess = new Person("Tess", "BERG", "1st street",
                "Dax", "40100", "000-000-0011", "tess.berg@jmail.com");
        personsForTest.add(personAlan);
        personsForTest.add(personIda);
        personsForTest.add(personJoey);
        personsForTest.add(personTess);
        for (Person person : personsForTest) {
            personsByLastNameFirstNameForTest.put(person.lastName() + person.firstName(), person);
        }
        PersonRepository.personsByLastNameFirstName = personsByLastNameFirstNameForTest;
    }

    @AfterEach
    public void tearDown() {
        PersonRepository.personsByLastNameFirstName = null;
    }

    @Test
    public void should_convert_the_persons_list_into_a_map_according_to_lastName_concatenated_with_firstName() {
        // when
        Map<String, Person> results = PersonRepository.indexPersonsByLastNameFirstName(personsForTest);
        // Then
        assertEquals(4, results.size());
        assertEquals(results.get("CRUGAlan").firstName(), "Alan");
        assertEquals(results.get("CRUGAlan").lastName(), "CRUG");
        assertEquals(results.get("CRUGAlan").address(), "1st street");
        assertEquals(results.get("CRUGAlan").city(), "Dax");
        assertEquals(results.get("ROLAIda").zip(), "31600");
        assertEquals(results.get("ZULOJoey").phone(), "001-002-0033");
        assertEquals(results.get("BERGTess").email(), "tess.berg@jmail.com");
    }

    @Test
    public void should_save_person_successfully() throws IOException {
        // given
        Person personTest = new Person("Bert", "STERN",
                "9th red street", "Albi", "81000", "000-000-0022", "bert.stern@dmail.com");
        Person preExistingPersonAtTheseLasTNameFirstName = PersonRepository.personsByLastNameFirstName.get("STERNBert");
        // when
        personRepository.save(personTest);
        // then
        assertEquals(PersonRepository.personsByLastNameFirstName.get("STERNBert").firstName(), "Bert");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("STERNBert").lastName(), "STERN");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("STERNBert").address(), "9th red street");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("STERNBert").city(), "Albi");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("STERNBert").zip(), "81000");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("STERNBert").phone(), "000-000-0022");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("STERNBert").email(), "bert.stern@dmail.com");
        assertNull(preExistingPersonAtTheseLasTNameFirstName);
    }

    @Test
    public void should_update_person_successfully() throws NotFoundException {
        // given
        Person personTest = new Person("Alan", "CRUG",
                "111 square", "Lons", "40999", "111-111-1111", "alan.crug@Mmail.com");
        // when
        personRepository.update(personTest);
        // then
        assertEquals(PersonRepository.personsByLastNameFirstName.get("CRUGAlan").firstName(), "Alan");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("CRUGAlan").lastName(), "CRUG");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("CRUGAlan").address(), "111 square");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("CRUGAlan").city(), "Lons");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("CRUGAlan").zip(), "40999");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("CRUGAlan").phone(), "111-111-1111");
        assertEquals(PersonRepository.personsByLastNameFirstName.get("CRUGAlan").email(), "alan.crug@Mmail.com");
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_Lastname_corresponding_to_person_to_update_is_not_found() {
        // given
        Person personTest = new Person("Lola", "MIGE",
                "10th road", "Mees", "40111", "000-000-0001", "lola.mige@Imail.com");
        Person preExistingPersonAtTheseLasTNameFirstName = PersonRepository.personsByLastNameFirstName.get("MIGELola");
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            personRepository.update(personTest);
        }, "NotFoundException was expected");
        //then
        assertEquals("Unable to find and update person corresponding to LastName MIGE and FirstName Lola",
                thrown.getMessage());
        assertNull(preExistingPersonAtTheseLasTNameFirstName);
    }

    @Test
    public void should_delete_person_successfully() throws NotFoundException {
        // given
        Person preExistingPersonAtTheseLasTNameFirstName = PersonRepository.personsByLastNameFirstName.get("CRUGAlan");
        //when
        personRepository.delete("Alan", "CRUG");
        //then
        assertNotNull(preExistingPersonAtTheseLasTNameFirstName);
        assertNull(PersonRepository.personsByLastNameFirstName.get("CRUGAlan"));
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_Lastname_corresponding_to_person_to_delete_is_not_found() {
        //given
        Person preExistingPersonAtTheseLasTNameFirstName = PersonRepository.personsByLastNameFirstName.get("MIGELola");
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            personRepository.delete("Lola", "MIGE");
        }, "NotFoundException was expected");
        //then
        assertEquals("Unable to find and delete person corresponding to LastName MIGE and FirstName Lola",
                thrown.getMessage());
        assertNull(preExistingPersonAtTheseLasTNameFirstName);
    }

    @Test
    public void should_find_person_successfully() throws NotFoundException {
        assertEquals(personRepository.findPersonByFirstNameLastName("Alan", "CRUG").firstName(), "Alan");
        assertEquals(personRepository.findPersonByFirstNameLastName("Alan", "CRUG").lastName(), "CRUG");
        assertEquals(personRepository.findPersonByFirstNameLastName("Alan", "CRUG").address(), "1st street");
        assertEquals(personRepository.findPersonByFirstNameLastName("Alan", "CRUG").city(), "Dax");
        assertEquals(personRepository.findPersonByFirstNameLastName("Alan", "CRUG").zip(), "40100");
        assertEquals(personRepository.findPersonByFirstNameLastName("Alan", "CRUG").phone(), "111-222-3333");
        assertEquals(personRepository.findPersonByFirstNameLastName("Alan", "CRUG").email(), "alan.crug@jmail.com");
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_Lastname_corresponding_to_person_to_find_is_not_found() {
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    personRepository.findPersonByFirstNameLastName("Lola", "MIGE");
                }, "NotFoundException was expected");
        //then
        assertEquals("Person corresponding to LastName MIGE and FirstName Lola not found in database",
                thrown.getMessage());
    }


    @Test
    public void should_get_emails_by_city_successfully() throws NotFoundException {
        //when
        TreeSet<String> eMails = new TreeSet<>();
        eMails.add("alan.crug@jmail.com");
        eMails.add("tess.berg@jmail.com");
        //then
        assertEquals(personRepository.getEmailsByCity("Dax"), eMails);
    }

    @Test
    public void should_throw_an_exception_when_city_corresponding_to_emails_to_get_is_not_found() {
        //given
        boolean cityInDataBase = false;
        for (Map.Entry<String, Person> entry : PersonRepository.personsByLastNameFirstName.entrySet()) {
            if (entry.getValue().city().equals("CityTest")) {
                cityInDataBase = true;
            }
        }
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            personRepository.getEmailsByCity("CityTest");
        }, "NotFoundException was expected");
        //then
        assertEquals("City : CityTest not found in database", thrown.getMessage());
        assertFalse(cityInDataBase);
    }

    @Test
    public void should_get_phones_by_address_successfully() throws NotFoundException {
        //when
        TreeSet<String> phones = new TreeSet<>();
        phones.add("111-222-3333");
        phones.add("000-000-0011");
        //then
        assertEquals(personRepository.getPhonesByAddress("1st street"), phones);
    }

    @Test
    public void should_throw_an_exception_when_address_corresponding_to_phones_to_get_is_not_found() {
        //given
        boolean addressInDataBase = false;
        for (Map.Entry<String, Person> entry : PersonRepository.personsByLastNameFirstName.entrySet()) {
            if (entry.getValue().address().equals("addressTest")) {
                addressInDataBase = true;
            }
        }
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            personRepository.getPhonesByAddress("addressTest");
        }, "NotFoundException was expected");
        //then
        assertEquals("Address : addressTest not found in database", thrown.getMessage());
        assertFalse(addressInDataBase);
    }

    @Test
    public void should_get_persons_by_address_successfully() throws NotFoundException {
        //when
        Map<String, Person> personsByAddress = new HashMap<>();
        personsByAddress.put("CRUGAlan", new Person("Alan", "CRUG",
                "1st street", "Dax", "40100", "111-222-3333", "alan.crug@jmail.com"));
        personsByAddress.put("BERGTess", new Person("Tess", "BERG", "1st street",
                "Dax", "40100", "000-000-0011", "tess.berg@jmail.com"));
        //then
        assertEquals(personRepository.getPersonsByAddress("1st street"), personsByAddress);
    }

    @Test
    public void should_throw_an_exception_when_address_corresponding_to_persons_to_get_is_not_found() {
        //given
        boolean addressInDataBase = false;
        for (Map.Entry<String, Person> entry : PersonRepository.personsByLastNameFirstName.entrySet()) {
            if (entry.getValue().address().equals("addressTest")) {
                addressInDataBase = true;
            }
        }
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            personRepository.getPersonsByAddress("addressTest");
        }, "NotFoundException was expected");
        //then
        assertEquals("Address : addressTest not found in database", thrown.getMessage());
        assertFalse(addressInDataBase);
    }

}
