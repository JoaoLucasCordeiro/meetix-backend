package com.meetix.meetix_api.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private UUID eventId;

    private String titulo;

    @Column(columnDefinition = "TEXT") 
    private String descricao;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(name = "inicio_inscricao")
    private LocalDateTime inicioInscricao;

    @Column(name = "fim_inscricao")
    private LocalDateTime fimInscricao;

    private String local;

    private Integer capacidade;

    private boolean pago;

    private Double preco;

    private String capaUrl; // link da foto de capa do evento

    private String tipoEvento; // palestra, workshop, festa, etc.
}
