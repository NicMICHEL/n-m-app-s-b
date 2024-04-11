package com.safetynet.repository;


import com.safetynet.model.MedicalRecord;
import com.safetynet.util.RawData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MedicalRecordRepository {

    public MedicalRecordRepository() {
    }

    private static final Logger logger = LogManager.getLogger(MedicalRecordRepository.class);

    // Convert the data file data.json to a list of medicalRecords
    public static List<MedicalRecord> medicalRecordDeserializer() throws IOException {
        return RawData.rawDataDeserializer().medicalrecords;
    }

    private static List<MedicalRecord> medicalRecords;

    static {
        try {
            medicalRecords = medicalRecordDeserializer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, MedicalRecord> indexMedicalRecordsByLastNameFirstName(List<MedicalRecord> medicalRecords) {
        Map<String, MedicalRecord> medicalRecordsByLastNameFirstName = new HashMap<>();
        for (MedicalRecord medicalRecord : medicalRecords) {
            medicalRecordsByLastNameFirstName.put(medicalRecord.lastName() + medicalRecord.firstName(), medicalRecord);
        }
        return medicalRecordsByLastNameFirstName;
    }

    public static Map<String, MedicalRecord> medicalRecordsByLastNameFirstName
            = indexMedicalRecordsByLastNameFirstName(medicalRecords);

    public MedicalRecord save(MedicalRecord medicalRecord) {
        logger.debug("Creating new medicalRecord {}", medicalRecord);
        medicalRecordsByLastNameFirstName.put(medicalRecord.lastName() + medicalRecord.firstName(), medicalRecord);
        return medicalRecordsByLastNameFirstName.get(medicalRecord.lastName() + medicalRecord.firstName());
    }

    public MedicalRecord update(MedicalRecord medicalRecord) throws NotFoundException {
        if (medicalRecordsByLastNameFirstName.get(medicalRecord.lastName() + medicalRecord.firstName()) != null) {
            logger.debug("Updating existing medicalRecord {}", medicalRecord);
            medicalRecordsByLastNameFirstName.put(medicalRecord.lastName() + medicalRecord.firstName(), medicalRecord);
            return medicalRecordsByLastNameFirstName.get(medicalRecord.lastName() + medicalRecord.firstName());
        } else {
            logger.error("Unable to find and update medicalRecord corresponding to LastName {} and FirstName {}",
                    medicalRecord.lastName(), medicalRecord.firstName());
            throw new NotFoundException(String.format("Unable to find and update medicalRecord corresponding" +
                    " to LastName %s and FirstName %s", medicalRecord.lastName(), medicalRecord.firstName()));
        }
    }

    public void delete(String firstName, String lastName) throws NotFoundException {
        if (medicalRecordsByLastNameFirstName.get(lastName + firstName) != null) {
            logger.debug("Deleting medicalRecord corresponding to firstName : {} and lastName : {}",
                    firstName, lastName);
            medicalRecordsByLastNameFirstName.remove(lastName + firstName);
        } else {
            logger.error("Unable to find and delete medicalRecord corresponding to LastName {} and FirstName {}",
                    lastName, firstName);
            throw new NotFoundException(String.format("Unable to find and delete medicalRecord corresponding" +
                    " to LastName %s and FirstName %s", lastName, firstName));
        }
    }

    public MedicalRecord findMedicalRecordByFirstNameLastName(String firstName, String lastName)
            throws NotFoundException {
        if (medicalRecordsByLastNameFirstName.get(lastName + firstName) != null) {
            logger.debug("Getting medicalRecord corresponding to firstName : {} and lastName : {}",
                    firstName, lastName);
            return medicalRecordsByLastNameFirstName.get(lastName + firstName);
        } else {
            logger.error("Unable to find medicalRecord corresponding to LastName {} and FirstName {}",
                    lastName, firstName);
            throw new NotFoundException(String.format("Unable to find medicalRecord corresponding" +
                    " to LastName %s and FirstName %s", lastName, firstName));
        }
    }

    public int getAgeByLastNameFirstName(String firstName, String lastName) throws NotFoundException {
        if (medicalRecordsByLastNameFirstName.get(lastName + firstName) != null) {
            logger.debug("Getting age corresponding to firstName : {} and lastName : {}", firstName, lastName);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate = LocalDate.parse(medicalRecordsByLastNameFirstName.get(lastName + firstName).birthdate(),
                    formatter);
            return LocalDate.now().getYear() - birthDate.getYear();
        } else {
            logger.error("Person corresponding to LastName {} and FirstName {} not found in database",
                    lastName, firstName);
            throw new NotFoundException(String.format("Person corresponding" +
                    " to LastName %s and FirstName %s not found in database", lastName, firstName));
        }
    }

    public List<String> getMedicationsByLastNameFirstName(String firstName, String lastName) throws NotFoundException {
        if (medicalRecordsByLastNameFirstName.get(lastName + firstName) != null) {
            logger.debug("Getting Medications corresponding to firstName : {} and lastName : {}", firstName,
                    lastName);
            return medicalRecordsByLastNameFirstName.get(lastName + firstName).medications();
        } else {
            logger.error("Person corresponding to LastName {} and FirstName {} not found in database",
                    lastName, firstName);
            throw new NotFoundException(String.format("Person corresponding" +
                    " to LastName %s and FirstName %s not found in database", lastName, firstName));
        }
    }

    public List<String> getAllergiesByLastNameFirstName(String firstName, String lastName) throws NotFoundException {
        if (medicalRecordsByLastNameFirstName.get(lastName + firstName) != null) {
            logger.debug("Getting Allergies corresponding to firstName : {} and lastName : {}", firstName,
                    lastName);
            return medicalRecordsByLastNameFirstName.get(lastName + firstName).allergies();
        } else {
            logger.error("Person corresponding to LastName {} and FirstName {} not found in database",
                    lastName, firstName);
            throw new NotFoundException(String.format("Person corresponding" +
                    " to LastName %s and FirstName %s not found in database", lastName, firstName));
        }
    }

}
