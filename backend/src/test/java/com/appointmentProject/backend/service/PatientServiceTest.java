package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Patient;
import com.appointmentProject.backend.repository.PatientRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/******************************************************************************************
 * PatientServiceTest.java
 *
 *      Tests the PatientService using Mockito to mock the repository.
 *      No actual Database calls are made.
 *
 * @author Matthew Kiyono
 * @since 12/4/2025
 * @version 1.0
 ******************************************************************************************/
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepo;

    @InjectMocks
    private PatientService patientService;

    private Patient samplePatient;

    @BeforeEach
    void setup() {
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
        when(patientRepo.save(samplePatient)).thenReturn(samplePatient);

        Patient created = patientService.addPatient(samplePatient);

        assertEquals("John", created.getFirstName());
        verify(patientRepo, times(1)).save(samplePatient);
    }

    @Test
    void testUpdatePatient_Success() {
        Patient updated = new Patient.Builder(
                "Johnny",
                "Doe",
                LocalDate.of(1990, 1, 1),
                35,
                80.0,
                180.0,
                "555-1234"
        )
                .gender("Male")
                .email("johnny.doe@example.com")
                .insuranceId(10)
                .emergencyContactId(5)
                .build();

        when(patientRepo.findById(1)).thenReturn(Optional.of(samplePatient));
        when(patientRepo.save(any(Patient.class))).thenReturn(updated);

        Patient result = patientService.updatePatient(updated);

        assertEquals("Johnny", result.getFirstName());
        assertEquals("johnny.doe@example.com", result.getEmail());
    }

    @Test
    void testUpdatePatient_NotFound() {
        when(patientRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> patientService.updatePatient(samplePatient));
    }

    @Test
    void testGetAllPatients() {
        when(patientRepo.findAll()).thenReturn(List.of(samplePatient));

        List<Patient> list = patientService.getAllPatients();

        assertEquals(1, list.size());
        verify(patientRepo).findAll();
    }

    @Test
    void testGetByLastName() {
        when(patientRepo.findByLastName("Doe")).thenReturn(List.of(samplePatient));

        List<Patient> results = patientService.getByLastName("Doe");

        assertEquals(1, results.size());
    }
}
