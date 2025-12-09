package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Manufacturer;
import com.appointmentProject.backend.service.ManufacturerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manufacturer")
public class ManufacturerController {

    private final ManufacturerService service;

    public ManufacturerController(ManufacturerService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Manufacturer> getAll() {
        return service.getAllManufacturers();
    }

    @GetMapping("/{id}")
    public Manufacturer getById(@PathVariable int id) {
        return service.getManufacturerById(id);
    }

    @PostMapping("/save")
    public Manufacturer save(@RequestBody Manufacturer manufacturer) {
        return service.saveManufacturer(manufacturer);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.deleteManufacturer(id);
    }
}

