/*****************************************************************************************
 * Person.java
 *
 *      This class is a parent class to many of the person related
 *      object fields. As a lot of variables are the same across
 *      models, it is more efficient to create a singular parent
 *      model to hold the duplicate data instead.
 *
 *      "first_person": the first name of the person
 *      "last_name": the last name of the person
 *      "phone" : preferred phone number of the person
 *      "email" : preferred email of the person
 *
 *      Depending on the Person subclass, phone and email could be
 *      optional; but nulls can still pass through the Parent class.
 *      - The import for NullString is applied to display nulls as "N/A".
 *
 * @author Matthew Kiyono
 * @version 1.2
 * @since 10/16/2025
 ****************************************************************************************/

package com.appointmentProject.backend.abstractmodel;
import com.appointmentProject.backend.util.NullString;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class Person {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    protected String lastName;

    @Column(name = "phone", nullable = false, length = 100)
    protected String phone;

    @Column(name = "email", nullable = false, length = 255)
    protected String email;


    //Test Constructor Only!
    protected Person() {}

    //Parent Constructor
    public Person(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }


    //common getters
    public String getFirstName() {return firstName;}
    public  String getLastName() {return lastName;}
    public String getPhone() {return phone;}
    public String getEmail() {return email;}

    //common setters
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setEmail(String email) {this.email = email;}

    //common toString
    public String toString(){
        return "First name: " + firstName +
                "\nLast name: " + lastName +
                "\nPhone: " + phone +
                "\nEmail: " + NullString.check(email);
    }

}
