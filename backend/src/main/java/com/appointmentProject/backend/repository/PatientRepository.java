
/********************************************************************************************
 *     PatientRepository.java
 *
 *      Repository interface involving Patient entities.
 *
 * @author Matthew Kiyono
 * @since 12/4/2025
 * @version 1.0
 ********************************************************************************************/
package com.appointmentProject.backend.repository;

import com.appointmentProject.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    List<Patient> findByLastName(String lastName);

    List<Patient> findByPhone(String phone);

    List<Patient> findByEmail(String email);

    List<Patient> findByGender(String gender);

    List<Patient> findByInsuranceId(Integer insuranceId);
}
