package com.example.compatibilidad.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.compatibilidad.entity.Evento;
import com.example.compatibilidad.entity.Relacion;
import com.example.compatibilidad.entity.TipoEvento;
import com.example.compatibilidad.entity.brawlers;
import com.example.compatibilidad.repository.BrawlerRepository;
import com.example.compatibilidad.repository.TipoEventoRepository;
import com.example.compatibilidad.service.RelacionService;

@RestController
@RequestMapping("/api/relaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class RelacionController {

    @Autowired
    private RelacionService relacionService;

    @Autowired
    private BrawlerRepository brawlerRepository;

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

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
            Relacion relacion = relacionService.obtenerRelacion(brawler1, brawler2, request.getUid());

            // Calcular compatibilidad anterior
            Double compatibilidadAnterior = relacion.getCompatibilidad();

            // Obtener el TipoEvento desde la base de datos por el nombre del evento
            TipoEvento tipoEventoObj = tipoEventoRepository.findByNombre(request.getTipoEvento())
                    .orElseThrow(() -> new RuntimeException("Tipo de evento no encontrado"));

            // Registrar el evento
            Evento evento = relacionService.registrarEvento(relacion.getIdRelacion(), request.getTipoEvento(), tipoEventoObj.getDescripcion(), request.getUid());

            // Calcular la compatibilidad nueva como la diferencia
            Double compatibilidadNueva = relacion.getCompatibilidad() - compatibilidadAnterior;

            // Formatear la compatibilidad nueva con el signo correcto
            String compatibilidadNuevaConSigno = (compatibilidadNueva >= 0 ? "+ " : "- ") + Math.abs(compatibilidadNueva);

            // Obtener la compatibilidad actual después del evento
            Double compatibilidadActual = relacion.getCompatibilidad();

            // Obtener la descripción del impacto
            String descripcionImpacto = evento.getDescripcionImpacto();  // Asumiendo que tienes un método para obtener la descripción del impacto

            // Crear el HashMap con los resultados organizados según el formato solicitado
            Map<String, Object> respuesta = new LinkedHashMap<>(); // Usamos LinkedHashMap para garantizar el orden de los elementos
            respuesta.put("evento", tipoEventoObj.getNombre());  // Nombre del evento
            respuesta.put("descripcionEvento", tipoEventoObj.getDescripcion());  // Descripción del evento
            respuesta.put("brawler1Nombre", brawler1.getNombreBrawler());  // Nombre del Brawler 1
            respuesta.put("brawler2Nombre", brawler2.getNombreBrawler());  // Nombre del Brawler 2
         
            respuesta.put("impacto", evento.getTipoImpacto());  // Tipo de impacto del evento
            respuesta.put("descripcionImpacto", descripcionImpacto);  // Descripción del impacto
            respuesta.put("compatibilidadAnterior", compatibilidadAnterior);  // Compatibilidad anterior
            respuesta.put("compatibilidadNueva", compatibilidadNuevaConSigno);  // Compatibilidad nueva con signo
            respuesta.put("compatibilidadActual", compatibilidadActual);  // Compatibilidad actual de la relación
            respuesta.put("nivelRelacionActualizado", relacion.getNivelRelacion().getNivelRelacion());  // Nivel de relación actualizado

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
        private String uid;  // Añadido el campo UID para el parámetro que falta
        private Long nivelRelacionId;  // Añadido el campo nivelRelacionId

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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Long getNivelRelacionId() {
            return nivelRelacionId;  // Devolvemos el nivel de relación
        }

        public void setNivelRelacionId(Long nivelRelacionId) {
            this.nivelRelacionId = nivelRelacionId;  // Establecemos el nivel de relación
        }
    }



    @GetMapping
    public List<Relacion> listarRelaciones() {
        return relacionService.listarRelaciones();
    }
    
    
    
    @GetMapping("/listar/{uid}")
    public ResponseEntity<List<Map<String, Object>>> listarRelacionesPorUid(@PathVariable String uid) {
        List<Relacion> relaciones = relacionService.listarRelacionesPorUid(uid);

        if (relaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Map<String, Object>> resultado = new ArrayList<>();

        // Mapear las relaciones al formato deseado usando LinkedHashMap
        for (Relacion relacion : relaciones) {
            Map<String, Object> relacionMap = new LinkedHashMap<>();

            relacionMap.put("ID Relacion", relacion.getIdRelacion());
            relacionMap.put("Nombre del Primer Personaje", relacion.getBrawler1().getNombreBrawler());
            relacionMap.put("Imagen del Primer Personaje", relacion.getBrawler1().getImagenBrawler());

            relacionMap.put("Nombre del Segundo Personaje", relacion.getBrawler2().getNombreBrawler());
            relacionMap.put("Imagen del Segundo Personaje", relacion.getBrawler2().getImagenBrawler());

            relacionMap.put("Nivel de Relacion", relacion.getNivelRelacion().getNivelRelacion());
            relacionMap.put("Compatibilidad", relacion.getCompatibilidad());

            resultado.add(relacionMap);
        }

        return ResponseEntity.ok(resultado);
    }
    
    
    
    
    // Método PUT para actualizar el nivel de relación
    @PutMapping("/editar/{idRelacion}")
    public ResponseEntity<Relacion> editarRelacion(
            @PathVariable Long idRelacion,  // El ID de la relación que se va a editar
            @RequestBody RelacionRequest relacionRequest) {  // Recibe el nivel de relación y el UID

        Relacion updatedRelacion = relacionService.editarNivelRelacion(
                idRelacion, 
                relacionRequest.getNivelRelacionId(), 
                relacionRequest.getUid()
        );

        if (updatedRelacion != null) {
            return ResponseEntity.ok(updatedRelacion);  // Si la relación fue editada correctamente
        } else {
            return ResponseEntity.notFound().build();  // Si la relación no fue encontrada
        }
    }

    
}
