package com.safetynet.controller;

import com.safetynet.model.HomesByFireStation;
import com.safetynet.model.InfosByPerson;
import com.safetynet.repository.NotFoundException;
import com.safetynet.service.ServiceURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.TreeSet;

@RestController
public class Controller {

    @Autowired
    private ServiceURL service;

    @RequestMapping("/communityEmail")
    public TreeSet<String> getEmailsByCity(@RequestParam String city) throws NotFoundException {
        return service.getEmailsByCity(city);
    }

    @RequestMapping("/phoneAlert")
    public TreeSet<String> getPhonesByFireStation(@RequestParam String firestation) throws NotFoundException {
        return service.getPhonesByFireStation(firestation);
    }

    @RequestMapping("/personInfo")
    public InfosByPerson getInfosByPerson(@RequestParam String firstName, @RequestParam String lastName)
            throws NotFoundException {
        return service.getInfosByPerson(firstName, lastName);
    }

    @RequestMapping("/flood/stations")
    public HomesByFireStation getHomesByFireStation(@RequestParam String stations) throws NotFoundException {
        return service.getHomesByFireStation(stations);
    }

}
