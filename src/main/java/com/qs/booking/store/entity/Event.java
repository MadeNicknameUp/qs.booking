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

    @OneToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private Account author;

    @Column(nullable = false)
    private Integer spotsAmount;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;
}
