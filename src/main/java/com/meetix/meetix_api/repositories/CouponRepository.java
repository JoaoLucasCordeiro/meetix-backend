package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Event, UUID> {

}
