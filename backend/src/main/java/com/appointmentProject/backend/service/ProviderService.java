/******************************************************************************************************************
 * ProviderService.java
 *
 *          Provides the CRUD operations and certain find queries for getting the provider data.
 *
 * @author Matthew Kiyono
 * @since 12/6/2025
 * @version 1.0
 ******************************************************************************************************************/

package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Provider;
import com.appointmentProject.backend.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepo;

    // Get all providers
    public List<Provider> getAllProviders() {
        return providerRepo.findAll();
    }

    // Get single provider
    public Provider getProviderById(int id) {
        return providerRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Provider with id " + id + " not found."));
    }

    // Create provider
    public Provider addProvider(Provider provider) {

        // Validate: unique first + last name
        if (providerRepo.findByFirstNameAndLastName(provider.getFirstName(), provider.getLastName()).isPresent()) {
            throw new IllegalArgumentException("A provider with this first + last name already exists.");
        }

        // Validate: unique email
        if (providerRepo.findByEmail(provider.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A provider with this email already exists.");
        }

        return providerRepo.save(provider);
    }

    // Update provider
    public Provider updateProvider(int id, Provider updated) {

        Provider existing = providerRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Provider with ID " + id + " not found."));

        // Update fields
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhone(updated.getPhone());
        existing.setEmail(updated.getEmail());
        existing.setSpecialty(updated.getSpecialty());
        existing.setAddress(updated.getAddress());

        return providerRepo.save(existing);
    }

    // Delete provider
    public void deleteProvider(int id) {

        if (!providerRepo.existsById(id)) {
            throw new RecordNotFoundException("Provider with ID " + id + " not found.");
        }

        providerRepo.deleteById(id);
    }
}
