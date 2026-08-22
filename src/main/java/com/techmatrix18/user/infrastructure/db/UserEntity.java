package com.techmatrix18.user.infrastructure.db;

import com.techmatrix18.user.domain.BaseRole;
import com.techmatrix18.user.domain.Gender;
import com.techmatrix18.user.domain.User;
import com.techmatrix18.user.domain.UserStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * UserEntity
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 22.08.2026
 */

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "base_role", nullable = false)
    private BaseRole baseRole;

    @Column(length = 20)
    private String mobile;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private String gender;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserStatus status;

    private String password;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // --- Конструкторы ---

    public UserEntity() {
    }

    // --- Маппинг: Из чистого Домена в JPA Entity (для сохранения в БД) ---
    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.id = user.getId();
        entity.username = user.getUsername();
        entity.email = user.getEmail();
        entity.baseRole = user.getBaseRole();
        entity.mobile = user.getMobile();
        entity.nickname = user.getNickname();
        entity.gender = user.getGender() != null ? user.getGender().name() : null;
        entity.avatarUrl = user.getAvatarUrl();
        entity.birthDate = user.getBirthDate();
        entity.bio = user.getBio();
        entity.status = user.getStatus();
        entity.password = user.getPassword();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();
        return entity;
    }

    // --- Маппинг: Из JPA Entity в чистый Домен (для бизнес-логики) ---
    public User toDomain() {
        return new User(
                this.id,
                this.username,
                this.email,
                this.baseRole,
                this.mobile,
                this.nickname,
                this.gender != null ? Gender.valueOf(this.gender) : null,
                this.avatarUrl,
                this.birthDate,
                this.bio,
                this.status,
                this.password,
                this.createdAt,
                this.updatedAt
        );
    }

    // --- Геттеры и Сеттеры ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BaseRole getBaseRole() { return baseRole; }
    public void setBaseRole(BaseRole baseRole) { this.baseRole = baseRole; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}

