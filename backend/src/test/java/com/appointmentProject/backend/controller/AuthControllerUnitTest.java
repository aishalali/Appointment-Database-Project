package com.appointmentProject.backend.controller;

import com.appointmentProject.backend.model.Account;
import com.appointmentProject.backend.model.Account.Authorization;
import com.appointmentProject.backend.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        Account admin = new Account();
        admin.setUsername("admin");
        admin.setPassword("adminpass");
        admin.setEmail("admin@test.com");
        admin.setUserType(Authorization.ADMIN);
        accountRepository.save(admin);
    }

    @Test
    void testValidLogin() throws Exception {
        mockMvc.perform(get("/api/auth/login")
                        .param("username", "admin")
                        .param("password", "adminpass"))
                .andExpect(status().isOk())
                .andExpect(content().string("ADMIN"));
    }

    @Test
    void testInvalidLogin() throws Exception {
        mockMvc.perform(get("/api/auth/login")
                        .param("username", "admin")
                        .param("password", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(content().string("INVALID"));
    }
}
