/********************************************************************************************************
 * PatientController.java
 *
 *          Handles Patient related requests.
 *
 * @author Aisha Ali
 * @since 12/8/2025
 * @version 1.0
 ********************************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Pharmacy;
import com.appointmentProject.backend.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pharmacy")
@CrossOrigin(origins = "*")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @GetMapping("/all")
    public ResponseEntity<List<Pharmacy>> getAllPharmacies() {
        return ResponseEntity.ok(pharmacyService.getAllPharmacies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getPharmacy(@PathVariable("id") int id) {
        return ResponseEntity.ok(pharmacyService.getPharmacyById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Pharmacy> addPharmacy(@RequestBody Pharmacy pharmacy) {
        return ResponseEntity.ok(pharmacyService.addPharmacy(pharmacy));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Pharmacy> updatePharmacy(
            @PathVariable("id") int id,
            @RequestBody Pharmacy pharmacy
    ) {
        return ResponseEntity.ok(pharmacyService.updatePharmacy(id, pharmacy));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePharmacy(@PathVariable("id") int id) {
        pharmacyService.deletePharmacy(id);
        return ResponseEntity.ok("Pharmacy deleted.");
    }
}
