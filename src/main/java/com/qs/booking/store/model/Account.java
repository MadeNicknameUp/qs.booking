package com.qs.booking.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="app_account")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String profilePictureUrl;

    @Column(nullable=false, unique=true)
    @NotBlank(message="Email is mandatory")
    private String email;

    private String firstName;

    private String lastName;

    @Column(nullable=false)
    @NotBlank(message="Nickname is mandatory")
    private String nickname;

    @Column(nullable=false)
    @NotBlank(message="Password is mandatory")
    private String password;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    private AccountRole role;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
