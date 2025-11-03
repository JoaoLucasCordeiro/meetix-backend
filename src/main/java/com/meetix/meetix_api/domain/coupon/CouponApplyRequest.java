package com.meetix.meetix_api.domain.coupon;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CouponApplyRequest(
        @NotBlank String code,
        @NotNull UUID eventId
) {}
