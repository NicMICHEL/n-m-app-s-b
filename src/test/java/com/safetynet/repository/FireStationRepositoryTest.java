package com.safetynet.repository;

import com.safetynet.model.FireStation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class FireStationRepositoryTest {

    private FireStationRepository fireStationRepository;

    private List<FireStation> fireStationsForTest = new ArrayList<>();

    private Map<String, FireStation> fireStationsByAddressForTest = new HashMap<>();


    @BeforeEach
    public void setUpPerTest() {
        fireStationRepository = new FireStationRepository();
        fireStationsForTest.add(new FireStation("1111 RUE  AA", "11"));
        fireStationsForTest.add(new FireStation("2222 RUE  BB", "22"));
        fireStationsForTest.add(new FireStation("3333 RUE  CC", "33"));
        fireStationsForTest.add(new FireStation("4444 RUE  DD", "11"));
        for (FireStation station : fireStationsForTest) {
            fireStationsByAddressForTest.put(station.address(), station);
        }
        FireStationRepository.fireStationsByAddress = fireStationsByAddressForTest;
    }

    @AfterEach
    public void tearDown() {
        FireStationRepository.fireStationsByAddress = null;
    }

    @Test
    public void should_convert_the_firestations_list_into_a_map_according_to_the_addresses() throws IOException {
        // when
        Map<String, FireStation> results = FireStationRepository.indexByAddress(fireStationsForTest);
        // Then
        assertEquals(4, results.size());
        assertEquals(results.get("1111 RUE  AA").station(), "11");
        assertEquals(results.get("2222 RUE  BB").station(), "22");
        assertEquals(results.get("3333 RUE  CC").station(), "33");
        assertEquals(results.get("4444 RUE  DD").station(), "11");
    }

    @Test
    public void should_save_fireStation_successfully() throws IOException {
        // given
        FireStation fireStationTest = new FireStation("95 rue Coutel", "555");
        FireStation preExistingFireStationAtTheAddress
                = FireStationRepository.fireStationsByAddress.get("95 rue Coutel");
        // when
        fireStationRepository.save(fireStationTest);
        // then
        assertEquals("555", FireStationRepository.fireStationsByAddress.get("95 rue Coutel").station());
        assertNull(preExistingFireStationAtTheAddress);
    }

    @Test
    public void should_update_fireStation_successfully() throws NotFoundException {
        // given
        FireStation fireStationTest = new FireStation("1111 RUE  AA", "999");
        String preExistingStation = FireStationRepository.fireStationsByAddress.get("1111 RUE  AA").station();
        // when
        fireStationRepository.update(fireStationTest);
        // then
        assertEquals(preExistingStation, "11");
        assertEquals(FireStationRepository.fireStationsByAddress.get("1111 RUE  AA").station(), "999");
    }

    @Test
    public void should_throw_an_exception_when_address_corresponding_to_firestation_to_update_is_not_found() {
        // given
        FireStation fireStationTest = new FireStation("8 road endless", "9999");
        FireStation preExistingFireStationAtTheAddress
                = FireStationRepository.fireStationsByAddress.get("8 road endless");
        //when
        NotFoundException thrown
                = assertThrows(NotFoundException.class, () -> {
            fireStationRepository.update(fireStationTest);
        }, "NotFoundException was expected");
        //then
        assertEquals("Unable to find and update fireStation corresponding to address 8 road endless"
                , thrown.getMessage());
        assertNull(preExistingFireStationAtTheAddress);
    }


    @Test
    public void should_delete_fireStation_successfully() throws NotFoundException {
        // given
        FireStation preExistingFireStationAtTheAddress = FireStationRepository.fireStationsByAddress.get("1111 RUE  AA");
        //when
        fireStationRepository.delete("1111 RUE  AA");
        //then
        assertNotNull(preExistingFireStationAtTheAddress);
        assertNull(FireStationRepository.fireStationsByAddress.get("1111 RUE  AA"));
    }

    @Test
    public void should_throw_an_exception_when_address_corresponding_to_firestation_to_delete_is_not_found() {
        //given
        FireStation preExistingFireStationAtTheAddress = FireStationRepository.fireStationsByAddress.get("9 r d S t");
        //when
        NotFoundException thrown
                = assertThrows(NotFoundException.class, () -> {
            fireStationRepository.delete("9 r d S t");
        }, "NotFoundException was expected");
        //then
        assertEquals("Unable to find and delete fireStation corresponding to address 9 r d S t"
                , thrown.getMessage());
        assertNull(preExistingFireStationAtTheAddress);
    }

    @Test
    public void should_find_fireStation_successfully() throws NotFoundException {
        assertEquals(fireStationRepository.findByAddress("1111 RUE  AA").station(), "11");
    }

    @Test
    public void should_throw_an_exception_when_address_corresponding_to_firestation_to_find_is_not_found() {
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    fireStationRepository.findByAddress("4 Manch S t");
                }, "NotFoundException was expected");
        //then
        assertEquals("Unable to find fireStation corresponding to address 4 Manch S t", thrown.getMessage());
    }

    @Test
    public void should_get_addresses_by_fireStation_successfully() throws NotFoundException {
        //given
        List<String> addressesExpected = new ArrayList<String>();
        addressesExpected.add("1111 RUE  AA");
        addressesExpected.add("4444 RUE  DD");
        //when
        TreeSet<String> addresses = fireStationRepository.getAddressesByFireStation("11");
        //then
        assertEquals(2, addresses.size());
        Iterator iterator = addresses.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            assertEquals(addressesExpected.get(i), iterator.next());
            i++;
        }
    }

    @Test
    public void should_throw_an_exception_when_firestation_corresponding_to_addresses_to_get_is_not_found() {
        //when
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            fireStationRepository.getAddressesByFireStation("7777");
        }, "NotFoundException was expected");
        //then
        assertEquals("Firestation 7777 not found in database", thrown.getMessage());
    }

}
