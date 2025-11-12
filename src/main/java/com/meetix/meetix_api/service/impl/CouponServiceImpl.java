package com.meetix.meetix_api.service.impl;

import com.meetix.meetix_api.domain.coupon.Coupon;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;
import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.coupon.CouponResponseDTO;
import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.exception.common.PermissionDeniedException;
import com.meetix.meetix_api.exception.common.ResourceNotFoundException;
import com.meetix.meetix_api.exception.common.ValidationException;
import com.meetix.meetix_api.repositories.CouponRepository;
import com.meetix.meetix_api.repositories.EventRepository;
import com.meetix.meetix_api.service.CouponService;
import com.meetix.meetix_api.service.EventAdminService;
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
    private final EventAdminService eventAdminService;

    @Override
    public CouponResponseDTO addCouponToEvent(UUID eventId, CouponRequestDTO couponData, UUID userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));

        // Valida permissões - apenas criador ou admin pode criar cupons
        if (!eventAdminService.isEventAdminOrCreator(eventId, userId)) {
            throw new PermissionDeniedException("Apenas o criador ou administradores podem criar cupons para este evento");
        }

        // Valida se já existe um cupom com o mesmo código
        if (couponRepository.findByCode(couponData.code()).isPresent()) {
            throw new ValidationException("Já existe um cupom com o código '" + couponData.code() + "'");
        }

        // Converte timestamp para LocalDateTime
        LocalDateTime validDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(couponData.valid()),
                ZoneId.systemDefault()
        );

        // Valida se a data é futura
        if (validDate.isBefore(LocalDateTime.now())) {
            throw new ValidationException("A data de validade deve ser futura");
        }

        Coupon coupon = new Coupon();
        coupon.setCode(couponData.code());
        coupon.setDiscount(couponData.discount());
        coupon.setValid(validDate);
        coupon.setEvent(event);

        Coupon savedCoupon = couponRepository.save(coupon);

        return mapToResponse(savedCoupon);
    }

    @Override
    public CouponResponseDTO applyCoupon(CouponApplyRequest request) {
        Coupon coupon = couponRepository.findByCode(request.code())
                .orElseThrow(() -> new ResourceNotFoundException("Cupom '" + request.code() + "' não encontrado"));

        if (!coupon.getEvent().getId().equals(request.eventId())) {
            throw new ValidationException("Este cupom não é válido para este evento");
        }

        if (coupon.getValid().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Cupom expirado");
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