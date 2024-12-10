package com.example.compatibilidad.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.compatibilidad.entity.Evento;
import com.example.compatibilidad.entity.Relacion;
import com.example.compatibilidad.entity.TipoEvento;
import com.example.compatibilidad.entity.brawlers;
import com.example.compatibilidad.repository.BrawlerRepository;
import com.example.compatibilidad.repository.TipoEventoRepository;
import com.example.compatibilidad.service.RelacionService;

@RestController
@RequestMapping("/api/relaciones")  // Cambiado a /api para que sea un endpoint REST
@CrossOrigin(origins = "http://localhost:3000")  // Permitir acceso desde el frontend (si es necesario)
public class RelacionController {

    @Autowired
    private RelacionService relacionService;

    @Autowired
    private BrawlerRepository brawlerRepository;

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

    // Endpoint para calcular la compatibilidad y registrar evento
    @PostMapping("/evaluarRelacion")
    public ResponseEntity<Map<String, Object>> evaluarRelacion(@RequestBody RelacionRequest request) {
        try {
            // Validar que los dos brawlers sean diferentes
            if (request.getBrawler1Id().equals(request.getBrawler2Id())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "No puedes seleccionar el mismo brawler para ambos personajes.");
                return ResponseEntity.badRequest().body(errorResponse);  // Respuesta 400 si los brawlers son iguales
            }

            // Obtener los brawlers
            brawlers brawler1 = brawlerRepository.findById(request.getBrawler1Id())
                    .orElseThrow(() -> new RuntimeException("Brawler 1 no encontrado"));
            brawlers brawler2 = brawlerRepository.findById(request.getBrawler2Id())
                    .orElseThrow(() -> new RuntimeException("Brawler 2 no encontrado"));

            // Registrar o actualizar la relación
            Relacion relacion = relacionService.obtenerRelacion(brawler1, brawler2);

            // Calcular compatibilidad anterior
            Double compatibilidadAnterior = relacion.getCompatibilidad();

            // Obtener el TipoEvento desde la base de datos por el nombre del evento
            TipoEvento tipoEventoObj = tipoEventoRepository.findByNombre(request.getTipoEvento())
                    .orElseThrow(() -> new RuntimeException("Tipo de evento no encontrado"));

            // Registrar el evento
            Evento evento = relacionService.registrarEvento(relacion.getIdRelacion(), request.getTipoEvento(), tipoEventoObj.getDescripcion());

            // Calcular la compatibilidad nueva como la diferencia
            Double compatibilidadNueva = relacion.getCompatibilidad() - compatibilidadAnterior;

            // Formatear la compatibilidad nueva con el signo correcto
            String compatibilidadNuevaConSigno = (compatibilidadNueva >= 0 ? "+ " : "- ") + Math.abs(compatibilidadNueva);

            // Crear la respuesta con los resultados
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("compatibilidadAnterior", compatibilidadAnterior);
            respuesta.put("compatibilidadNueva", compatibilidadNuevaConSigno);  // Usar la versión con signo formateado
            respuesta.put("evento", evento);  // Incluir el evento completo
            respuesta.put("nivelRelacionActualizado", relacion.getNivelRelacion().getNivelRelacion());
            respuesta.put("impacto", evento.getTipoImpacto());  // Usar el impacto del evento directamente

            return ResponseEntity.ok(respuesta);  // Respuesta 200 con los datos

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al evaluar la relación: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);  // Respuesta 500 si ocurre un error
        }
    }

    // Clase auxiliar para recibir el cuerpo de la solicitud (JSON)
    public static class RelacionRequest {
        private Long brawler1Id;
        private Long brawler2Id;
        private String tipoEvento;

        public Long getBrawler1Id() {
            return brawler1Id;
        }

        public void setBrawler1Id(Long brawler1Id) {
            this.brawler1Id = brawler1Id;
        }

        public Long getBrawler2Id() {
            return brawler2Id;
        }

        public void setBrawler2Id(Long brawler2Id) {
            this.brawler2Id = brawler2Id;
        }

        public String getTipoEvento() {
            return tipoEvento;
        }

        public void setTipoEvento(String tipoEvento) {
            this.tipoEvento = tipoEvento;
        }
    }
}
