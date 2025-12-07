/********************************************************************************************************
 * ProviderController.java
 *
 *          Handles Provider related requests.
 *
 * @author Matthew Kiyono
 * @since 12/6/2025
 * @version 1.1
 ********************************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Provider;
import com.appointmentProject.backend.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provider")
@CrossOrigin(origins = "*")
public class ProviderController {


    @Autowired
    private ProviderService providerService;


    // Get all providers
    @GetMapping("/all")
    public ResponseEntity<List<Provider>> getAllProviders() {
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    // Get one provider
    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProvider(@PathVariable int id) {
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    // Create provider
    @PostMapping("/add")
    public ResponseEntity<Provider> addProvider(@RequestBody Provider provider) {
        return ResponseEntity.ok(providerService.addProvider(provider));
    }

    // Update provider
    @PutMapping("/update/{id}")
    public ResponseEntity<Provider> updateProvider(
            @PathVariable int id,
            @RequestBody Provider provider
    ) {
        return ResponseEntity.ok(providerService.updateProvider(id, provider));
    }

    // Delete provider
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable int id) {
        providerService.deleteProvider(id);
        return ResponseEntity.ok("Provider deleted.");
    }
}
