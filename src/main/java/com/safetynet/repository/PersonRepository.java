package com.safetynet.repository;

import com.safetynet.model.Person;
import com.safetynet.util.RawData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Repository
public class PersonRepository {

    public PersonRepository() {
    }

    private static final Logger logger = LogManager.getLogger(PersonRepository.class);

    // Convert the data file data.json to a list of fire persons
    public static List<Person> personDeserializer() throws IOException {
        return RawData.rawDataDeserializer().persons;
    }

    private static List<Person> persons;

    static {
        try {
            persons = personDeserializer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Person> indexPersonsByLastNameFirstName(List<Person> persons) {
        Map<String, Person> personsByLastNameFirstName = new HashMap<>();
        for (Person person : persons) {
            personsByLastNameFirstName.put(person.lastName() + person.firstName(), person);
        }
        return personsByLastNameFirstName;
    }

    public static Map<String, Person> personsByLastNameFirstName = indexPersonsByLastNameFirstName(persons);

    public Person save(Person person) {
        logger.debug("Creating new person {}", person);
        personsByLastNameFirstName.put(person.lastName() + person.firstName(), person);
        return personsByLastNameFirstName.get(person.lastName() + person.firstName());
    }

    public Person update(Person person) throws NotFoundException {
        if (personsByLastNameFirstName.get(person.lastName() + person.firstName()) != null) {
            logger.debug("Updating existing person {}", person);
            personsByLastNameFirstName.put(person.lastName() + person.firstName(), person);
            return personsByLastNameFirstName.get(person.lastName() + person.firstName());
        } else {
            logger.error("Unable to find and update person corresponding to LastName {} and FirstName {}",
                    person.lastName(), person.firstName());
            throw new NotFoundException(String.format("Unable to find and update person corresponding" +
                    " to LastName %s and FirstName %s", person.lastName(), person.firstName()));
        }
    }

    public void delete(String firstName, String lastName) throws NotFoundException {
        if (personsByLastNameFirstName.get(lastName + firstName) != null) {
            logger.debug("Deleting person corresponding to firstName : {} and lastName : {}"
                    , firstName, lastName);
            personsByLastNameFirstName.remove(lastName + firstName);
        } else {
            logger.error("Unable to find and delete person corresponding to LastName {} and FirstName {}",
                    lastName, firstName);
            throw new NotFoundException(String.format("Unable to find and delete person corresponding" +
                    " to LastName %s and FirstName %s", lastName, firstName));
        }
    }

    public Person findPersonByFirstNameLastName(String firstName, String lastName) throws NotFoundException {
        if (personsByLastNameFirstName.get(lastName + firstName) != null) {
            logger.debug("Getting person corresponding to firstName : {} and lastName : {}"
                    , firstName, lastName);
            return personsByLastNameFirstName.get(lastName + firstName);
        } else {
            logger.error("Person corresponding to LastName {} and FirstName {} not found in database",
                    lastName, firstName);
            throw new NotFoundException(String.format("Person corresponding" +
                    " to LastName %s and FirstName %s not found in database", lastName, firstName));
        }
    }

    public TreeSet<String> getEmailsByCity(String city) throws NotFoundException {
        logger.debug("Getting emails corresponding to city : {}", city);
        Boolean cityInDataBase = false;
        TreeSet<String> eMails = new TreeSet<>();
        for (Map.Entry<String, Person> entry : personsByLastNameFirstName.entrySet()) {
            if (entry.getValue().city().equals(city)) {
                cityInDataBase = true;
                eMails.add(entry.getValue().email());
            }
        }
        if (cityInDataBase) {
            logger.info("Emails corresponding to city : {} :", city);
            for (String email : eMails) {
                logger.info(email);
            }
        } else {
            logger.error("City : {} not found in database", city);
            throw new NotFoundException(String.format("City : %s not found in database", city));
        }
        return eMails;
    }

    public TreeSet<String> getPhonesByAddress(String address) throws NotFoundException {
        logger.debug("Getting phones corresponding to address : {}", address);
        Boolean addressInDataBase = false;
        TreeSet<String> phones = new TreeSet<>();
        for (Map.Entry<String, Person> entry : personsByLastNameFirstName.entrySet()) {
            if (entry.getValue().address().equals(address)) {
                addressInDataBase = true;
                phones.add(entry.getValue().phone());
            }
        }
        if (!addressInDataBase) {
            logger.error("Address : {} not found in database", address);
            throw new NotFoundException(String.format("Address : %s not found in database", address));
        }
        return phones;
    }

    public Map<String, Person> getPersonsByAddress(String address) throws NotFoundException {
        logger.debug("Getting persons corresponding to address : {}", address);
        Boolean addressInDataBase = false;
        Map<String, Person> personsByAddress = new HashMap<>();

        for (Map.Entry<String, Person> entry : personsByLastNameFirstName.entrySet()) {
            if (entry.getValue().address().equals(address)) {
                addressInDataBase = true;
                personsByAddress.put(entry.getValue().lastName() + entry.getValue().firstName(),
                        entry.getValue());
            }
        }
        if (!addressInDataBase) {
            logger.error("Address : {} not found in database", address);
            throw new NotFoundException(String.format("Address : %s not found in database", address));
        }
        return personsByAddress;
    }

}
