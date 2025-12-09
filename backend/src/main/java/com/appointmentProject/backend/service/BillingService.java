/******************************************************************************************************************
 * BillingService.java
 *
 *          Provides the CRUD operations and certain find queries for getting the billing data.
 *
 * @author Jack Mitchell
 * @since 12/9/2025
 * @version 1.0
 ******************************************************************************************************************/

package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Billing;
import com.appointmentProject.backend.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepo;

    // Get all billings
    public List<Billing> getAllBillings() {
        return billingRepo.findAll();
    }

    // Get single billing
    public Billing getBillingById(int id) {
        return billingRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Billing with id " + id + " not found."));
    }

    // Create billing
    public Billing addBilling(Billing billing) {

        return billingRepo.save(billing);
    }

    // Update billing
    public Billing updateBilling(int id, Billing updated) {

        Billing existing = billingRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Billing with ID " + id + " not found."));

        // Update fields
        existing.setCost(updated.getCost());
        existing.setStatusOfPayment(updated.getStatusOfPayment());
        existing.setPaymentType(updated.getPaymentType());
        existing.setInsuranceId(updated.getInsuranceId());

        return billingRepo.save(existing);
    }

    // Delete billing
    public void deleteBilling(int id) {

        if (!billingRepo.existsById(id)) {
            throw new RecordNotFoundException("Billing with ID " + id + " not found.");
        }

        billingRepo.deleteById(id);
    }
}
