package com.japanApply.journal.controller;

import com.japanApply.journal.dto.AuthenticationRequestCreateDto;
import com.japanApply.journal.dto.AuthenticationRequestDto;
import com.japanApply.journal.model.RangType;
import com.japanApply.journal.model.User;
import com.japanApply.journal.security.JwtUtil;
import com.japanApply.journal.security.MyUserDetailsService;
import com.japanApply.journal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
import java.time.LocalDate;

@RestController
@Tag(name = "Auth", description = "endpoints for authentication")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final MyUserDetailsService userDetailsService;

    private final UserService userService;

    private final PasswordEncoder encoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            MyUserDetailsService userDetailsService,
            UserService userService,
            PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Operation(
            summary = "Endpoint for authentication"
    )
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody AuthenticationRequestDto authenticationRequestDto) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDto.email(),
                            authenticationRequestDto.password()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequestDto.email());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("accessToken", jwt));
    }

    @Operation(
            summary = "Endpoint to create account"
    )
    @PostMapping("/signup")
    public User registerUser(@RequestBody AuthenticationRequestCreateDto authenticationRequestCreateDto) {
        // -- Check if the username is already taken and throw an exception if it is
        userService.getByUsername(authenticationRequestCreateDto.email())
                .ifPresent(user -> {
                    throw new RuntimeException("Error: user already exists, please sign in");
                });

        // -- Create a new user's account
        User newUser = new User();
        newUser.setEmail(authenticationRequestCreateDto.email());
        String password = encoder.encode(authenticationRequestCreateDto.password());
        System.out.println(password);
        newUser.setPassword(password);
        newUser.setRangType(RangType.BASE); // Assign the rank to BASE by default
        newUser.setCountryOfOrigin(authenticationRequestCreateDto.countryOfOrigin());
        newUser.setPhone(authenticationRequestCreateDto.phone());
        newUser.setCreationDate(LocalDate.now()); // Add the user's creation date

        return userService.createUser(newUser)
                .orElseThrow(() -> new RuntimeException("Error occurred while adding user"));
    }
}
