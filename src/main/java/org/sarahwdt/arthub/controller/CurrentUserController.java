package org.sarahwdt.arthub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.dto.ChangePasswordRequest;
import org.sarahwdt.arthub.service.CurrentUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@PreAuthorize("isFullyAuthenticated()")
@RequiredArgsConstructor
public class CurrentUserController {
    private final CurrentUserService currentUserService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request, BindingResult bindingResult) {
        currentUserService.changePassword(request, bindingResult);
    }
}
