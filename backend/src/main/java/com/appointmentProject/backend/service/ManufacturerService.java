package com.appointmentProject.backend.service;

import com.appointmentProject.backend.model.Manufacturer;
import com.appointmentProject.backend.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {

    private final ManufacturerRepository repo;

    public ManufacturerService(ManufacturerRepository repo) {
        this.repo = repo;
    }

    public List<Manufacturer> getAllManufacturers() {
        return repo.findAll();
    }

    public Manufacturer getManufacturerById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Manufacturer saveManufacturer(Manufacturer manufacturer) {
        return repo.save(manufacturer);
    }

    public void deleteManufacturer(int id) {
        repo.deleteById(id);
    }
}
