package com.safetynet.controller;

import com.safetynet.model.FireStation;
import com.safetynet.repository.NotFoundException;
import com.safetynet.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fireStation")
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    @PostMapping
    public FireStation create(@RequestBody FireStation fireStation) {
        return fireStationService.save(fireStation);
    }

    @PutMapping
    public FireStation update(@RequestBody FireStation fireStation) throws NotFoundException {
        return fireStationService.update(fireStation);
    }

    /**
     * Delete - Delete a fireStation
     *
     * @param address - The address of the fireStation to delete
     */
    @DeleteMapping("/{address}")
    public void delete(@PathVariable("address") final String address) throws NotFoundException {
        fireStationService.delete(address);
    }

    /**
     * Get - Get a fireStation
     *
     * @param address - The address of the fireStation to find
     */
    @GetMapping("/{address}")
    public FireStation get(@PathVariable("address") final String address) throws NotFoundException {
        return fireStationService.get(address);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFoundException(NotFoundException notFoundException) {
        return notFoundException.getMessage();
    }

}
