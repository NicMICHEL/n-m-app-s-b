package com.safetynet;

import com.safetynet.repository.FireStationRepository;
import com.safetynet.repository.MedicalRecordRepository;
import com.safetynet.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NMAppSBApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NMAppSBApplication.class, args);
    }

    // When launching the application, convert the data file data.json to :
    //  - a list of fire stations
    //  - a list of persons
    //  - a list of medicalRecords
    @Override
    public void run(String... args) throws Exception {
        FireStationRepository.fireStationDeserializer();
        PersonRepository.personDeserializer();
        MedicalRecordRepository.medicalRecordDeserializer();
    }

    @Bean
    public InMemoryHttpExchangeRepository createTraceRepository() {
        return new InMemoryHttpExchangeRepository();
    }
}
