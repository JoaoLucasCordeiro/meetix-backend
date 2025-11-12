package com.meetix.meetix_api.service.impl;

import com.meetix.meetix_api.domain.coupon.Coupon;
import com.meetix.meetix_api.domain.coupon.CouponApplyRequest;
import com.meetix.meetix_api.domain.coupon.CouponRequestDTO;
import com.meetix.meetix_api.domain.coupon.CouponResponseDTO;
import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.exception.common.PermissionDeniedException;
import com.meetix.meetix_api.exception.common.ResourceNotFoundException;
import com.meetix.meetix_api.exception.common.ValidationException;
import com.meetix.meetix_api.repositories.CouponRepository;
import com.meetix.meetix_api.repositories.EventRepository;
import com.meetix.meetix_api.service.EventAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponServiceImpl Tests")
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventAdminService eventAdminService;

    @InjectMocks
    private CouponServiceImpl couponService;

    private UUID eventId;
    private UUID userId;
    private Event event;
    private User user;
    private CouponRequestDTO couponRequestDTO;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");

        event = new Event();
        event.setId(eventId);
        event.setTitle("Test Event");
        event.setOrganizer(user);

        long futureTimestamp = System.currentTimeMillis() + 86400000L;
        couponRequestDTO = new CouponRequestDTO("PROMO10", 10, futureTimestamp);

        coupon = new Coupon();
        coupon.setId(UUID.randomUUID());
        coupon.setCode("PROMO10");
        coupon.setDiscount(10);
        coupon.setValid(LocalDateTime.now().plusDays(1));
        coupon.setEvent(event);
    }

    @Test
    @DisplayName("Deve criar cupom com sucesso quando usuário tem permissão")
    void shouldCreateCouponSuccessfully() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventAdminService.isEventAdminOrCreator(eventId, userId)).thenReturn(true);
        when(couponRepository.findByCode("PROMO10")).thenReturn(Optional.empty());
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        CouponResponseDTO response = couponService.addCouponToEvent(eventId, couponRequestDTO, userId);

        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo("PROMO10");
        assertThat(response.discount()).isEqualTo(10);
        assertThat(response.eventId()).isEqualTo(eventId);

        verify(eventRepository).findById(eventId);
        verify(eventAdminService).isEventAdminOrCreator(eventId, userId);
        verify(couponRepository).findByCode("PROMO10");
        verify(couponRepository).save(any(Coupon.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando evento não existe")
    void shouldThrowResourceNotFoundExceptionWhenEventDoesNotExist() {

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponService.addCouponToEvent(eventId, couponRequestDTO, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Evento não encontrado");

        verify(eventRepository).findById(eventId);
        verify(eventAdminService, never()).isEventAdminOrCreator(any(), any());
        verify(couponRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar PermissionDeniedException quando usuário não tem permissão")
    void shouldThrowPermissionDeniedExceptionWhenUserHasNoPermission() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventAdminService.isEventAdminOrCreator(eventId, userId)).thenReturn(false);

        assertThatThrownBy(() -> couponService.addCouponToEvent(eventId, couponRequestDTO, userId))
                .isInstanceOf(PermissionDeniedException.class)
                .hasMessage("Apenas o criador ou administradores podem criar cupons para este evento");

        verify(eventRepository).findById(eventId);
        verify(eventAdminService).isEventAdminOrCreator(eventId, userId);
        verify(couponRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ValidationException quando código já existe")
    void shouldThrowValidationExceptionWhenCodeAlreadyExists() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventAdminService.isEventAdminOrCreator(eventId, userId)).thenReturn(true);
        when(couponRepository.findByCode("PROMO10")).thenReturn(Optional.of(coupon));

        assertThatThrownBy(() -> couponService.addCouponToEvent(eventId, couponRequestDTO, userId))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Já existe um cupom com o código 'PROMO10'");

        verify(couponRepository).findByCode("PROMO10");
        verify(couponRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ValidationException quando data é passada")
    void shouldThrowValidationExceptionWhenDateIsInThePast() {
        long pastTimestamp = System.currentTimeMillis() - 86400000L; // -1 dia
        CouponRequestDTO pastCouponDTO = new CouponRequestDTO("PROMO10", 10, pastTimestamp);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventAdminService.isEventAdminOrCreator(eventId, userId)).thenReturn(true);
        when(couponRepository.findByCode("PROMO10")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponService.addCouponToEvent(eventId, pastCouponDTO, userId))
                .isInstanceOf(ValidationException.class)
                .hasMessage("A data de validade deve ser futura");

        verify(couponRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve aplicar cupom com sucesso")
    void shouldApplyCouponSuccessfully() {

        CouponApplyRequest request = new CouponApplyRequest("PROMO10", eventId);
        when(couponRepository.findByCode("PROMO10")).thenReturn(Optional.of(coupon));

        CouponResponseDTO response = couponService.applyCoupon(request);

        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo("PROMO10");
        assertThat(response.discount()).isEqualTo(10);
        assertThat(response.eventId()).isEqualTo(eventId);

        verify(couponRepository).findByCode("PROMO10");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando cupom não existe")
    void shouldThrowResourceNotFoundExceptionWhenCouponDoesNotExist() {

        CouponApplyRequest request = new CouponApplyRequest("INVALID", eventId);
        when(couponRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponService.applyCoupon(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cupom 'INVALID' não encontrado");

        verify(couponRepository).findByCode("INVALID");
    }

    @Test
    @DisplayName("Deve lançar ValidationException quando cupom não é válido para o evento")
    void shouldThrowValidationExceptionWhenCouponIsNotValidForEvent() {

        UUID wrongEventId = UUID.randomUUID();
        CouponApplyRequest request = new CouponApplyRequest("PROMO10", wrongEventId);
        when(couponRepository.findByCode("PROMO10")).thenReturn(Optional.of(coupon));

        assertThatThrownBy(() -> couponService.applyCoupon(request))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Este cupom não é válido para este evento");

        verify(couponRepository).findByCode("PROMO10");
    }

    @Test
    @DisplayName("Deve lançar ValidationException quando cupom está expirado")
    void shouldThrowValidationExceptionWhenCouponIsExpired() {

        Coupon expiredCoupon = new Coupon();
        expiredCoupon.setId(UUID.randomUUID());
        expiredCoupon.setCode("EXPIRED10");
        expiredCoupon.setDiscount(10);
        expiredCoupon.setValid(LocalDateTime.now().minusDays(1));
        expiredCoupon.setEvent(event);

        CouponApplyRequest request = new CouponApplyRequest("EXPIRED10", eventId);
        when(couponRepository.findByCode("EXPIRED10")).thenReturn(Optional.of(expiredCoupon));

        assertThatThrownBy(() -> couponService.applyCoupon(request))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Cupom expirado");

        verify(couponRepository).findByCode("EXPIRED10");
    }

    @Test
    @DisplayName("Deve retornar lista de cupons válidos para evento")
    void shouldReturnValidCouponsForEvent() {

        Coupon coupon2 = new Coupon();
        coupon2.setId(UUID.randomUUID());
        coupon2.setCode("PROMO20");
        coupon2.setDiscount(20);
        coupon2.setValid(LocalDateTime.now().plusDays(2));
        coupon2.setEvent(event);

        List<Coupon> validCoupons = List.of(coupon, coupon2);
        when(couponRepository.findByEventIdAndValidAfter(eq(eventId), any(LocalDateTime.class)))
                .thenReturn(validCoupons);

        List<CouponResponseDTO> response = couponService.consultValidCouponsForEvent(eventId);

        assertThat(response).hasSize(2);
        assertThat(response.get(0).code()).isEqualTo("PROMO10");
        assertThat(response.get(1).code()).isEqualTo("PROMO20");

        verify(couponRepository).findByEventIdAndValidAfter(eq(eventId), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há cupons válidos")
    void shouldReturnEmptyListWhenNoValidCoupons() {

        when(couponRepository.findByEventIdAndValidAfter(eq(eventId), any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<CouponResponseDTO> response = couponService.consultValidCouponsForEvent(eventId);

        assertThat(response).isEmpty();

        verify(couponRepository).findByEventIdAndValidAfter(eq(eventId), any(LocalDateTime.class));
    }
}
