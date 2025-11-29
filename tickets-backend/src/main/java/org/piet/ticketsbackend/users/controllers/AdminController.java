package org.piet.ticketsbackend.users.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.users.dtos.UserDto;
import org.piet.ticketsbackend.users.dtos.UserRequestDto;
import org.piet.ticketsbackend.users.entities.Role;
import org.piet.ticketsbackend.users.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.prefix}/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping("/users/create")
    public ResponseEntity<UserDto> createAdmin(@Valid @RequestBody UserRequestDto dto, Authentication authentication) throws AccessDeniedException {
        var reqAuthor = userService.getFromAuthentication(authentication);
        var user = userService.createUser(dto.getUsername(), dto.getPassword(), Role.ADMIN, reqAuthor);

        return ResponseEntity.ok(UserDto.create(user));
    }
}
