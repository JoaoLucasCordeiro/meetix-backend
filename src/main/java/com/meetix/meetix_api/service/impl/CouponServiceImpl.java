package com.meetix.meetix_api.service.impl;

import com.meetix.meetix_api.domain.coupon.Coupon;
import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;
import com.meetix.meetix_api.repositories.CouponRepository;
import com.meetix.meetix_api.repositories.EventRepository;
import com.meetix.meetix_api.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;

    @Override
    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO couponData) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));

        Coupon coupon = new Coupon();
        coupon.setCode(couponData.code());
        coupon.setDiscount(couponData.discount());
        coupon.setValid(new Date(couponData.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }

    @Override
    public Coupon applyCoupon(CouponApplyRequest request) {
        // 1. Busca o cupom pelo código
        Coupon coupon = couponRepository.findByCode(request.code())
                .orElseThrow(() -> new EntityNotFoundException("Cupom '" + request.code() + "' inválido."));

        // 2. Verifica se o cupom pertence ao evento correto
        if (!coupon.getEvent().getId().equals(request.eventId())) {
            throw new IllegalArgumentException("Este cupom não é válido para este evento.");
        }

        // 3. Verifica se o cupom não está expirado
        if (coupon.getValid().before(new Date())) { // 'new Date()' é a data/hora atual
            throw new IllegalArgumentException("Cupom expirado.");
        }

        // 4. Se passou em todas as verificações, retorna o cupom
        return coupon;
    }

    @Override
    public List<Coupon> consultValidCouponsForEvent(UUID eventId) {
        // Pega a data/hora exata de agora
        Date currentDate = new Date();

        // Retorna a lista de cupons
        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }
}