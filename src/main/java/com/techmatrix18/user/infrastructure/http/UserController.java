package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.RegisterUserCommand;
import com.techmatrix18.user.application.command.UpdateProfileCommand;
import com.techmatrix18.user.application.port.in.RegisterUserUseCase;
import com.techmatrix18.user.application.port.in.UpdateProfileUseCase;
import com.techmatrix18.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase, UpdateProfileUseCase updateProfileUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.updateProfileUseCase = updateProfileUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserCommand command) {
        User user = registerUserUseCase.register(command);
        return new ResponseEntity<>(UserResponse.fromDomain(user), HttpStatus.CREATED);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UpdateProfileCommand command) {
        User updatedUser = updateProfileUseCase.updateProfile(command);
        return ResponseEntity.ok(UserResponse.fromDomain(updatedUser));
    }
}

