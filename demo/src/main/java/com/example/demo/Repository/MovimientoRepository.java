package com.example.demo.Repository;

import com.example.demo.Entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    Optional<Movimiento> findByConfiguracion_IdMinaAndCoordXAndCoordY(Integer idMina, Integer coordX, Integer coordY);

    int countByConfiguracion_IdMinaAndFueBombaFalse(Integer idMina);

    void deleteByConfiguracion_IdMina(Integer idMina);
}
