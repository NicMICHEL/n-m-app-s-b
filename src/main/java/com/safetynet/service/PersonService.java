package com.safetynet.service;

import com.safetynet.model.Person;
import com.safetynet.repository.NotFoundException;
import com.safetynet.repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person save(Person person) {
        Person savedPerson = personRepository.save(person);
        return savedPerson;
    }

    public Person update(Person person) throws NotFoundException {
        Person updatedPerson = personRepository.update(person);
        return updatedPerson;
    }

    public void delete(String firstName, String lastName) throws NotFoundException {
        personRepository.delete(firstName, lastName);
    }

    public Person get(String firstName, String lastName) throws NotFoundException {
        return personRepository.findPersonByFirstNameLastName(firstName, lastName);
    }

}
