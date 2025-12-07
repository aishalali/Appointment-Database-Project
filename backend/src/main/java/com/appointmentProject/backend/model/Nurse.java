/*****************************************************************************************
 * Nurse.java
 *
 *      This is the blueprint for storing information related to Nurses and acts as a
 *      model to transfer information between the backend and the frontend.
 *
 *      Contains identifying variables of the Nurse and is used by the service layer for
 *      retrieval and updates.
 *      - "id": is a unique identifier that differentiates between Nurses. It is also the
 *          primary way of accessing specific nurse records. This is immutable.
 *          The number combination is created by the auto-increment feature
 *          in MySQL.
 *      - "firstName", "lastName", "phone", and "email" are inherited from Person.java.
 *      - "address": the Nurse's address.
 *
 *
 * @author Matthew Kiyono
 * @version 1.4
 * @since 10/20/2025
 ****************************************************************************************/

package com.appointmentProject.backend.model;

import com.appointmentProject.backend.abstractmodel.Person;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "nurse")
@DynamicUpdate
public class Nurse extends Person {

    //variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    // --- REQUIRED BY JPA (no-arg constructor) ---
    protected Nurse() {
        // for JPA only
    }

    //Constructor
    public Nurse(int id, String firstName, String lastName, String phone, String email, String address) {
        super(firstName, lastName, phone, email);
        this.id = id;
        this.address = address;
    }

    //getter method
    public int getId() {return id;}
    public String getAddress() {return address;}

    //setter method
    public void setId(int id) {this.id = id;}
    public void setAddress(String address) {this.address = address;}

    //toString
    public String toString(){
        return "Nurse:\n" +
                "Nurse id: " + this.id + "\n" +
                super.toString() +
                "\nAddress: " + this.address + "\n";
    }
}
