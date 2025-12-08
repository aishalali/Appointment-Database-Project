/********************************************************************************************
 *     EmergencyContactRepository
 *      The repository interface involving EmergencyContact.
 *
 *      The imports of Repository and JpaRepository automatically provide the following prompts:
 *      - findAll() - Select * query
 *      - findById(Integer id) - select by primary key
 *      - save(Insurance entity) - insert and update
 *      - deleteById(Integer id) - remove
 *
 *      For simplistic queries, like findBy<variableTypeHere> only need their method headers,
 *      while more advance prompts require a different structure:
 *
 *      (Custom Query Format):
 *
 *      (@)Query       //(without the parenthesis around the @ sign)
 *      (""" ... """)  //(triple quotes ensures queries are clean and multi-lined)
 *
 *      List<Entity> findBy<...>(@Param("<databaseFieldName>") <modelFieldVarType> <modelFieldName>);
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/7/2025
 *
 ********************************************************************************************/
package com.appointmentProject.backend.repository;

import com.appointmentProject.backend.model.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Integer> {

    List<EmergencyContact> findByLastName(String lastName);

    List<EmergencyContact> findByPhone(String phone);

    List<EmergencyContact> findByEmail(String email);

    Optional<EmergencyContact> findByFirstNameAndLastName(String firstName, String lastName);
}
