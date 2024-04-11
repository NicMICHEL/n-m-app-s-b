package com.safetynet.repository;

import com.safetynet.model.MedicalRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordRepositoryTest {

    private MedicalRecordRepository medicalRecordRepository;

    private List<MedicalRecord> medicalRecordsForTest = new ArrayList<>();

    private Map<String, MedicalRecord> medicalRecordsByLastNameFirstNameForTest = new HashMap<>();


    @BeforeEach
    public void setUpPerTest() {
        medicalRecordRepository = new MedicalRecordRepository();
        List<String> medicationsAlan = new ArrayList<>();
        medicationsAlan.add("amol:1g");
        medicationsAlan.add("lugan:6mg");
        List<String> allergiesAlan = new ArrayList<>();
        allergiesAlan.add("nuts");
        allergiesAlan.add("fish");
        MedicalRecord medicalRecordAlan = new MedicalRecord("Alan", "CRUG", "03/01/1980",
                medicationsAlan, allergiesAlan);
        List<String> medicationsIda = new ArrayList<>();
        medicationsIda.add("cyclaz:50mg");
        medicationsIda.add("xadin:3mg");
        List<String> allergiesIda = new ArrayList<>();
        allergiesIda.add("lacilan");
        allergiesIda.add("milk");
        MedicalRecord medicalRecordIda = new MedicalRecord("Ida", "ROLA", "07/11/2003",
                medicationsIda, allergiesIda);
        List<String> medicationsJoey = new ArrayList<>();
        medicationsJoey.add("triclaz:150mg");
        List<String> allergiesJoey = new ArrayList<>();
        allergiesJoey.add("gluten");
        allergiesJoey.add("lactose");
        MedicalRecord medicalRecordJoey = new MedicalRecord("Joey", "ZULO", "06/05/2014",
                medicationsJoey, allergiesJoey);
        medicalRecordsForTest.add(medicalRecordAlan);
        medicalRecordsForTest.add(medicalRecordIda);
        medicalRecordsForTest.add(medicalRecordJoey);
        for (MedicalRecord medicalRecord : medicalRecordsForTest) {
            medicalRecordsByLastNameFirstNameForTest
                    .put(medicalRecord.lastName() + medicalRecord.firstName(), medicalRecord);
        }
        MedicalRecordRepository.medicalRecordsByLastNameFirstName = medicalRecordsByLastNameFirstNameForTest;
    }

    @AfterEach
    public void tearDown() {
        MedicalRecordRepository.medicalRecordsByLastNameFirstName = null;
    }

    @Test
    public void should_convert_the_medicalRecords_list_into_a_map_according_to_lastName_concatenated_with_firstName() {
        // when
        List<String> medicationsIda = new ArrayList<>();
        medicationsIda.add("cyclaz:50mg");
        medicationsIda.add("xadin:3mg");
        List<String> allergiesJoey = new ArrayList<>();
        allergiesJoey.add("gluten");
        allergiesJoey.add("lactose");
        Map<String, MedicalRecord> results =
                MedicalRecordRepository.indexMedicalRecordsByLastNameFirstName(medicalRecordsForTest);
        // Then
        assertEquals(3, results.size());
        assertEquals(results.get("CRUGAlan").firstName(), "Alan");
        assertEquals(results.get("CRUGAlan").lastName(), "CRUG");
        assertEquals(results.get("ROLAIda").birthdate(), "07/11/2003");
        assertEquals(results.get("ROLAIda").medications(), medicationsIda);
        assertEquals(results.get("ZULOJoey").allergies(), allergiesJoey);
    }

    @Test
    public void should_save_medicalRecord_successfully() throws IOException {
        // given
        List<String> medicationsBert = new ArrayList<>();
        medicationsBert.add("peran:100g");
        medicationsBert.add("legiz:2mg");
        List<String> allergiesBert = new ArrayList<>();
        allergiesBert.add("hazelnuts");
        allergiesBert.add("lobster");
        MedicalRecord medicalRecordBert = new MedicalRecord("Bert", "STERN", "01/01/2000",
                medicationsBert, allergiesBert);
        MedicalRecord preExistingMedicalRecordAtTheseLasTNameFirstName
                = MedicalRecordRepository.medicalRecordsByLastNameFirstName.get("STERNBert");
        // when
        medicalRecordRepository.save(medicalRecordBert);
        // then
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("STERNBert").firstName(), "Bert");
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("STERNBert").lastName(), "STERN");
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("STERNBert").birthdate(), "01/01/2000");
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("STERNBert").medications(), medicationsBert);
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("STERNBert").allergies(), allergiesBert);
        assertNull(preExistingMedicalRecordAtTheseLasTNameFirstName);
    }

    @Test
    public void should_update_medicalRecord_successfully() throws NotFoundException {
        // given
        List<String> medicationsAlan = new ArrayList<>();
        medicationsAlan.add("amol:30g");
        List<String> allergiesAlan = new ArrayList<>();
        allergiesAlan.add("oat");
        allergiesAlan.add("jellyfish");
        MedicalRecord medicalRecordAlanModified = new MedicalRecord("Alan", "CRUG",
                "03/01/1980", medicationsAlan, allergiesAlan);
        // when
        medicalRecordRepository.update(medicalRecordAlanModified);
        // then
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("CRUGAlan").firstName(), "Alan");
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("CRUGAlan").lastName(), "CRUG");
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("CRUGAlan").medications(), medicationsAlan);
        assertEquals(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("CRUGAlan").allergies(), allergiesAlan);
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastname_corresponding_to_medicalRecord_to_update_is_not_found() {
        // given
        List<String> medicationsLola = new ArrayList<>();
        List<String> allergiesLola = new ArrayList<>();
        MedicalRecord medicalRecordLola = new MedicalRecord("Lola", "MIGE", "05/02/1977",
                medicationsLola, allergiesLola);
        MedicalRecord preExistingMedicalRecordAtTheseLasTNameFirstName
                = MedicalRecordRepository.medicalRecordsByLastNameFirstName.get("MIGELola");
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    medicalRecordRepository.update(medicalRecordLola);
                }, "NotFoundException was expected");
        //then
        assertEquals("Unable to find and update medicalRecord corresponding to LastName MIGE " +
                "and FirstName Lola", thrown.getMessage());
        assertNull(preExistingMedicalRecordAtTheseLasTNameFirstName);
    }

    @Test
    public void should_delete_medicalRecord_successfully() throws NotFoundException {
        // given
        MedicalRecord preExistingMedicalRecordAtTheseLasTNameFirstName =
                MedicalRecordRepository.medicalRecordsByLastNameFirstName.get("CRUGAlan");
        //when
        medicalRecordRepository.delete("Alan", "CRUG");
        //then
        assertNotNull(preExistingMedicalRecordAtTheseLasTNameFirstName);
        assertNull(MedicalRecordRepository.medicalRecordsByLastNameFirstName.get("CRUGAlan"));
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastname_corresponding_to_medicalRecord_to_delete_is_not_found() {
        // given
        MedicalRecord preExistingMedicalRecordAtTheseLasTNameFirstName
                = MedicalRecordRepository.medicalRecordsByLastNameFirstName.get("MIGELola");
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    medicalRecordRepository.delete("Lola", "MIGE");
                }, "NotFoundException was expected");
        //then
        assertEquals("Unable to find and delete medicalRecord corresponding to LastName MIGE" +
                " and FirstName Lola", thrown.getMessage());
        assertNull(preExistingMedicalRecordAtTheseLasTNameFirstName);
    }

    @Test
    public void should_find_medicalRecord_successfully() throws NotFoundException {
        //given
        List<String> medicationsAlan = new ArrayList<>();
        medicationsAlan.add("amol:1g");
        medicationsAlan.add("lugan:6mg");
        List<String> allergiesAlan = new ArrayList<>();
        allergiesAlan.add("nuts");
        allergiesAlan.add("fish");
        //when then
        assertEquals(medicalRecordRepository.findMedicalRecordByFirstNameLastName("Alan", "CRUG")
                .firstName(), "Alan");
        assertEquals(medicalRecordRepository.findMedicalRecordByFirstNameLastName("Alan", "CRUG")
                .lastName(), "CRUG");
        assertEquals(medicalRecordRepository.findMedicalRecordByFirstNameLastName("Alan", "CRUG")
                .birthdate(), "03/01/1980");
        assertEquals(medicalRecordRepository.findMedicalRecordByFirstNameLastName("Alan", "CRUG")
                .medications(), medicationsAlan);
        assertEquals(medicalRecordRepository.findMedicalRecordByFirstNameLastName("Alan", "CRUG")
                .allergies(), allergiesAlan);
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastname_corresponding_to_medicalRecord_to_find_is_not_found() {
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    medicalRecordRepository.findMedicalRecordByFirstNameLastName("Lola", "MIGE");
                }, "NotFoundException was expected");
        //then
        assertEquals("Unable to find medicalRecord corresponding to LastName MIGE and FirstName Lola",
                thrown.getMessage());
    }

    @Test
    public void should_get_age_successfully() throws NotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDateAlan = LocalDate.parse(MedicalRecordRepository.medicalRecordsByLastNameFirstName
                .get("CRUG" + "Alan").birthdate(), formatter);
        int ageAlan = LocalDate.now().getYear() - birthDateAlan.getYear();
        assertEquals(medicalRecordRepository.getAgeByLastNameFirstName("Alan", "CRUG"), ageAlan);
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastname_corresponding_to_age_to_get_is_not_found() {
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    medicalRecordRepository.getAgeByLastNameFirstName("Lola", "MIGE");
                }, "NotFoundException was expected");
        //then
        assertEquals("Person corresponding to LastName MIGE " +
                "and FirstName Lola not found in database", thrown.getMessage());
    }

    @Test
    public void should_get_medications_successfully() throws NotFoundException {
        //given
        List<String> medicationsAlan = new ArrayList<>();
        medicationsAlan.add("amol:1g");
        medicationsAlan.add("lugan:6mg");
        //when then
        assertEquals(medicalRecordRepository.getMedicationsByLastNameFirstName("Alan", "CRUG"), medicationsAlan);
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastname_corresponding_to_medications_to_get_is_not_found() {
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    medicalRecordRepository.getMedicationsByLastNameFirstName("Lola", "MIGE");
                }, "NotFoundException was expected");
        //then
        assertEquals("Person corresponding to LastName MIGE " +
                "and FirstName Lola not found in database", thrown.getMessage());
    }

    @Test
    public void should_get_allergies_successfully() throws NotFoundException {
        //given
        List<String> allergiesAlan = new ArrayList<>();
        allergiesAlan.add("nuts");
        allergiesAlan.add("fish");
        //when then
        assertEquals(medicalRecordRepository.getAllergiesByLastNameFirstName("Alan", "CRUG"), allergiesAlan);
    }

    @Test
    public void should_throw_an_exception_when_firstName_or_lastname_corresponding_to_allergies_to_get_is_not_found() {
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    medicalRecordRepository.getAllergiesByLastNameFirstName("Lola", "MIGE");
                }, "NotFoundException was expected");
        //then
        assertEquals("Person corresponding to LastName MIGE " +
                "and FirstName Lola not found in database", thrown.getMessage());
    }

}
