/******************************************************************************************************************
 * EmergencyContactService.java
 *
 *      This service class calls on the repository interfaces to communicate with the database.
 *
 *      The types of queries that can be made:
 *      - Insert
 *      - Delete
 *      - Update
 *      - Select (by: all, Id, insurance name, address, email, and phone number)
 *
 *      Later updates will include format checks on parameter data.
 *      List of validation checks and formatting checks needing to be done prior to completion:
 *          - Ensure that every field is unique.
 *              - This will require multiple queries to be done on every field.
 *              - None of the fields can be null.
 *
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/7/2025
 *******************************************************************************************************************/
package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.EmergencyContact;
import com.appointmentProject.backend.repository.EmergencyContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmergencyContactService {

    @Autowired
    private EmergencyContactRepository ecRepo;

    // INSERT
    public EmergencyContact addEmergencyContact(EmergencyContact ec) {

        validateRequiredFields(ec);

        // Enforce unique (firstName, lastName) within the table
        Optional<EmergencyContact> existing =
                ecRepo.findByFirstNameAndLastName(ec.getFirstName(), ec.getLastName());

        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "An emergency contact with the name " +
                            ec.getFirstName() + " " + ec.getLastName() +
                            " already exists.");
        }

        return ecRepo.save(ec);
    }

    // DELETE
    public void removeEmergencyContact(EmergencyContact ec) {
        ecRepo.deleteById(ec.getId());
    }

    // UPDATE
    public EmergencyContact updateEmergencyContact(EmergencyContact update) {

        Optional<EmergencyContact> exists = ecRepo.findById(update.getId());

        if (exists.isEmpty()) {
            throw new RecordNotFoundException(
                    "Emergency contact with ID " + update.getId() + " was not found."
            );
        }

        EmergencyContact current = exists.get();

        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setPhone(update.getPhone());
        current.setEmail(update.getEmail());
        current.setAddress(update.getAddress());

        return ecRepo.save(current);
    }

    // SELECT ALL
    public List<EmergencyContact> getAllEmergencyContacts() {
        return ecRepo.findAll();
    }

    // SELECT BY ID
    public Optional<EmergencyContact> getEmergencyContactById(int id) {
        return ecRepo.findById(id);
    }

    // QUERIES
    public List<EmergencyContact> getByLastName(String lastName) {
        return ecRepo.findByLastName(lastName);
    }

    public List<EmergencyContact> getByPhone(String phone) {
        return ecRepo.findByPhone(phone);
    }

    public List<EmergencyContact> getByEmail(String email) {
        return ecRepo.findByEmail(email);
    }

    // basic validation so DB doesn't explode on null required fields
    private void validateRequiredFields(EmergencyContact ec) {
        if (ec.getFirstName() == null || ec.getFirstName().isBlank()) {
            throw new IllegalArgumentException("Emergency contact first name is required.");
        }
        if (ec.getLastName() == null || ec.getLastName().isBlank()) {
            throw new IllegalArgumentException("Emergency contact last name is required.");
        }
        if (ec.getPhone() == null || ec.getPhone().isBlank()) {
            throw new IllegalArgumentException("Emergency contact phone is required.");
        }
    }
}

