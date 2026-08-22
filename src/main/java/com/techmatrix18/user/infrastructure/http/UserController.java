package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.user.application.command.RegisterUserCommand;
import com.techmatrix18.user.application.port.in.RegisterUserUseCase;
import com.techmatrix18.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Внедряем интерфейс Use Case, а не сам сервис напрямую
    private final RegisterUserUseCase registerUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserCommand command) {
        User user = registerUserUseCase.register(command);
        return new ResponseEntity<>(UserResponse.fromDomain(user), HttpStatus.CREATED);
    }
}

