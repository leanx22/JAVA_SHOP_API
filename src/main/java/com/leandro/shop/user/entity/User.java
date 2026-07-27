package com.leandro.shop.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name="email",
            nullable = false,
            unique = true,
            length = 254
    )
    private String email;

    @Column(
            name = "first_name",
            nullable = false,
            length = 75
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 75
    )
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    private AccountStatus status;

    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false
    )
    private UserRole role;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    @Override
    public boolean isAccountNonLocked() {
        return this.status != AccountStatus.BANNED;
    }

    @Override
    public boolean isEnabled() {
        return this.status != AccountStatus.DELETED;
    }
}
