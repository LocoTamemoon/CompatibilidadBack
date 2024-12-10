package com.example.compatibilidad.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.compatibilidad.entity.Evento;
import com.example.compatibilidad.entity.Relacion;
import com.example.compatibilidad.entity.TipoEvento;
import com.example.compatibilidad.entity.brawlers;
import com.example.compatibilidad.repository.BrawlerRepository;
import com.example.compatibilidad.repository.TipoEventoRepository;
import com.example.compatibilidad.service.RelacionService;

@Controller
@RequestMapping("/relaciones")
@CrossOrigin(origins = "http://localhost:3000")  // Cambiar según tu frontend
public class RelacionController {

    @Autowired
    private RelacionService relacionService;

    @Autowired
    private BrawlerRepository brawlerRepository;

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

    // Mostrar la página de compatibilidad (con el formulario)
    @GetMapping("/compatibilidad")
    public String mostrarPaginaCompatibilidad(Model model) {
        // Obtener todos los tipos de evento desde la base de datos
        List<TipoEvento> tiposEventos = tipoEventoRepository.findAll();
        model.addAttribute("tiposEventos", tiposEventos);

        // Obtener todos los brawlers desde la base de datos
        List<brawlers> brawlers = brawlerRepository.findAll();
        model.addAttribute("brawlers", brawlers);  // Añadir los brawlers al modelo

        return "compatibilidad";  // nombre del archivo HTML
    }

    // Calcular compatibilidad y registrar evento
    @PostMapping("/evaluarRelacion")
    public String evaluarRelacion(@RequestParam Long brawler1Id,
                                  @RequestParam Long brawler2Id,
                                  @RequestParam String tipoEvento,
                                  Model model) {
        try {
            // Validar que los dos brawlers sean diferentes
            if (brawler1Id.equals(brawler2Id)) {
                model.addAttribute("error", "No puedes seleccionar el mismo brawler para ambos personajes.");
                return "compatibilidad";  // Redirigir a la misma página
            }

            // Obtener los brawlers
            brawlers brawler1 = brawlerRepository.findById(brawler1Id)
                    .orElseThrow(() -> new RuntimeException("Brawler 1 no encontrado"));
            brawlers brawler2 = brawlerRepository.findById(brawler2Id)
                    .orElseThrow(() -> new RuntimeException("Brawler 2 no encontrado"));

            // Registrar o actualizar la relación
            Relacion relacion = relacionService.obtenerRelacion(brawler1, brawler2);

            // Calcular compatibilidad anterior
            Double compatibilidadAnterior = relacion.getCompatibilidad();

            // Obtener el TipoEvento desde la base de datos por el nombre del evento
            TipoEvento tipoEventoObj = tipoEventoRepository.findByNombre(tipoEvento)
                    .orElseThrow(() -> new RuntimeException("Tipo de evento no encontrado"));

            // Registrar el evento
            Evento evento = relacionService.registrarEvento(relacion.getIdRelacion(), tipoEvento, tipoEventoObj.getDescripcion());

            // Calcular la compatibilidad nueva como la diferencia
            Double compatibilidadNueva = relacion.getCompatibilidad() - compatibilidadAnterior;

            // Formatear la compatibilidad nueva con el signo correcto
            String compatibilidadNuevaConSigno = (compatibilidadNueva >= 0 ? "+ " : "- ") + Math.abs(compatibilidadNueva);

            // Crear respuesta con los resultados
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("compatibilidadAnterior", compatibilidadAnterior);
            respuesta.put("compatibilidadNueva", compatibilidadNuevaConSigno);  // Usar la versión con signo formateado
            respuesta.put("evento", evento);  // Incluye el evento completo
            respuesta.put("nivelRelacionActualizado", relacion.getNivelRelacion().getNivelRelacion());
            respuesta.put("impacto", evento.getTipoImpacto());  // Usar el impacto del evento directamente

            model.addAttribute("resultado", respuesta);

            return "compatibilidad";  // Redirigir a la misma página con los resultados
        } catch (Exception e) {
            model.addAttribute("error", "Error al evaluar la relación: " + e.getMessage());
            return "compatibilidad";  // Redirigir a la misma página con el error
        }
    }

}
