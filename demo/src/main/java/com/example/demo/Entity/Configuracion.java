package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "configuracion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Configuracion {

    @Id
    @Column(name = "id_mina")
    private Integer idMina;

    @Column(name = "dim_x", nullable = false)
    private Integer dimX;

    @Column(name = "dim_y", nullable = false)
    private Integer dimY;

    @Column(name = "cant_bombas", nullable = false)
    private Integer cantBombas;

    @Column(name = "cant_intentos", nullable = false)
    private Integer cantIntentos;

    @Column(name = "intentos_actuales", nullable = false)
    private Integer intentosActuales;
}
