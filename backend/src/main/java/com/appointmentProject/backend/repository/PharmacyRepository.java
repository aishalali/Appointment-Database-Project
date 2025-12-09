/********************************************************************************************
 *     PharmacyRepository.java
 *
 *      Repository interface involving Pharmacy entities.
 *
 * @author Aisha Ali
 * @since 12/7/2025
 * @version 1.0
 ********************************************************************************************/

package com.appointmentProject.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.appointmentProject.backend.model.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Integer> {
}
