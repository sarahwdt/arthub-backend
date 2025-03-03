package org.sarahwdt.arthub.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.dto.AccessTokenResponse;
import org.sarahwdt.arthub.dto.LoginRequest;
import org.sarahwdt.arthub.dto.RefreshRequest;
import org.sarahwdt.arthub.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@PermitAll
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AccessTokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    public AccessTokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }
}
