package com.appointmentProject.backend.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.model.Account.Authorization;


public class AccountTest {
    /***************************************************************************
     *   AccountTest.java
     *
     *          This will test the Construction, getters, setters, and
     *          toString of the Account model.
     *          Prioritizes data integrity over logic in these tests.
     *
     * @author Matthew Kiyono
     * @version 1.0
     * @since 11/05/2025
     ****************************************************************************/

    @Test
    void testGetterAndSetter() {
        //test Objects
        String name1 = "workerholic2", name2 = "channelIV9";
        String pass1 = "password", pass2 = "admin";
        String email1 = "zelda@uwm.edu", email2 = "pikachu@gmail.com";
        Authorization aut1 = Account.Authorization.NURSE, aut2 = Account.Authorization.PROVIDER;

        Account acc = new Account(name1, pass1, email1, aut1);

        //getter tests
        assertAll(
                () -> assertEquals(name1, acc.getUsername()),
                () -> assertEquals(pass1, acc.getPassword()),
                () -> assertEquals(email1, acc.getEmail()),
                () -> assertEquals(aut1, acc.getUserType())
        );

        //setter tests
        acc.setUsername(name2);
        acc.setPassword(pass2);
        acc.setEmail(email2);
        acc.setUserType(aut2);

        assertAll(
                () -> assertEquals(name2, acc.getUsername()),
                () -> assertEquals(pass2, acc.getPassword()),
                () -> assertEquals(email2, acc.getEmail()),
                () -> assertEquals(aut2, acc.getUserType())
        );

    }

    @Test
    void testToString() {
        //Test Object
        Account acc = new Account("mudkip123", "secret",
                "mk@nintendo.com", Account.Authorization.ADMIN);

        String expected =
                "User Account: " +
                "\nUsername: mudkip123" +
                "\nPassword: secret" +
                "\nEmail: mk@nintendo.com" +
                "\nUserType: ADMIN" +
                "\n";

        assertEquals(expected, acc.toString());
    }




}
