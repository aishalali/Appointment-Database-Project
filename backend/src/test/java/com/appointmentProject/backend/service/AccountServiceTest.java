package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.model.Account.Authorization;
import com.appointmentProject.backend.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/***************************************************************************************************
 * AccountServiceTest.java
 *
 *      This file tests the AccountService class, ensuring that the correct repository methods
 *      are called, exceptions are thrown when appropriate, and the service returns the expected
 *      data. Mockito is used to mock the AccountRepository so that NO actual database calls
 *      are made. These are TRUE unit tests of the service layer.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/03/2025
 ***************************************************************************************************/
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accRepo;

    @InjectMocks
    private AccountService accService;

    private Account sampleAdmin;
    private Account sampleNurse;

    @BeforeEach
    void init() {
        sampleAdmin = new Account("adminUser", "pw", "admin@mail.com", Authorization.ADMIN);
        sampleNurse = new Account("nurseUser", "pw", "nurse@mail.com", Authorization.NURSE);
    }

    // 1. INSERT
    @Test
    void testAddAccount() {
        when(accRepo.save(sampleAdmin)).thenReturn(sampleAdmin);

        Account result = accService.addAccount(sampleAdmin);

        assertNotNull(result);
        assertEquals("adminUser", result.getUsername());
        verify(accRepo, times(1)).save(sampleAdmin);
    }

    // 2. UPDATE – happy path
    @Test
    void testUpdateAccount_Success() {
        Account updated = new Account("adminUser", "newPw", "newAdmin@mail.com", Authorization.ADMIN);

        when(accRepo.findById("adminUser")).thenReturn(Optional.of(sampleAdmin));
        when(accRepo.save(any(Account.class))).thenReturn(updated);

        Account result = accService.updateAccount(updated);

        assertEquals("newAdmin@mail.com", result.getEmail());
        assertEquals("newPw", result.getPassword());
        verify(accRepo, times(1)).findById("adminUser");
        verify(accRepo, times(1)).save(any(Account.class));
    }

    // 3. UPDATE – not found
    @Test
    void testUpdateAccount_NotFound() {
        Account updated = new Account("missingUser", "pw", "x@mail.com", Authorization.ADMIN);

        when(accRepo.findById("missingUser")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> accService.updateAccount(updated));
    }

    // 4. DELETE – success
    @Test
    void testDeleteAccount_Success() {
        when(accRepo.findById("adminUser")).thenReturn(Optional.of(sampleAdmin));

        accService.deleteAccountByUsername("adminUser");

        verify(accRepo, times(1)).findById("adminUser");
        verify(accRepo, times(1)).delete(sampleAdmin);
    }

    // 5. DELETE – not found
    @Test
    void testDeleteAccount_NotFound() {
        when(accRepo.findById("badUser")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> accService.deleteAccountByUsername("badUser"));
    }

    // 6. SELECT ALL
    @Test
    void testGetAllAccounts() {
        when(accRepo.findAll()).thenReturn(List.of(sampleAdmin, sampleNurse));

        List<Account> list = accService.getAllAccounts();

        assertEquals(2, list.size());
        verify(accRepo, times(1)).findAll();
    }

    // 7. SELECT BY USERNAME
    @Test
    void testGetByUsername() {
        when(accRepo.findByUsername("adminUser")).thenReturn(Optional.of(sampleAdmin));

        Optional<Account> result = accService.getByUsername("adminUser");

        assertTrue(result.isPresent());
        assertEquals("adminUser", result.get().getUsername());
        verify(accRepo, times(1)).findByUsername("adminUser");
    }
}
