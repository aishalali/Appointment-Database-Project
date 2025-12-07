/******************************************************************************
 * Account.java
 *
 *  *****THE TABLE NAME IT WILL REFERENCE WILL BE CALLED USER_ACCOUNT.
 *
 *     Represents an Account entity from the database in object
 *     format for transferring from the database to the frontend.
 *
 *     Contains the identifying variables of Accounts and is used by the
 *     service layer for retrieval and updates.
 *     - "username": the  unique username of the user.
 *     - "password": the password to access the user's account
 *     - "email": the email that is connected to the user's account
 *     - "user_type": the user's authority level that gives them access to certain
 *              privileges.
 *              Types:
 *                  - Admin
 *                  - Provider
 *                  - Nurse
 *                  - Receptionist
 *
 * @author Matthew Kiyono
 * @version 1.1
 * @since 10/29/2025
 ********************************************************************************/

package com.appointmentProject.backend.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "useraccount")
@DynamicUpdate
public class Account {

    // --------------------------------------------------------
    // ENUM (Renamed Properly)
    // --------------------------------------------------------
    public enum Authorization {
        ADMIN,
        PROVIDER,
        NURSE,
        RECEPTIONIST
    }

    // --------------------------------------------------------
    // FIELDS
    // --------------------------------------------------------

    @Id
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private Authorization userType;

    // --------------------------------------------------------
    // CONSTRUCTORS
    // --------------------------------------------------------

    public Account() {}

    public Account(String username, String password, String email, Authorization userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    // --------------------------------------------------------
    // GETTERS
    // --------------------------------------------------------

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Authorization getUserType() {
        return userType;
    }

    // --------------------------------------------------------
    // SETTERS
    // --------------------------------------------------------

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(Authorization userType) {
        this.userType = userType;
    }

    // --------------------------------------------------------
    // TO STRING
    // --------------------------------------------------------

    @Override
    public String toString() {
        return "User Account:" +
                "\nUsername: " + username +
                "\nPassword: " + password +
                "\nEmail: " + email +
                "\nUserType: " + userType +
                "\n";
    }
}
