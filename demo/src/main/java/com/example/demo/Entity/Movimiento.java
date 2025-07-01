package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mina", referencedColumnName = "id_mina")
    private Configuracion configuracion;

    @Column(name = "coord_x", nullable = false)
    private Integer coordX;

    @Column(name = "coord_y", nullable = false)
    private Integer coordY;

    @Column(nullable = false)
    private Boolean descubierta;

    @Column(name = "fue_bomba", nullable = false)
    private Boolean fueBomba;

    @Column(name = "minas_alrededor", nullable = false)
    private Integer minasAlrededor;

    @Column(name = "fecha_click", nullable = false)
    private LocalDateTime fechaClick;
}
