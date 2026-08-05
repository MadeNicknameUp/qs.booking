package com.qs.booking.store.model;

import jakarta.persistence.*;
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
    private String email;

    private String firstName;

    private String lastName;

    @Column(nullable=false)
    private String nickname;

    @Column(nullable=false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    private AccountRole role;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
