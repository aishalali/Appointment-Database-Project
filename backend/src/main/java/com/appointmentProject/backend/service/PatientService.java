/******************************************************************************************************************
 * PatientService.java
 *
 *          Provides the CRUD operations and certain find queries for getting the patient data.
 *
 * @author Matthew Kiyono
 * @since 12/4/2025
 * @version 1.0
 ******************************************************************************************************************/
package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Patient;
import com.appointmentProject.backend.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepo;

    // INSERT
    public Patient addPatient(Patient patient) {
        return patientRepo.save(patient);
    }

    // DELETE
    public void removePatient(Patient patient) {
        patientRepo.deleteById(patient.getId());
    }

    // UPDATE
    public Patient updatePatient(Patient update) {
        Optional<Patient> exists = patientRepo.findById(update.getId());

        if (exists.isEmpty()) {
            throw new RecordNotFoundException(
                    "Patient with ID " + update.getId() + " was not found."
            );
        }

        Patient current = exists.get();

        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setPhone(update.getPhone());
        current.setEmail(update.getEmail());

        current.setDoB(update.getDoB());
        current.setAge(update.getAge());
        current.setWeight(update.getWeight());
        current.setHeight(update.getHeight());
        current.setGender(update.getGender());
        current.setInsuranceId(update.getInsuranceId());
        current.setEmergencyContactId(update.getEmergencyContactId());

        return patientRepo.save(current);
    }

    // SELECT ALL
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    // SELECT BY ID
    public Optional<Patient> getPatientById(int id) {
        return patientRepo.findById(id);
    }

    // QUERIES
    public List<Patient> getByLastName(String lastName) {
        return patientRepo.findByLastName(lastName);
    }

    public List<Patient> getByPhone(String phone) {
        return patientRepo.findByPhone(phone);
    }

    public List<Patient> getByEmail(String email) {
        return patientRepo.findByEmail(email);
    }

    public List<Patient> getByGender(String gender) {
        return patientRepo.findByGender(gender);
    }

    public List<Patient> getByInsuranceId(Integer insuranceId) {
        return patientRepo.findByInsuranceId(insuranceId);
    }
}
