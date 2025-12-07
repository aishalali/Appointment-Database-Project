

package com.appointmentProject.backend.controller;
/********************************************************************************************************
 * ProviderController.java
 *
 *          Handles Provider related requests.
 *
 * @author Matthew Kiyono
 * @since 12/6/2025
 * @version 1.0
 ********************************************************************************************************/
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

    // 1. Get all providers
    @GetMapping("/all")
    public ResponseEntity<List<Provider>> getAllProviders() {
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    // 2. Get one provider by ID
    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable int id) {
        Provider provider = providerService.getById(id);
        return ResponseEntity.ok(provider);
    }

    // 3. Create provider
    @PostMapping("/add")
    public ResponseEntity<Provider> addProvider(@RequestBody Provider provider) {
        Provider created = providerService.addProvider(provider);
        return ResponseEntity.ok(created);
    }

    // 4. Update provider
    @PutMapping("/update/{id}")
    public ResponseEntity<Provider> updateProvider(
            @PathVariable int id,
            @RequestBody Provider provider
    ) {
        Provider updated = providerService.updateProvider(id, provider);
        return ResponseEntity.ok(updated);
    }

    // 5. Delete provider
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable int id) {
        providerService.deleteProvider(id);
        return ResponseEntity.ok("Provider deleted successfully.");
    }
}
