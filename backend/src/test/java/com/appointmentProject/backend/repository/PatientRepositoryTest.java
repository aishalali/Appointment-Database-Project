package com.appointmentProject.backend.repository;

import com.appointmentProject.backend.model.Patient;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*************************************************************************************************
 *  PatientRepositoryTest.java
 *
 *      Integration tests to ensure PatientRepository queries execute correctly.
 *      Uses the REAL database connection.
 *
 * @author Matthew Kiyono
 * @since 12/4/2025
 * @version 1.0
 ************************************************************************************************/
@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void resetDatabase() {
        entityManager.createQuery("DELETE FROM Patient").executeUpdate();
    }

    @Test
    void testSaveAndFindAll() {
        Patient p = new Patient.Builder(

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

        patientRepository.save(p);

        List<Patient> all = patientRepository.findAll();

        assertEquals(1, all.size());
        assertEquals("John", all.get(0).getFirstName());
    }

    @Test
    void testFindByLastName() {
        Patient p1 = new Patient.Builder(

                "Alice",
                "Smith",
                LocalDate.of(1995, 5, 5),
                30,
                60.0,
                165.0,
                "555-1111"
        )
                .gender("Female")
                .email("alice@example.com")
                .insuranceId(20)
                .build();

        Patient p2 = new Patient.Builder(

                "Bob",
                "Smith",
                LocalDate.of(1992, 3, 10),
                33,
                85.0,
                175.0,
                "555-2222"
        )
                .gender("Male")
                .email("bob@example.com")
                .insuranceId(30)
                .build();

        patientRepository.save(p1);
        patientRepository.save(p2);

        List<Patient> smiths = patientRepository.findByLastName("Smith");

        assertEquals(2, smiths.size());
    }

    @Test
    void testFindByInsuranceId() {
        Patient p = new Patient.Builder(

                "Carol",
                "Jones",
                LocalDate.of(1988, 8, 8),
                37,
                70.0,
                170.0,
                "555-3333"
        )
                .insuranceId(42)
                .build();

        patientRepository.save(p);

        List<Patient> insured42 = patientRepository.findByInsuranceId(42);

        assertEquals(1, insured42.size());
        assertEquals(42, insured42.get(0).getInsuranceId());
    }
}
