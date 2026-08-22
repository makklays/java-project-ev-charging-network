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
    private final String email;
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

