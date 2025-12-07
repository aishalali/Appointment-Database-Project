/***************************************************************************************
 *   AccountControllerUnitTest.java
 *
 *      A unit test determining if the controller test is functioning.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/03/2025
 ***************************************************************************************/
package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.model.Account.Authorization;
import com.appointmentProject.backend.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account sampleAccount;

    @BeforeEach
    void setup() {
        sampleAccount = new Account();
        sampleAccount.setUsername("matthew");
        sampleAccount.setPassword("pass123");
        sampleAccount.setEmail("matthew@test.com");
        sampleAccount.setUserType(Authorization.ADMIN);
    }

    @Test
    void testAddAccount() throws Exception {
        when(accountService.addAccount(any(Account.class))).thenReturn(sampleAccount);

        mockMvc.perform(post("/account/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("matthew"))
                .andExpect(jsonPath("$.email").value("matthew@test.com"))
                .andExpect(jsonPath("$.userType").value("ADMIN"));
    }

    @Test
    void testGetAll() throws Exception {
        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(sampleAccount));

        mockMvc.perform(get("/account/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("matthew"));
    }

    @Test
    void testGetByUsername() throws Exception {
        when(accountService.getByUsername("matthew")).thenReturn(Optional.of(sampleAccount));

        mockMvc.perform(get("/account/matthew"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("matthew@test.com"));
    }

    @Test
    void testUpdateAccount() throws Exception {
        when(accountService.updateAccount(any(Account.class))).thenReturn(sampleAccount);

        mockMvc.perform(put("/account/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("matthew"));
    }

    @Test
    void testDeleteAccount() throws Exception {
        Mockito.doNothing().when(accountService).deleteAccountByUsername("matthew");

        mockMvc.perform(delete("/account/delete/matthew"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account removed successfully."));
    }
}
