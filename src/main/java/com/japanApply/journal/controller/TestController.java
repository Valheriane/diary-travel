package com.japanApply.journal.controller;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/auth/test")
    public String testAuthEndpoint() {
        return "Endpoint /api/auth/test fonctionne !";
    }

    @GetMapping("/users/test")
    public String testUsersEndpoint() {
        return "Endpoint /api/users/test fonctionne !";
    }
}
