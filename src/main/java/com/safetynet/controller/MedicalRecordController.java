package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.NotFoundException;
import com.safetynet.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping
    public MedicalRecord create(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.save(medicalRecord);
    }

    @PutMapping
    public MedicalRecord update(@RequestBody MedicalRecord medicalRecord) throws NotFoundException {
        return medicalRecordService.update(medicalRecord);
    }

    /**
     * Delete - Delete a medicalRecord
     *
     * @param firstName,
     * @param lastName   - The firstName and lastName of the medicalRecord to delete
     */
    @DeleteMapping("/{firstName}/{lastName}")
    public void delete(@PathVariable String firstName, @PathVariable String lastName) throws NotFoundException {
        medicalRecordService.delete(firstName, lastName);
    }

    /**
     * Get - get a medicalRecord
     *
     * @param firstName,
     * @param lastName   - The firstName and lastName of the medicalRecord to get
     */
    @GetMapping("/{firstName}/{lastName}")
    public MedicalRecord get(@PathVariable String firstName, @PathVariable String lastName) throws NotFoundException {
        return medicalRecordService.get(firstName, lastName);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFoundException(NotFoundException notFoundException) {
        return notFoundException.getMessage();
    }

}
