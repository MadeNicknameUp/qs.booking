package com.qs.booking.store.repository;

import com.qs.booking.store.entity.Account;
import com.qs.booking.store.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Page<Booking> findAllByAccount(Account fetchedAccount, Pageable pageable);
}
