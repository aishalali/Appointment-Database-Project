package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Medication;
import com.appointmentProject.backend.service.MedicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medication")
public class MedicationController {

    private final MedicationService service;

    public MedicationController(MedicationService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Medication> getAll() {
        return service.getAllMedications();
    }

    @GetMapping("/{id}")
    public Medication getById(@PathVariable int id) {
        return service.getMedicationById(id);
    }

    @PostMapping("/save")
    public Medication save(@RequestBody Medication medication) {
        return service.saveMedication(medication);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deleteMedication(id);
    }
}

