/********************************************************************************************************
 * BillingController.java
 *
 *          Handles Billing related requests.
 *
 * @author Jack Mitchell
 * @since 12/9/2025
 * @version 1.0
 ********************************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Billing;
import com.appointmentProject.backend.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
@CrossOrigin(origins = "*")
public class BillingController {


    @Autowired
    private BillingService billingService;


    // Get all billings
    @GetMapping("/all")
    public ResponseEntity<List<Billing>> getAllBillings() {
        return ResponseEntity.ok(billingService.getAllBillings());
    }

    // Get one billing
    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBilling(@PathVariable("id") int id) {
        return ResponseEntity.ok(billingService.getBillingById(id));
    }

    // Create billing
    @PostMapping("/add")
    public ResponseEntity<Billing> addBilling(@RequestBody Billing billing) {
        return ResponseEntity.ok(billingService.addBilling(billing));
    }

    // Update billing
    @PutMapping("/update/{id}")
    public ResponseEntity<Billing> updateBilling(
            @PathVariable("id") int id,
            @RequestBody Billing billing
    ) {
        return ResponseEntity.ok(billingService.updateBilling(id, billing));
    }


    // Delete billing
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBilling(@PathVariable("id") int id) {
        billingService.deleteBilling(id);
        return ResponseEntity.ok("Billing deleted.");
    }
}