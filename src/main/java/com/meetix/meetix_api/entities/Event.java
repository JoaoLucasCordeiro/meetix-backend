package com.meetix.meetix_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório.")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    @Future(message = "A data de início deve ser no futuro.")
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @NotNull(message = "A data e hora de término são obrigatórias.")
    @Future(message = "A data de término deve ser no futuro.")
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private String location;

    private Integer maxAttendees;

    @Column(name = "is_paid")
    private Boolean isPaid;

    private BigDecimal price;

    private Long organizerId;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}