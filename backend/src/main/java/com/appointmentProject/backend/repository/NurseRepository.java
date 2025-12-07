/********************************************************************************************
 *     NurseRepository
 *      The repository interface involving Nurse.
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
 * @since 12/6/2025
 *
 ********************************************************************************************/
package com.appointmentProject.backend.repository;

import com.appointmentProject.backend.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer> {

    // Later we can add:
    // Optional<Nurse> findByEmail(String email);
    // Optional<Nurse> findByFirstNameAndLastName(String firstName, String lastName);
}
