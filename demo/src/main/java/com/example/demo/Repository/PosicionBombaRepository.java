package com.example.demo.Repository;

import com.example.demo.Entity.PosicionBomba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosicionBombaRepository extends JpaRepository<PosicionBomba, Integer> {
    boolean existsByConfiguracion_IdMinaAndCoordXAndCoordY(Integer idMina, Integer coordX, Integer coordY);

    @Query("SELECT COUNT(b) FROM PosicionBomba b WHERE b.configuracion.idMina = :idMina AND " +
            "ABS(b.coordX - :x) <= 1 AND ABS(b.coordY - :y) <= 1 AND NOT (b.coordX = :x AND b.coordY = :y)")
    int contarBombasAlrededor(@Param("idMina") Integer idMina, @Param("x") Integer x, @Param("y") Integer y);
}
