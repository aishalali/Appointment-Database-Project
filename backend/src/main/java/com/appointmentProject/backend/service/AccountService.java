package com.appointmentProject.backend.service;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/******************************************************************************************************************
 * AccountService.java
 *
 *      This service class calls on the AccountRepository interface to communicate with the database.
 *
 *      The types of queries that can be made:
 *      - Insert
 *      - Delete
 *      - Update
 *      - Select (by: all, username)
 *
 *      Later updates can include format checks (valid email, password constraints, etc.).
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/03/2025
 ******************************************************************************************************************/

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accRepo;

    // CREATE
    public Account addAccount(Account account) {
        return accRepo.save(account);
    }

    // READ ALL
    public List<Account> getAllAccounts() {
        return accRepo.findAll();
    }

    // READ ONE
    public Optional<Account> getByUsername(String username) {
        return accRepo.findByUsername(username);
    }


    // LOGIN LOOKUP (new version: look up by username, then compare password in Java)
    public Account findByUsernameAndPassword(String username, String password) {
        Optional<Account> opt = accRepo.findByUsername(username);

        if (opt.isEmpty()) {
            return null; // no such user
        }

        Account acc = opt.get();

        // Plain-text comparison (matches how passwords are stored right now)
        if (!acc.getPassword().equals(password)) {
            return null; // wrong password
        }

        return acc;
    }


    //Checks if Email exists
    public boolean emailExists(String email) {
        return accRepo.findByEmail(email).isPresent();
    }

    //Updates the Email
    public boolean updateEmail(String username, String newEmail) {

        // email already used?
        if (accRepo.findByEmail(newEmail).isPresent()) {
            return false;
        }

        Optional<Account> opt = accRepo.findByUsername(username);
        if (opt.isEmpty()) {
            return false;
        }

        Account acc = opt.get();
        acc.setEmail(newEmail);
        accRepo.save(acc);
        return true;
    }


    // UPDATE
    public Account updateAccount(Account account) {
        Optional<Account> existing = accRepo.findByUsername(account.getUsername());

        if (existing.isEmpty()) {
            throw new RecordNotFoundException("Account with username " + account.getUsername() + " does not exist.");
        }

        return accRepo.save(account);
    }

    // DELETE
    public void deleteAccountByUsername(String username) {
        if (!accRepo.existsById(username)) {
            throw new RecordNotFoundException("Cannot delete. No account found with username " + username);
        }
        accRepo.deleteById(username);
    }

    //Update Password
    public boolean updatePassword(String username, String oldPassword, String newPassword) {

        // 1. Lookup user
        Optional<Account> opt = accRepo.findByUsername(username);
        if (opt.isEmpty()) {
            return false; // no such user
        }

        Account acc = opt.get();

        // 2. Verify old password matches
        if (!acc.getPassword().equals(oldPassword)) {
            return false; // wrong old password
        }

        // 3. Update password
        acc.setPassword(newPassword);
        accRepo.save(acc);
        return true;
    }

    // ADMIN: Update user email + role
    public String updateUserFields(String username, String newEmail, String newRole) {
        Optional<Account> opt = accRepo.findByUsername(username);
        if (opt.isEmpty()) {
            return "USER_NOT_FOUND";
        }

        Account acc = opt.get();

        acc.setEmail(newEmail);
        acc.setUserType(Account.Authorization.valueOf(newRole));

        accRepo.save(acc);
        return "SUCCESS";
    }
    public boolean safeDeleteUser(String username) {

        Optional<Account> existing = accRepo.findByUsername(username);

        if (existing.isEmpty()) {
            return false;   // User does not exist
        }

        accRepo.delete(existing.get());
        return true;
    }





}
