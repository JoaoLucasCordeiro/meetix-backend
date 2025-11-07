package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;
import com.meetix.meetix_api.domain.coupon.CouponResponseDTO; // <-- Importe o DTO
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
    public ResponseEntity<CouponResponseDTO> addCouponToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO data) {
        CouponResponseDTO coupon = couponService.addCouponToEvent(eventId, data);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @PostMapping("/apply")
    public ResponseEntity<CouponResponseDTO> applyCoupon(@Valid @RequestBody CouponApplyRequest request) {
        CouponResponseDTO coupon = couponService.applyCoupon(request);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/event/{eventId}/valid")
    public ResponseEntity<List<CouponResponseDTO>> getValidCoupons(@PathVariable UUID eventId) {
        List<CouponResponseDTO> coupons = couponService.consultValidCouponsForEvent(eventId);
        return ResponseEntity.ok(coupons);
    }
}