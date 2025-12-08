
/********************************************************************************************************
 * PatientController.java
 *
 *          Handles Patient related requests.
 *
 * @author Matthew Kiyono
 * @since 12/4/2025
 * @version 1.1
 ********************************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Patient;
import com.appointmentProject.backend.service.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping("/add")
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        Patient created = patientService.addPatient(patient);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getAll() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable("id") int id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new RecordNotFoundException("Patient with ID " + id + " was not found.")
                );
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<Patient>> getByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(patientService.getByLastName(lastName));
    }

    @PutMapping("/update")
    public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient) {
        Patient updated = patientService.updatePatient(patient);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable("id") int id) {
        return patientService.getPatientById(id)
                .map(existing -> {
                    patientService.removePatient(existing);
                    return ResponseEntity.ok("Patient record removed successfully.");
                })
                .orElseThrow(() ->
                        new RecordNotFoundException("Cannot delete — ID " + id + " does not exist.")
                );
    }
}