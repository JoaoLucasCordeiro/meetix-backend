package com.meetix.meetix_api.domain.coupon;

import jakarta.validation.constraints.*;

public record CouponRequestDTO(
        @NotBlank(message = "O código do cupom é obrigatório")
        @Size(min = 3, max = 50, message = "O código deve ter entre 3 e 50 caracteres")
        String code,

        @NotNull(message = "O desconto é obrigatório")
        @Min(value = 1, message = "O desconto deve ser no mínimo 1%")
        @Max(value = 100, message = "O desconto deve ser no máximo 100%")
        Integer discount,

        @NotNull(message = "A data de validade é obrigatória")
        Long valid
) {
}
