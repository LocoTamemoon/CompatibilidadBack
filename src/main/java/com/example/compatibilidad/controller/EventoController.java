package com.example.compatibilidad.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.compatibilidad.entity.Evento;
import com.example.compatibilidad.service.EventoService;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping
    public List<Evento> listarEventos() {
        return eventoService.obtenerTodosEventos();  // Retorna la lista de eventos
    }
    
    @GetMapping("/listar/{uid}")
    public ResponseEntity<List<Map<String, Object>>> listarEventosPorUid(@PathVariable String uid) {
        List<Evento> eventos = eventoService.listarEventosPorUid(uid);

        if (eventos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Crear una lista para almacenar los resultados personalizados
        List<Map<String, Object>> resultado = new ArrayList<>();

        // Transformar cada Evento en un LinkedHashMap con los campos deseados
        for (Evento evento : eventos) {
            Map<String, Object> eventoMap = new LinkedHashMap<>();

            // Reemplazar los IDs por nombres
            eventoMap.put("Numero de Evento", evento.getIdEvento());
            eventoMap.put("Relacion", evento.getRelacion().getNivelRelacion().getNivelRelacion()); // Nombre del nivel de relación
            eventoMap.put("Tipo De Evento", evento.getTipoEvento().getNombre()); // Usar el campo 'nombre'
            eventoMap.put("Descripcion del Evento", evento.getDescripcionEvento());
            eventoMap.put("Impacto", evento.getTipoImpacto());
            eventoMap.put("Descripcion Del Evento", evento.getDescripcionImpacto());
            eventoMap.put("Fecha del Evento", evento.getFechaEvento());

            eventoMap.put("Nombre del Primer Personaje", evento.getRelacion().getBrawler1().getNombreBrawler());
            eventoMap.put("Nombre del Segundo Personaje", evento.getRelacion().getBrawler2().getNombreBrawler());

            eventoMap.put("brawler1_imagen", evento.getRelacion().getBrawler1().getImagenBrawler());
            eventoMap.put("brawler2_imagen", evento.getRelacion().getBrawler2().getImagenBrawler());

            resultado.add(eventoMap);
        }

        return ResponseEntity.ok(resultado);
    }


}
