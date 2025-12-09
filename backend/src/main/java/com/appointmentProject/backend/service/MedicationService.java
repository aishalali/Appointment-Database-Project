package com.appointmentProject.backend.service;

import com.appointmentProject.backend.model.Medication;
import com.appointmentProject.backend.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationService {

    private final MedicationRepository repo;

    public MedicationService(MedicationRepository repo) {
        this.repo = repo;
    }

    public List<Medication> getAllMedications() {
        return repo.findAll();
    }

    public Medication getMedicationById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Medication saveMedication(Medication medication) {
        return repo.save(medication);
    }

    public void deleteMedication(int id) {
        repo.deleteById(id);
    }
}
