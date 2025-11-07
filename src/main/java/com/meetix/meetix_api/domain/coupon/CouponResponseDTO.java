package com.meetix.meetix_api.domain.coupon;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponseDTO(
        UUID id,
        String code,
        Integer discount,
        LocalDateTime valid,
        UUID eventId
) {}