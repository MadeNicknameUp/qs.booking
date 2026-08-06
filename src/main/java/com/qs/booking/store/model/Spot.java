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
@Table(name="app_spot")
@Getter
@Setter
@NoArgsConstructor
public class Spot {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpotState state;

    private UUID eventId;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
