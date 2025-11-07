package com.meetix.meetix_api.repositories;
import com.meetix.meetix_api.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    List<Coupon> findByEventIdAndValidAfter(UUID eventId, LocalDateTime currentDate);

    Optional<Coupon> findByCode(String code);
}