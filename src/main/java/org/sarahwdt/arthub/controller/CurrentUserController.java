package org.sarahwdt.arthub.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.dto.ChangePasswordRequest;
import org.sarahwdt.arthub.dto.RegistrationRequest;
import org.sarahwdt.arthub.dto.UserResponse;
import org.sarahwdt.arthub.service.CurrentUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CurrentUserController {
    private final CurrentUserService currentUserService;

    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    @PostMapping("/registration")
    public UserResponse registration(@Valid @RequestBody RegistrationRequest request) {
        return currentUserService.registration(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        currentUserService.changePassword(request);
    }
}
