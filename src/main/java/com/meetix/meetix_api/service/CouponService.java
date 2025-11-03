package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.coupon.Coupon;
import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;

import java.util.List;
import java.util.UUID;

public interface CouponService {

    Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponData);

    Coupon applyCoupon(CouponApplyRequest request);

    List<Coupon> consultValidCouponsForEvent(UUID eventId);
}