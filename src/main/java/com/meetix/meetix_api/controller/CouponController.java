package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;
import com.meetix.meetix_api.domain.coupon.CouponResponseDTO;
import com.meetix.meetix_api.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
@Tag(name = "Cupons", description = "Gerenciamento de cupons de desconto para eventos")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/event/{eventId}")
    @Operation(summary = "Adicionar cupom a evento", description = "Cria um novo cupom de desconto para um evento. Apenas criador ou admins podem criar.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CouponResponseDTO> addCouponToEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody CouponRequestDTO data,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        CouponResponseDTO coupon = couponService.addCouponToEvent(eventId, data, userId);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @PostMapping("/apply")
    @Operation(summary = "Aplicar cupom", description = "Valida e aplica um cupom de desconto a um evento")
    public ResponseEntity<CouponResponseDTO> applyCoupon(@Valid @RequestBody CouponApplyRequest request) {
        CouponResponseDTO coupon = couponService.applyCoupon(request);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/event/{eventId}/valid")
    @Operation(summary = "Listar cupons válidos", description = "Retorna todos os cupons válidos (não expirados) de um evento")
    public ResponseEntity<List<CouponResponseDTO>> getValidCoupons(@PathVariable UUID eventId) {
        List<CouponResponseDTO> coupons = couponService.consultValidCouponsForEvent(eventId);
        return ResponseEntity.ok(coupons);
    }
}