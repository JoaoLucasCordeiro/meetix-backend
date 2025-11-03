package com.meetix.meetix_api.domain.coupon;

public record CouponRequestDTO(String code, Integer discount, Long valid) {
}
