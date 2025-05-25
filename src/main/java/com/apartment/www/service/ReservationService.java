package com.apartment.www.service;

import com.apartment.www.entity.ReservationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {

    private final String FILE_PATH = "src/main/resources/reservations.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Boolean> getReservations() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return new HashMap<>();
            return objectMapper.readValue(file, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not read reservations", e);
        }
    }

    public Map<String, Boolean> updateReservation(List<ReservationRequest> requests) {

        clearPastReservations();
        Map<String, Boolean> reservations = getReservations();

        for (ReservationRequest req :requests) {

            reservations.put(req.getDate(), req.isReserved());
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), reservations);
            return reservations;
        } catch (IOException e) {
            throw new RuntimeException("Could not write reservations", e);
        }

    }

    public Map<String, Boolean> clearPastReservations() {
        Map<String, Boolean> reservations = getReservations();

        LocalDate today = LocalDate.now();
        Map<String, Boolean> updated = new HashMap<>();

        for (Map.Entry<String, Boolean> entry : reservations.entrySet()) {
            LocalDate date = LocalDate.parse(entry.getKey());

            // Keep only today and future dates
            if (!date.isBefore(today)) {
                updated.put(entry.getKey(), entry.getValue());
            }
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), updated);
        } catch (IOException e) {
            throw new RuntimeException("Could not update reservation file", e);
        }

        return updated;
    }


}
