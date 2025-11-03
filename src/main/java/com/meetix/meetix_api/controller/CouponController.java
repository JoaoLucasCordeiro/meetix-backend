package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.coupon.Coupon;
import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;
import com.meetix.meetix_api.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO data) {
        Coupon coupon = couponService.addCouponToEvent(eventId, data);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @PostMapping("/apply")
    public ResponseEntity<Coupon> applyCoupon(@Valid @RequestBody CouponApplyRequest request) {
        Coupon coupon = couponService.applyCoupon(request);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/event/{eventId}/valid")
    public ResponseEntity<List<Coupon>> getValidCoupons(@PathVariable UUID eventId) {
        List<Coupon> coupons = couponService.consultValidCouponsForEvent(eventId);
        return ResponseEntity.ok(coupons);
    }
}