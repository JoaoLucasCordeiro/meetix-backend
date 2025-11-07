package com.meetix.meetix_api.service.impl;

// ...
import com.meetix.meetix_api.domain.coupon.Coupon;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;
import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.coupon.CouponResponseDTO;
import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.repositories.CouponRepository;
import com.meetix.meetix_api.repositories.EventRepository;
import com.meetix.meetix_api.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;

    @Override
    public CouponResponseDTO addCouponToEvent(UUID eventId, CouponRequestDTO couponData) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        Coupon coupon = new Coupon();
        coupon.setCode(couponData.code());
        coupon.setDiscount(couponData.discount());


        coupon.setValid(LocalDateTime.ofInstant(Instant.ofEpochMilli(couponData.valid()), ZoneId.systemDefault()));

        coupon.setEvent(event);

        Coupon savedCoupon = couponRepository.save(coupon);


        return mapToResponse(savedCoupon);
    }

    @Override
    public CouponResponseDTO applyCoupon(CouponApplyRequest request) {
        Coupon coupon = couponRepository.findByCode(request.code())
                .orElseThrow(() -> new EntityNotFoundException("Cupom '" + request.code() + "' inválido."));

        if (!coupon.getEvent().getId().equals(request.eventId())) {
            throw new IllegalArgumentException("Este cupom não é válido para este evento.");
        }


        if (coupon.getValid().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cupom expirado.");
        }

        return mapToResponse(coupon);
    }

    @Override
    public List<CouponResponseDTO> consultValidCouponsForEvent(UUID eventId) {
        LocalDateTime currentDate = LocalDateTime.now();


        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CouponResponseDTO mapToResponse(Coupon coupon) {
        return new CouponResponseDTO(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDiscount(),
                coupon.getValid(),
                coupon.getEvent().getId()
        );
    }
}