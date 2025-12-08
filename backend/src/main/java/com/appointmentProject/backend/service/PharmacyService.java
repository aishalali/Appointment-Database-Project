/******************************************************************************************************************
 * PatientService.java
 *
 *          Provides the CRUD operations and certain find queries for getting the patient data.
 *
 * @author Aisha Ali
 * @since 12/8/2025
 * @version 1.0
 ******************************************************************************************************************/
package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Pharmacy;
import com.appointmentProject.backend.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepo;

    // Get all pharmacies
    public List<Pharmacy> getAllPharmacies() {
        return pharmacyRepo.findAll();
    }

    // Get one pharmacy by ID
    public Pharmacy getPharmacyById(int id) {
        return pharmacyRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Pharmacy with ID " + id + " not found."));
    }

    // Create pharmacy
    public Pharmacy addPharmacy(Pharmacy pharmacy) {

        // OPTIONAL: Add validation rules if needed (email unique, phone unique, etc.)

        return pharmacyRepo.save(pharmacy);
    }

    // Update pharmacy
    public Pharmacy updatePharmacy(int id, Pharmacy updated) {

        Pharmacy existing = pharmacyRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Pharmacy with ID " + id + " not found."));

        existing.setName(updated.getName());
        existing.setPhone(updated.getPhone());
        existing.setEmail(updated.getEmail());
        existing.setAddress(updated.getAddress());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());

        return pharmacyRepo.save(existing);
    }

    // Delete pharmacy
    public void deletePharmacy(int id) {

        if (!pharmacyRepo.existsById(id)) {
            throw new RecordNotFoundException("Pharmacy with ID " + id + " not found.");
        }

        pharmacyRepo.deleteById(id);
    }
}
