/********************************************************************************************
 *     BillingRepository
 *      The repository interface involving Billing.
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
 * @author Jack Mitchell
 * @version 1.0
 * @since 12/9/2025
 *
 ********************************************************************************************/
package com.appointmentProject.backend.repository;

import com.appointmentProject.backend.model.Billing;
import com.appointmentProject.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {


}
