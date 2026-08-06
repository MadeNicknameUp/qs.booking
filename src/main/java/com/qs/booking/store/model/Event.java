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
@Table(name="app_event")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String pictureUrl;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Instant startingDate;

    @Column(nullable = false)
    private Instant endingDate;

    @Column(nullable = false, updatable = false)
    private UUID authorId;

    @Column(nullable = false)
    private Integer spotsAmount;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;
}
