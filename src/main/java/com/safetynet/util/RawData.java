package com.safetynet.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.FireStation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Data
public class RawData {
    public List<Person> persons;
    public List<FireStation> firestations;
    public List<MedicalRecord> medicalrecords;

    // Convert the data file data.json to a RawData Java object
    public static com.safetynet.util.RawData rawDataDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        com.safetynet.util.RawData rawData = objectMapper
                .readValue(new File("src/main/resources/data.json"), com.safetynet.util.RawData.class);
        return rawData;
    }
}
