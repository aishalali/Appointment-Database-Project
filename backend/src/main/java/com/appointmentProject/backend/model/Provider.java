/******************************************************************************************
 * Provider.java
 *
 *      This is the blueprint for storing information related to Providers and acts as a
 *      model to transfer information between the frontend and the database.
 *
 *      Contains the identifying variables of the Provider and is used by the service
 *      layer for retrieval and updates.
 *      - "id": a unique identifier to differentiate between Providers. It is also the
 *           primary way of accessing specific Provider records. This is immutable.
 *           The number combination is created using the
 *           auto-increment feature in MySQL.
 *      - "firstName", "lastName", "phone", and "email" are inherited from Person.java.
 *      - "specialty": the area of expertise of the provider. For example, one provider
 *           may be identified as "Family Practice" while another is identified as a
 *           "NeuroSurgeon".
 *      - "address": the Provider's personal address.
 *
 *      Designer Note: As there are no optional fields, the standard constructor is used.
 *
 * @author Matthew Kiyono
 * @version 1.4
 * @since 10/20/2025
 *
 *****************************************************************************************/

package com.appointmentProject.backend.model;
import com.appointmentProject.backend.abstractmodel.Person;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "provider")
@DynamicUpdate
public class Provider extends Person {

    //non-inherited variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id",  nullable = false, unique = true)
    private int id;

    @NotNull
    @Column(name = "specialty", nullable = false)
    private String specialty;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    //Test Constructor ONLY!
    protected Provider() {}

    //constructor
    public Provider(int id, String firstName, String lastName, String phone, String email, String specialty, String address) {
        super(firstName, lastName, phone, email);
        this.id = id;
        this.specialty = specialty;
        this.address = address;
    }

    //getter methods
    public int getId() {return id;}
    public String getSpecialty() {return specialty;}
    public String getAddress() {return address;}

    //setter methods
    public void setId(int id) {this.id = id;}
    public void setSpecialty(String specialty) {this.specialty = specialty;}
    public void setAddress(String address) {this.address = address;}

    //toString
    public String toString(){
        return "Provider:" +
                "\nProvider id: " + id + "\n" +
                super.toString() +
                "\nSpecialty: " + specialty +
                "\nAddress: " + address + "\n";
    }
}