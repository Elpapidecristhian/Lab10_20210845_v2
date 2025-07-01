package com.example.demo.Entity;

import com.example.demo.Entity.Configuracion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posicion_bomba")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosicionBomba {

    @Id
    @Column(name = "id_bomba")
    private Integer idBomba;

    @Column(name = "coord_x", nullable = false)
    private Integer coordX;

    @Column(name = "coord_y", nullable = false)
    private Integer coordY;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mina", referencedColumnName = "id_mina")
    private Configuracion configuracion;
}
