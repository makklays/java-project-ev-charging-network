package com.techmatrix18.user.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * User
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

public class User {
    private final Long id;
    private String username;
    private String email;
    private BaseRole baseRole;
    private String mobile;
    private String nickname;
    private Gender gender;
    private String avatarUrl;
    private LocalDate birthDate;
    private String bio;
    private UserStatus status;
    private String password;
    private final ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    // Конструктор для создания нового пользователя (без ID и дат)
    public User(String username, String email, BaseRole baseRole, String mobile,
                String nickname, Gender gender, String avatarUrl, LocalDate birthDate,
                String bio, UserStatus status, String password) {
        this.id = null;
        this.username = username;
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.baseRole = baseRole != null ? baseRole : BaseRole.USER;
        this.mobile = mobile;
        this.nickname = Objects.requireNonNull(nickname, "Nickname cannot be null");
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.birthDate = birthDate;
        this.bio = bio;
        this.status = status != null ? status : UserStatus.DRIVER;
        this.password = password;
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    // Конструктор для восстановления из БД (используется в репозитории/маппере)
    public User(Long id, String username, String email, BaseRole baseRole, String mobile,
                String nickname, Gender gender, String avatarUrl, LocalDate birthDate,
                String bio, UserStatus status, String password,
                ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.baseRole = baseRole;
        this.mobile = mobile;
        this.nickname = nickname;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.birthDate = birthDate;
        this.bio = bio;
        this.status = status;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Бизнес-логика (Пример доменных методов) ---

    public void updateProfile(String nickname, String bio, String avatarUrl) {
        this.nickname = Objects.requireNonNull(nickname, "Nickname cannot be empty");
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.updatedAt = ZonedDateTime.now();
    }

    public void changeStatus(UserStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus);
        this.updatedAt = ZonedDateTime.now();
    }

    // Бизнес-логика активации пользователя. Переводит аккаунт в активное состояние.
    public void activate() {
        // Защита: если пользователь уже активен, генерируем доменное исключение
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User account is already active");
        }

        // Меняем состояние
        this.status = UserStatus.ACTIVE;

        // Фиксируем время изменения
        this.updatedAt = ZonedDateTime.now();
    }

    // Блокировка пользователя
    public void block(String reason) {
        if (this.status == UserStatus.BLOCKED) {
            throw new IllegalStateException("User account is already blocked");
        }
        this.status = UserStatus.BLOCKED;
        this.updatedAt = ZonedDateTime.now();
    }

    // Мягкое удаление пользователя
    public void delete() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("User account is already deleted");
        }
        this.status = UserStatus.DELETED;
        this.updatedAt = ZonedDateTime.now();
    }

    // Изменение email пользователя.
    public void changeEmail(String newEmail) {
        Objects.requireNonNull(newEmail, "New email cannot be null");

        // Защита: новый email не должен совпадать с текущим
        if (newEmail.equalsIgnoreCase(this.email)) {
            throw new IllegalStateException("New email must be different from the current one");
        }

        this.email = newEmail;
        this.updatedAt = java.time.ZonedDateTime.now();
    }

    // Изменение пароля пользователя.
    public void changePassword(String hashedNewPassword) {
        Objects.requireNonNull(hashedNewPassword, "Hashed password cannot be null");

        this.password = hashedNewPassword;
        this.updatedAt = java.time.ZonedDateTime.now();
    }

    // Смена роли пользователя.
    public void changeRole(BaseRole newRole) {
        Objects.requireNonNull(newRole, "New role cannot be null");

        if (this.baseRole == newRole) {
            throw new IllegalStateException("User already has the " + newRole + " role");
        }

        this.baseRole = newRole;
        this.updatedAt = java.time.ZonedDateTime.now();
    }

    // Мягкое удаление пользователя. Переводит аккаунт в статус DELETED.
    /*public void delete() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("User account is already deleted");
        }
        this.status = UserStatus.DELETED;
        this.updatedAt = java.time.ZonedDateTime.now();
    }*/

    // Обновление ссылки на аватар пользователя.
    public void updateAvatar(String avatarUrl) {
        java.util.Objects.requireNonNull(avatarUrl, "Avatar URL cannot be null");

        this.avatarUrl = avatarUrl;
        this.updatedAt = java.time.ZonedDateTime.now();
    }

    // Обновление номера телефона пользователя.
    public void updateMobile(String newMobile) {
        java.util.Objects.requireNonNull(newMobile, "Mobile number cannot be null");

        if (newMobile.equals(this.mobile)) {
            throw new IllegalStateException("New mobile number must be different from the current one");
        }

        this.mobile = newMobile;
        this.updatedAt = java.time.ZonedDateTime.now();
    }

    // --- Геттеры ---

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public BaseRole getBaseRole() { return baseRole; }
    public String getMobile() { return mobile; }
    public String getNickname() { return nickname; }
    public Gender getGender() { return gender; }
    public String getAvatarUrl() { return avatarUrl; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getBio() { return bio; }
    public UserStatus getStatus() { return status; }
    public String getPassword() { return password; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}

