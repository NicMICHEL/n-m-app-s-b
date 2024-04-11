package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.repository.NotFoundException;
import com.safetynet.service.MedicalRecordService;
import com.safetynet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    public Person create(@RequestBody Person person) {
        return personService.save(person);
    }

    @PutMapping
    public Person update(@RequestBody Person person) throws NotFoundException {
        return personService.update(person);
    }

    /**
     * Delete - Delete a person
     * @param firstName,
     * @param lastName   - The firstName and lastName of the person to delete
     */
    @DeleteMapping("/{firstName}/{lastName}")
    public void delete(@PathVariable String firstName, @PathVariable String lastName) throws NotFoundException {
        personService.delete(firstName, lastName);
    }

    /**
     * Get - Get a person
     * @param firstName,
     * @param lastName   - The firstName and lastName of the person to get
     */
    @GetMapping("/{firstName}/{lastName}")
    public Person get(@PathVariable String firstName, @PathVariable String lastName) throws NotFoundException {
        return personService.get(firstName, lastName);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFoundException(NotFoundException notFoundException) {
        return notFoundException.getMessage();
    }

}
