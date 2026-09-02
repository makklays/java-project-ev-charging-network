package com.techmatrix18.user.infrastructure.http;

import com.techmatrix18.building_blocks.infrastructure.interceptors.RequireIdempotency;
import com.techmatrix18.user.application.command.RegisterUserCommand;
import com.techmatrix18.user.application.command.UpdateUserCommand;
import com.techmatrix18.user.application.port.in.*;
import com.techmatrix18.user.domain.User;
import jakarta.validation.Valid;
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
    private final UpdateUserUseCase updateUserUseCase;
    private final ActivateUserUseCase activateUserUseCase;
    private final BlockUserUseCase blockUserUseCase;
    private final ChangeEmailUseCase changeEmailUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final UpdateAvatarUseCase updateAvatarUseCase;
    private final UpdateMobileUseCase updateMobileUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase, UpdateUserUseCase updateUserUseCase,
                          ActivateUserUseCase activateUserUseCase, BlockUserUseCase blockUserUseCase,
                          ChangeEmailUseCase changeEmailUseCase, ChangePasswordUseCase changePasswordUseCase,
                          ChangeUserRoleUseCase changeUserRoleUseCase, DeleteUserUseCase deleteUserUseCase,
                          ResetPasswordUseCase resetPasswordUseCase, UpdateAvatarUseCase updateAvatarUseCase,
                          UpdateMobileUseCase updateMobileUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.activateUserUseCase = activateUserUseCase;
        this.blockUserUseCase = blockUserUseCase;
        this.changeEmailUseCase = changeEmailUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.changeUserRoleUseCase = changeUserRoleUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.updateAvatarUseCase = updateAvatarUseCase;
        this.updateMobileUseCase = updateMobileUseCase;
    }

    @PostMapping("/register")
    @RequireIdempotency
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserCommand command = request.toCommand();
        User user = registerUserUseCase.register(command);
        return new ResponseEntity<>(UserResponse.fromDomain(user), HttpStatus.CREATED);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        UpdateUserCommand command = request.toCommand();
        User updatedUser = updateUserUseCase.updateUser(command);
        return ResponseEntity.ok(UserResponse.fromDomain(updatedUser));
    }

    @PutMapping("/users/activate")
    public ResponseEntity<Void> activate(@Valid @RequestBody ActivateUserRequest request) {
        // Валидация @NotNull сработала -> маппим в команду -> передаем в Use Case
        activateUserUseCase.activateUser(request.toCommand());
        return ResponseEntity.noContent().build(); // Возвращаем 204 No Content
    }

    @PutMapping("/users/block")
    public ResponseEntity<Void> block(@Valid @RequestBody BlockUserRequest request) {
        // Входящий BlockUserRequest проверяет @NotNull и @NotBlank
        blockUserUseCase.blockUser(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    // Смена адреса электронной почты
    @PutMapping("/change-email")
    public ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailRequest request) {
        // Валидируются @NotNull, @NotBlank и формат @Email
        changeEmailUseCase.changeEmail(request.toCommand());
        return ResponseEntity.noContent().build();
    }

    // Смена пароля из личного кабинета
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // Запускает валидацию @ValidPassword для нового пароля
        changePasswordUseCase.changePassword(request.toCommand());
        return ResponseEntity.noContent().build();
    }

    // Смена роли пользователя администратором
    @PutMapping("/change-role")
    public ResponseEntity<Void> changeUserRole(@Valid @RequestBody ChangeUserRoleRequest request) {
        // Валидирует @NotNull для userId и newRole
        changeUserRoleUseCase.changeUserRole(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Мягкое удаление пользователя
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody DeleteUserRequest request) {
        // Валидирует @NotNull для userId на входе
        deleteUserUseCase.deleteUser(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Сброс забытого пароля по временному токену
    @PostMapping("/reset-password")
    @RequireIdempotency
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // Запускает валидацию @NotBlank для токена и кастомную @ValidPassword для пароля
        resetPasswordUseCase.resetPassword(request.toCommand());
        return ResponseEntity.ok().build(); // HTTP 200 OK
    }

    // Обновление ссылки на аватар профиля
    @PatchMapping("/avatar")
    public ResponseEntity<Void> updateAvatar(@Valid @RequestBody UpdateAvatarRequest request) {
        // Валидирует @NotNull для ID и проверяет формат ссылки через @URL
        updateAvatarUseCase.updateAvatar(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // Обновление номера мобильного телефона
    @PatchMapping("/mobile")
    public ResponseEntity<Void> updateMobile(@Valid @RequestBody UpdateMobileRequest request) {
        // Здесь на входе автоматически отрабатывает кастомный @ValidMobile из инфраструктуры
        updateMobileUseCase.updateMobile(request.toCommand());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}

