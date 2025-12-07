package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.exception.RecordNotFoundException;
import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/********************************************************************************************************
 * AccountController.java
 *
 *      This controller defines the API endpoints that the frontend (JavaFX client, or any HTTP client)
 *      will call for CRUD operations on Accounts.
 *
 *      Responsibilities:
 *          - Accept HTTP requests
 *          - Delegate the business logic to AccountService
 *          - Return the service’s results as HTTP responses
 *
 * @author Matthew Kiyono
 * @since 12/3/2025
 * @version 1.0
 ********************************************************************************************************/
@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    AccountService accService;

    // 1. Create Account
    @PostMapping("/add")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        Account created = accService.addAccount(account);
        return ResponseEntity.ok(created);
    }

    // 2. Get All Accounts
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> list = accService.getAllAccounts();
        return ResponseEntity.ok(list);
    }

    // 3. Get By Username
    @GetMapping("/{username}")
    public ResponseEntity<Account> getByUsername(@PathVariable("username") String username) {
        return accService.getByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new RecordNotFoundException(
                                "Account with username " + username + " was not found."
                        )
                );
    }

    // 4. Update Account
    @PutMapping("/update")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        Account updated = accService.updateAccount(account);
        return ResponseEntity.ok(updated);
    }

    // 5. Delete Account
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteAccount(@PathVariable("username") String username) {

        try {
            if (username.equals("admin")) {
                return ResponseEntity.status(403).body("ADMIN_CANNOT_BE_DELETED");
            }

            boolean deleted = accService.safeDeleteUser(username);

            if (deleted) {
                return ResponseEntity.ok("DELETED");
            } else {
                return ResponseEntity.status(404).body("NOT_FOUND");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("ERROR");
        }
    }




    // 6. Check Email
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam("email") String email) {
        return accService.emailExists(email);
    }

    //7. Update Email
    @PutMapping("/update-email")
    public boolean updateEmail(
            @RequestParam("username") String username,
            @RequestParam("newEmail") String newEmail
    ) {
        return accService.updateEmail(username, newEmail);
    }

    //Update Password
    @PutMapping("/update-password")
    public boolean updatePassword(
            @RequestParam("username") String username,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword
    ) {
        return accService.updatePassword(username, oldPassword, newPassword);
    }

// 4b. Update user email + role (Admin Manage Users screen)
    @PutMapping({"/admin-update", "/adminUpdateUser"})   // <-- supports BOTH URLs
    public String adminUpdateUser(
            @RequestParam("username") String username,
            @RequestParam("email") String newEmail,
            @RequestParam("role") String newRole
    ) {
        return accService.updateUserFields(username, newEmail, newRole);
    }





}
