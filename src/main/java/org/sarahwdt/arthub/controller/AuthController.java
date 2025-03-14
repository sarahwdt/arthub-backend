package org.sarahwdt.arthub.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.dto.*;
import org.sarahwdt.arthub.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@PermitAll
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AccessTokenResponse login(@Valid @RequestBody LoginRequest request, BindingResult bindingResult) {
        return authService.login(request, bindingResult);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    public AccessTokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public UserResponse registration(@Valid @RequestBody RegistrationRequest request, BindingResult bindingResult) {
        return authService.registration(request, bindingResult);
    }
}
