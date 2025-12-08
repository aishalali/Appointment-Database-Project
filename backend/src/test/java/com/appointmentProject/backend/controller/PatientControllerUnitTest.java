package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Patient;
import com.appointmentProject.backend.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/******************************************************************************************
 * PatientControllerUnitTest.java
 *
 *      A unit test determining if the controller test is functioning.
 *
 * @author Matthew Kiyono
 * @since 12/4/2025
 * @version 1.0
 ******************************************************************************************/
public class PatientControllerUnitTest {

    private PatientController controller;
    private PatientService mockService;

    private Patient samplePatient;

    @BeforeEach
    void setup() {
        mockService = Mockito.mock(PatientService.class);
        controller = new PatientController();
        controller.patientService = mockService;

        samplePatient = new Patient.Builder(

                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                35,
                80.0,
                180.0,
                "555-1234"
        )
                .gender("Male")
                .email("john.doe@example.com")
                .insuranceId(10)
                .emergencyContactId(5)
                .build();
    }

    @Test
    void testAddPatient() {
        when(mockService.addPatient(any(Patient.class))).thenReturn(samplePatient);

        ResponseEntity<Patient> response = controller.addPatient(samplePatient);

        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    void testGetById_Found() {
        when(mockService.getPatientById(1)).thenReturn(Optional.of(samplePatient));

        ResponseEntity<Patient> response = controller.getById(1);

        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testGetById_NotFound() {
        when(mockService.getPatientById(99)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> controller.getById(99));
    }

    @Test
    void testGetByLastName() {
        when(mockService.getByLastName("Doe")).thenReturn(List.of(samplePatient));

        ResponseEntity<List<Patient>> response = controller.getByLastName("Doe");

        assertEquals(1, response.getBody().size());
    }
}
