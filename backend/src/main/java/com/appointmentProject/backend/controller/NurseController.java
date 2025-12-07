/********************************************************************************************************
 * NurseController.java
 *
 *          Handles Nurse related requests.
 *
 * @author Matthew Kiyono
 * @since 12/6/2025
 * @version 1.0
 ********************************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Nurse;
import com.appointmentProject.backend.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nurse")
@CrossOrigin(origins = "*")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    // 1. Get all nurses
    @GetMapping("/all")
    public ResponseEntity<List<Nurse>> getAllNurses() {
        return ResponseEntity.ok(nurseService.getAllNurses());
    }

    // 2. Get one nurse by ID
    @GetMapping("/{id}")
    public ResponseEntity<Nurse> getNurseById(@PathVariable int id) {
        Nurse nurse = nurseService.getById(id);
        return ResponseEntity.ok(nurse);
    }

    // 3. Create nurse
    @PostMapping("/add")
    public ResponseEntity<Nurse> addNurse(@RequestBody Nurse nurse) {
        Nurse created = nurseService.addNurse(nurse);
        return ResponseEntity.ok(created);
    }

    // 4. Update nurse
    @PutMapping("/update/{id}")
    public ResponseEntity<Nurse> updateNurse(
            @PathVariable int id,
            @RequestBody Nurse nurse
    ) {
        Nurse updated = nurseService.updateNurse(id, nurse);
        return ResponseEntity.ok(updated);
    }

    // 5. Delete nurse
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNurse(@PathVariable int id) {
        nurseService.deleteNurse(id);
        return ResponseEntity.ok("Nurse deleted successfully.");
    }
}
