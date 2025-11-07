package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;
import com.meetix.meetix_api.domain.coupon.CouponResponseDTO;
import java.util.List;
import java.util.UUID;

public interface CouponService {

    CouponResponseDTO addCouponToEvent(UUID eventId, CouponRequestDTO couponData);
    CouponResponseDTO applyCoupon(CouponApplyRequest request);
    List<CouponResponseDTO> consultValidCouponsForEvent(UUID eventId);
}