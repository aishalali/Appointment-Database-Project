
/********************************************************************************************************
 * InsuranceController.java
 *
 *      This controller defines the API endpoints that the frontend will call.
 *      Its ONLY responsibility is:
 *          - Receive HTTP requests from the frontend
 *          - Validate high-level parameters (if needed)
 *          - Pass the work to the InsuranceService
 *          - Return the service’s results as HTTP responses
 *
 * @author Matthew Kiyono
 * @version 11/13/2025
 * @version 1.0
 ********************************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Insurance;
import com.appointmentProject.backend.service.InsuranceService;
import com.appointmentProject.backend.exception.RecordNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    @Autowired
    InsuranceService insService;

    //Adding Insurance
    @PostMapping("/add")
    public ResponseEntity<Insurance> addInsurance(@RequestBody Insurance ins) {
        Insurance created = insService.addInsurance(ins);
        return ResponseEntity.ok(created);
    }

    //Get All
    @GetMapping("/all")
    public ResponseEntity<List<Insurance>> getAll() {
        List<Insurance> list = insService.getAllInsurance();
        return ResponseEntity.ok(list);
    }

    //Get By ID
    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getById(@PathVariable("id") int id) {
        return insService.getInsuranceById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Insurance record with ID " + id + " was not found."
                ));
    }

    //Get By Insurance Name
    @GetMapping("/name/{insuranceName}")
    public ResponseEntity<List<Insurance>> getByInsuranceName(@PathVariable String insuranceName) {
        List<Insurance> results = insService.getByInsuranceName(insuranceName);
        return ResponseEntity.ok(results);
    }

    //Update Insurance
    @PutMapping("/update")
    public ResponseEntity<Insurance> updateInsurance(@RequestBody Insurance ins) {
        Insurance updated = insService.updateInsurance(ins);
        return ResponseEntity.ok(updated);
    }

    // Remove Insurance
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInsurance(@PathVariable("id") int id) {

        // Step 1: Check existence
        return insService.getInsuranceById(id)
                .map(existing -> {
                    insService.removeInsurance(existing);
                    return ResponseEntity.ok("Insurance record removed successfully.");
                })
                .orElseThrow(() ->
                        new RecordNotFoundException("Cannot delete — ID " + id + " does not exist.")
                );
    }


}
