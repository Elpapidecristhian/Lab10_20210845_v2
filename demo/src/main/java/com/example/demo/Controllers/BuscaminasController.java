package com.example.demo.Controllers;

import com.example.demo.Entity.Configuracion;
import com.example.demo.Entity.Movimiento;
import com.example.demo.Repository.ConfiguracionRepository;
import com.example.demo.Repository.MovimientoRepository;
import com.example.demo.Repository.PosicionBombaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping("/buscaminas")
public class BuscaminasController {

    @Autowired
    private ConfiguracionRepository configuracionRepo;

    @Autowired
    private PosicionBombaRepository bombaRepo;

    @Autowired
    private MovimientoRepository movimientoRepo;

    @GetMapping
    public String mostrarVista() {
        return "buscaminas";
    }

    @PostMapping("/explotar")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> explotar(@RequestParam int x, @RequestParam int y) {
        int idMina = 1;
        Configuracion config = configuracionRepo.findById(idMina)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada"));

        if (movimientoRepo.findByConfiguracion_IdMinaAndCoordXAndCoordY(idMina, x, y).isPresent()) {
            return ResponseEntity.ok(Map.of("mensaje", "Ya descubriste esta celda"));
        }

        boolean exploto = false;
        boolean gano = false;
        List<Map<String, Object>> celdasDescubiertas = new ArrayList<>();

        Queue<Point> porProcesar = new LinkedList<>();
        porProcesar.add(new Point(x, y));

        while (!porProcesar.isEmpty()) {
            Point punto = porProcesar.poll();
            int cx = punto.x;
            int cy = punto.y;

            if (movimientoRepo.findByConfiguracion_IdMinaAndCoordXAndCoordY(idMina, cx, cy).isPresent()) continue;

            boolean esBomba = bombaRepo.existsByConfiguracion_IdMinaAndCoordXAndCoordY(idMina, cx, cy);
            int minasAlrededor = bombaRepo.contarBombasAlrededor(idMina, cx, cy);

            Movimiento mov = Movimiento.builder()
                    .configuracion(config)
                    .coordX(cx)
                    .coordY(cy)
                    .descubierta(true)
                    .fueBomba(esBomba)
                    .minasAlrededor(minasAlrededor)
                    .fechaClick(LocalDateTime.now())
                    .build();
            movimientoRepo.save(mov);

            Map<String, Object> celda = new HashMap<>();
            celda.put("x", cx);
            celda.put("y", cy);
            celda.put("bomba", esBomba);
            celda.put("minasAlrededor", minasAlrededor);
            celdasDescubiertas.add(celda);

            if (esBomba) {
                config.setIntentosActuales(config.getIntentosActuales() - 1);
                configuracionRepo.save(config);
                exploto = true;
                break;
            }

            if (minasAlrededor == 0) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int nx = cx + dx;
                        int ny = cy + dy;
                        if ((dx != 0 || dy != 0) && estaDentro(nx, ny, config)) {
                            porProcesar.add(new Point(nx, ny));
                        }
                    }
                }
            }
        }

        String mensaje;
        boolean finJuego = false;

        if (exploto) {
            if (config.getIntentosActuales() == 0) {
                mensaje = "¡Boom! Has perdido el juego";
                finJuego = true;
                movimientoRepo.deleteByConfiguracion_IdMina(idMina);
                config.setIntentosActuales(config.getCantIntentos());
                configuracionRepo.save(config);
            } else {
                mensaje = "Has encontrado una bomba, te queda 1 vida";
            }

        } else {
            int total = config.getDimX() * config.getDimY();
            int seguras = total - config.getCantBombas();
            int descubiertas = movimientoRepo.countByConfiguracion_IdMinaAndFueBombaFalse(idMina);
            if (descubiertas >= seguras) {
                mensaje = "¡Felicidades! Has ganado el juego 🎉";
                finJuego = true;
                movimientoRepo.deleteByConfiguracion_IdMina(idMina);
                config.setIntentosActuales(config.getCantIntentos());
                configuracionRepo.save(config);
                gano = true;
            } else {
                mensaje = "Casillas descubiertas con éxito";
            }
        }

        return ResponseEntity.ok(Map.of(
                "mensaje", mensaje,
                "finJuego", finJuego,
                "descubiertas", celdasDescubiertas,
                "vidasRestantes", config.getIntentosActuales(),
                "gano", gano
        ));
    }

    private boolean estaDentro(int x, int y, Configuracion c) {
        return x >= 1 && x <= c.getDimX() && y >= 1 && y <= c.getDimY();
    }

}
