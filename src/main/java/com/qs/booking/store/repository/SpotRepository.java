package com.qs.booking.store.repository;

import com.qs.booking.store.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpotRepository extends JpaRepository<Spot, UUID> {
}
