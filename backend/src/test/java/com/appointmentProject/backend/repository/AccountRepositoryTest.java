/*************************************************************************************************
 *  AccountRepositoryTest.java
 *
 *      The purpose of this test is to ensure that the AccountRepository executes queries
 *      correctly. @Autowired injects the repository and EntityManager managed by Spring.
 *
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/03/2025
 *************************************************************************************************/

package com.appointmentProject.backend.repository;

import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.model.Account.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account testAccount;

    @BeforeEach
    void setup() {
        testAccount = new Account();
        testAccount.setUsername("john123");
        testAccount.setPassword("password");
        testAccount.setEmail("john@example.com");
        testAccount.setUserType(Authorization.ADMIN);

        accountRepository.save(testAccount);
    }

    @Test
    void testFindByUsername() {
        Optional<Account> foundOpt = accountRepository.findByUsername("john123");

        assertTrue(foundOpt.isPresent());
        Account found = foundOpt.get();

        assertEquals("john@example.com", found.getEmail());
    }

    @Test
    void testFindByEmail() {
        Optional<Account> foundOpt = accountRepository.findByEmail("john@example.com");

        assertTrue(foundOpt.isPresent());
        Account found = foundOpt.get();

        assertEquals("john123", found.getUsername());
    }

    @Test
    void testFindByUsernameAndPassword() {
        Account found = accountRepository.findByUsernameAndPassword("john123", "password");
        assertNotNull(found);
        assertEquals(Authorization.ADMIN, found.getUserType());
    }

    @Test
    void testUpdateAccount() {
        Optional<Account> foundOpt = accountRepository.findByUsername("john123");
        assertTrue(foundOpt.isPresent());

        Account found = foundOpt.get();
        found.setPassword("newpass");

        accountRepository.save(found);

        Optional<Account> updatedOpt = accountRepository.findByUsername("john123");
        assertTrue(updatedOpt.isPresent());

        assertEquals("newpass", updatedOpt.get().getPassword());
    }

    @Test
    void testDeleteAccount() {
        accountRepository.deleteById("john123");

        Optional<Account> foundOpt = accountRepository.findByUsername("john123");
        assertTrue(foundOpt.isEmpty());
    }
}
