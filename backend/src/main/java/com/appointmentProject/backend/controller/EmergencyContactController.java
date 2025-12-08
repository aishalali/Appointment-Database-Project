/********************************************************************************************************
 * EmergencyContactController.java
 *
 *      This controller defines the API endpoints that the frontend will call.
 *      Its ONLY responsibility is:
 *          - Receive HTTP requests from the frontend
 *          - Validate high-level parameters (if needed)
 *          - Pass the work to the InsuranceService
 *          - Return the service’s results as HTTP responses
 *
 * @author Matthew Kiyono
 * @since 12/7/2025
 * @version 1.0
 ********************************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.EmergencyContact;
import com.appointmentProject.backend.service.EmergencyContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emergency")
@CrossOrigin(origins = "*")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService ecService;

    // Add Emergency Contact
    @PostMapping("/add")
    public ResponseEntity<EmergencyContact> addEmergencyContact(@RequestBody EmergencyContact ec) {
        EmergencyContact created = ecService.addEmergencyContact(ec);
        return ResponseEntity.ok(created);
    }

    // Get All
    @GetMapping("/all")
    public ResponseEntity<List<EmergencyContact>> getAll() {
        return ResponseEntity.ok(ecService.getAllEmergencyContacts());
    }

    // Get By ID
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContact> getById(@PathVariable("id") int id) {
        return ecService.getEmergencyContactById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new RecordNotFoundException("Emergency contact with ID " + id + " was not found.")
                );
    }

    // Get By Last Name
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<EmergencyContact>> getByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(ecService.getByLastName(lastName));
    }

    // Get By Phone
    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<EmergencyContact>> getByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(ecService.getByPhone(phone));
    }

    // Get By Email
    @GetMapping("/email/{email}")
    public ResponseEntity<List<EmergencyContact>> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(ecService.getByEmail(email));
    }

    // Update Emergency Contact
    @PutMapping("/update")
    public ResponseEntity<EmergencyContact> updateEmergencyContact(@RequestBody EmergencyContact ec) {
        EmergencyContact updated = ecService.updateEmergencyContact(ec);
        return ResponseEntity.ok(updated);
    }

    // Delete Emergency Contact
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmergencyContact(@PathVariable("id") int id) {
        return ecService.getEmergencyContactById(id)
                .map(existing -> {
                    ecService.removeEmergencyContact(existing);
                    return ResponseEntity.ok("Emergency contact record removed successfully.");
                })
                .orElseThrow(() ->
                        new RecordNotFoundException("Cannot delete — ID " + id + " does not exist.")
                );
    }
}
