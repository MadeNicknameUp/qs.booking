package com.qs.booking.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="app_account_role")
@Getter
@Setter
public class AccountRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @JoinColumn(nullable=false)
    private String title;

    @OneToOne
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;

    @OneToOne
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Account account;
}
