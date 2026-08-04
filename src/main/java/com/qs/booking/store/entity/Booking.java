package com.qs.booking.store.entity;

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
@Table(name="app_booking")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Enumerated(EnumType.STRING)
    private BookingState state;

    @OneToOne(optional = false)
    @JoinColumn(name = "purchaser_id", referencedColumnName = "id")
    private Account purchaser;

    @OneToOne(optional = false)
    @JoinColumn(name="spot_id", referencedColumnName="id")
    private Spot spot;

    @Column(
            nullable = false,
            updatable = false,
            unique = true
    )
    private UUID idempotencyKey;

    private Instant processedAt;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}
