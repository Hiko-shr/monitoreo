package com.monitoreo.monitoreo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Respaldo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespaldo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 50, nullable = false)
    private String estado; 

    @Column(length = 250, nullable = false)
    private String detalle;
}