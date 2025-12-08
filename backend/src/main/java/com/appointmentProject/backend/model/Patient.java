/*********************************************************************************************
 * Patient.java
 *
 *      This is the blueprint for storing information related to patients and acts as a
 *      model to transfer information between frontend and the database.
 *
 *      Contains the identifying variables of the Patient and is used by the service layer
 *      for retrieval and updates.
 *      - "id": the unique identifier to differentiate between Patients. It is also the
 *             primary way of accessing specific Patient records. This is immutable.
 *             The number combination is created from the
 *             auto-increment feature from MySQL.
 *      - "firstName", "lastName", "phone", and (Optional) "email"  are inherited from
 *              Person.java.
 *      - "DoB": the patient's date of birth.
 *      - "gender": (Optional) the patient's gender identity and expression
 *      - "age": patient's age, can be 0 if they are only a few months old.
 *      - "weight": patient's weight in pounds in the format of a double.
 *      - "height": patient's height in feet in the format of a double.
 *      - "insuranceId": the patient's insurance provider. Can be null if they do not have
 *              one. The id must have a "IN" lead before the number sequence. (Optional)
 *      - "emergencyContactId": the patient's emergency contact. Can be null if they do not
 *              have one. The id must start with "EC" before the number sequence. (Optional)
 *
 *      Designer Note: As email, gender, insuranceId, and emergencyContactId are optional,
 *              the builder pattern constructor is used over the traditional constructor.
 *              - In addition, the import of LocalDate is used for defining the Date of Birth.
 *              - Import for NullString applied for displaying nulls as "N/A"
 *
 * @author Matthew Kiyono
 * @version 1.3
 * @since 10/20/2025
 *
 ********************************************************************************************/
package com.appointmentProject.backend.model;

import com.appointmentProject.backend.abstractmodel.Person;
import com.appointmentProject.backend.util.NullString;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Table(name = "patient")
@AttributeOverride(name = "email", column = @Column(name = "email", nullable = true))
@DynamicUpdate
public class Patient extends Person {

    //required variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @NotNull
    @Column(name = "DoB", nullable = false, columnDefinition = "DATE(0)")
    private LocalDate DoB;

    @NotNull
    @Column(name = "age", nullable = false)
    private int age;

    @NotNull
    @Column(name = "weight", nullable = false)
    private double weight;

    @NotNull
    @Column(name = "height", nullable = false)
    private double height;

    //optional variables
    @Column(name = "gender")
    private String gender;

    // assuming you already aligned these column names with SQL (e.g. insurance_id, emergency_contact_id)
    @Column(name = "insurance_id")
    private Integer insuranceId;

    @Column(name = "emergency_contact_id")
    private Integer emergencyContactId;

    //Test Constructor ONLY!
    protected Patient() {
        super();
    }

    //private constructor used only by builder
    private Patient(Builder builder) {
        // Person fields
        super(builder.firstName, builder.lastName, builder.phone, builder.email);

        // id is NOT set by builder – JPA will generate it
        this.DoB = builder.DoB;
        this.age = builder.age;
        this.weight = builder.weight;
        this.height = builder.height;
        this.gender = builder.gender;
        this.insuranceId = builder.insuranceId;
        this.emergencyContactId = builder.emergencyContactId;
    }

    //getter methods
    public int getId() {
        return id;
    }

    public LocalDate getDoB() {
        return DoB;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getGender() {
        return gender;
    }

    public Integer getInsuranceId() {
        return insuranceId;
    }

    public Integer getEmergencyContactId() {
        return emergencyContactId;
    }

    //setter methods
    public void setId(int id) { this.id = id; }
    public void setDoB(LocalDate DoB) { this.DoB = DoB; }
    public void setAge(int age) { this.age = age; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }
    public void setGender(String gender) { this.gender = gender; }
    public void setInsuranceId(Integer inId) { this.insuranceId = inId; }
    public void setEmergencyContactId(Integer ecId) { this.emergencyContactId = ecId; }

    //toString
    public String toString() {
        return "Patient:\n" +
                "Patient ID: " + id + "\n" +
                super.toString() +
                "\nDoB: " + DoB + "\n" +
                "Age: " + age + "\n" +
                "Weight: " + weight + "\n" +
                "Height: " + height + "\n" +
                "Gender: " + NullString.check(gender) + "\n" +
                "Insurance Id: " + NullString.check(insuranceId) + "\n" +
                "Emergency Contact ID: " + NullString.check(emergencyContactId) +
                "\n";
    }

    public static class Builder {
        //required (no id here – DB handles it)
        private String firstName;
        private String lastName;
        private LocalDate DoB;
        private int age;
        private double weight;
        private double height;
        private String phone;

        //optional
        private String gender;
        private String email;
        private Integer insuranceId;
        private Integer emergencyContactId;

        //constructor that utilizes only the required variables.
        public Builder(String firstName,
                       String lastName,
                       LocalDate DoB,
                       int age,
                       double weight,
                       double height,
                       String phone) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.DoB = DoB;
            this.age = age;
            this.weight = weight;
            this.height = height;
        }

        //optional variables
        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder insuranceId(Integer inId) {
            this.insuranceId = inId;
            return this;
        }

        public Builder emergencyContactId(Integer ecId) {
            this.emergencyContactId = ecId;
            return this;
        }

        //combining provided variables
        public Patient build() {
            return new Patient(this);
        }
    }
}
