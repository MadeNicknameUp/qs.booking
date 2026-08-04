package com.qs.booking.store.repository;

import com.qs.booking.store.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e WHERE e.startingDate > CURRENT_TIMESTAMP")
    Page<Event> findAllUpcomingEvents(Pageable pageable);

}
