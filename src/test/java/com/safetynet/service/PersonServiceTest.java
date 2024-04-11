package com.safetynet.service;

import com.safetynet.model.Person;
import com.safetynet.repository.NotFoundException;
import com.safetynet.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    public void should_save_person_successfully() throws NotFoundException {
        Person person = new Person();
        Person savedPerson = new Person();
        when(personRepository.save(person)).thenReturn(savedPerson);
        personService.save(person);
        verify(personRepository).save(person);
    }

    @Test
    public void should_update_person_successfully() throws NotFoundException {
        Person person = new Person();
        Person updatedPerson = new Person();
        when(personRepository.update(person)).thenReturn(updatedPerson);
        personService.update(person);
        verify(personRepository).update(person);
    }

    @Test
    public void should_delete_person_successfully() throws NotFoundException {
        doNothing().when(personRepository).delete("Ali", "BABA");
        personService.delete("Ali", "BABA");
        verify(personRepository).delete("Ali", "BABA");
    }

    @Test
    public void should_get_person_successfully() throws NotFoundException {
        Person person = new Person();
        when(personRepository.findPersonByFirstNameLastName("Ali", "BABA")).thenReturn(person);
        personService.get("Ali", "BABA");
        verify(personRepository).findPersonByFirstNameLastName("Ali", "BABA");
    }

}
