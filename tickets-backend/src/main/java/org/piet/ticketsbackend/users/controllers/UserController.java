package org.piet.ticketsbackend.users.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.jwt.JwtService;
import org.piet.ticketsbackend.users.dtos.ChangePasswordDto;
import org.piet.ticketsbackend.users.dtos.UserRequestDto;
import org.piet.ticketsbackend.users.dtos.UserDto;
import org.piet.ticketsbackend.users.entities.Role;
import org.piet.ticketsbackend.users.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("${app.api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    private Map<String, String> tokenToResponse(String token){
        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        return body;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRequestDto dto) throws AccessDeniedException {
        var user = userService.createUser(dto.getUsername(), dto.getPassword(), Role.PASSENGER);
        var token = jwtService.generateToken(user);
        return ResponseEntity.ok(tokenToResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody UserRequestDto dto){
        var token = userService.loginUser(dto.getUsername(), dto.getPassword());

        return ResponseEntity.ok(tokenToResponse(token));
    }

    @PatchMapping("/{id}/password/change")
    public ResponseEntity<Map<String, String>> changePassword(
            @PathVariable UUID id,
            @Valid @RequestBody ChangePasswordDto dto,
            Authentication authentication) throws AccessDeniedException {
        var reqAuthor = userService.getFromAuthentication(authentication);
        var token = userService.changePassword(id, dto.getNewPassword(), reqAuthor);

        return ResponseEntity.ok(tokenToResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserInfo(Authentication authentication){
        var user = userService.getFromAuthentication(authentication);

        return ResponseEntity.ok(UserDto.create(user));
    }
}
