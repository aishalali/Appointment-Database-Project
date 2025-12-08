/******************************************************************************************************************
 * InsuranceService.java
 *
 *      This service class calls on the repository interfaces to communicate with the database.
 *
 *      The types of queries that can be made:
 *      - Insert
 *      - Delete
 *      - Update
 *      - Select (by: all, Id, insurance name, address, email, and phone number)
 *
 *      Later updates will include format checks on parameter data.
 *      List of validation checks and formatting checks needing to be done prior to completion:
 *          - Ensure that every field is unique.
 *              - This will require multiple queries to be done on every field.
 *              - None of the fields can be null.
 *
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 11/13/2025
 *******************************************************************************************************************/
package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.repository.InsuranceRepository;
import com.appointmentProject.backend.model.Insurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class InsuranceService {

    @Autowired
    private InsuranceRepository insRepo;

    //1. Insert Query (adds a record into the
    public Insurance addInsurance(Insurance ins) {

        //format checker here (pass for now)
        return insRepo.save(ins);
    }

    //2. Delete Query
    public void removeInsurance(Insurance ins) {

        //format checker here (pass for now)
        insRepo.deleteById(ins.getId());
    }


    //3. Update Query
    public Insurance updateInsurance(Insurance update) {

        //format checker here (pass for now)

        //checks to see if it exists
        Optional<Insurance> exists = insRepo.findById(update.getId());
        if (exists.isEmpty()) {
            throw new RecordNotFoundException("The record with the id " + update.getId() +
                    " does not exist.");
        }

        Insurance current = exists.get();

        //3. Apply new values
        current.setInsuranceName(update.getInsuranceName());
        current.setPhone(update.getPhone());
        current.setEmail(update.getEmail());
        current.setAddress(update.getAddress());

        //4. Save and return updated version
        return insRepo.save(current);
    }


    //4. Select All
    public List<Insurance> getAllInsurance() {
        return insRepo.findAll();
    }


    //5. Select BY
        // a. ID
    public Optional<Insurance> getInsuranceById(int id) {
        return insRepo.findById(id);
    }

        // b. Insurance Name
    public List<Insurance> getByInsuranceName(String name) {
        return insRepo.findByInsuranceName(name);
    }

        // c. Phone Number
    public List<Insurance> getByPhone(String phone) {
        return insRepo.findByPhone(phone);
    }

        //d. Email
    public List<Insurance> getByEmail(String email) {
        return insRepo.findByEmail(email);
    }

    //e. Address
    public List<Insurance> getByAddress(String address) {
        return insRepo.findByAddress(address);
    }

}
