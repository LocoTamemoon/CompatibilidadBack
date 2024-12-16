package com.example.compatibilidad.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.compatibilidad.entity.Evento;
import com.example.compatibilidad.entity.ImpactoEvento;
import com.example.compatibilidad.entity.NivelRelacion;
import com.example.compatibilidad.entity.Relacion;
import com.example.compatibilidad.entity.TipoEvento;
import com.example.compatibilidad.entity.brawlers;
import com.example.compatibilidad.repository.BrawlerRepository;
import com.example.compatibilidad.repository.EventoRepository;
import com.example.compatibilidad.repository.ImpactoEventoRepository;
import com.example.compatibilidad.repository.NivelRelacionRepository;
import com.example.compatibilidad.repository.RelacionRepository;
import com.example.compatibilidad.repository.TipoEventoRepository;

@Service
public class RelacionService {

    @Autowired
    private RelacionRepository relacionRepository;

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private BrawlerRepository brawlerRepository;

    @Autowired
    private NivelRelacionRepository nivelRelacionRepository;

    @Autowired
    private ImpactoEventoRepository impactoEventoRepository;

    private double probabilidadApriori = 0.5;  // Probabilidad a priori de compatibilidad

    // Cálculo de compatibilidad usando el teorema de Bayes
    public Double calcularCompatibilidad(brawlers brawler1, brawlers brawler2) {
        // Validar que los brawlers no sean nulos
        if (brawler1 == null || brawler2 == null) {
            throw new IllegalArgumentException("Brawlers no pueden ser nulos");
        }

        // P(B|A) = Probabilidad observada (basada en atributos y personalidad)
        double probabilidadObservada = calcularProbabilidadCondicional(brawler1, brawler2);

        // P(B) = Probabilidad total de observar los atributos (suponemos 1.0 para simplificación)
        double probabilidadTotal = 1.0;

        // Aplicamos el teorema de Bayes: P(A|B) = (P(B|A) * P(A)) / P(B)
        double compatibilidad = (probabilidadObservada * probabilidadApriori) / probabilidadTotal;

        return compatibilidad;
    }

    // Calcula la probabilidad condicional (P(B|A)) en función de los atributos
    private double calcularProbabilidadCondicional(brawlers brawler1, brawlers brawler2) {
        // Validar atributos físicos de los brawlers
        if (brawler1.getSaludBrawler() < 0 || brawler2.getSaludBrawler() < 0) {
            throw new IllegalArgumentException("Los atributos de salud no pueden ser negativos");
        }
        if (brawler1.getDañoBrawler() < 0 || brawler2.getDañoBrawler() < 0) {
            throw new IllegalArgumentException("Los atributos de daño no pueden ser negativos");
        }
        if (brawler1.getSpeedBrawler() < 0 || brawler2.getSpeedBrawler() < 0) {
            throw new IllegalArgumentException("Los atributos de velocidad no pueden ser negativos");
        }

        // Calculamos la compatibilidad de personalidad
        double compatibilidadPersonalidad = calcularCompatibilidadPersonalidad(brawler1, brawler2);

        // Calculamos la compatibilidad de atributos físicos (salud, daño, velocidad)
        double compatibilidadAtributos = calcularCompatibilidadAtributos(brawler1, brawler2);

        // P(B|A) es la combinación de la compatibilidad de personalidad y atributos
        return (compatibilidadPersonalidad + compatibilidadAtributos) / 2.0;
    }

    // Calcula la compatibilidad basada en la personalidad
    private double calcularCompatibilidadPersonalidad(brawlers brawler1, brawlers brawler2) {
        double compatibilidad = 0.0;

        // Validar que las personalidades no sean nulas
        if (brawler1.getPersonalidadBrawler() == null || brawler2.getPersonalidadBrawler() == null) {
            throw new IllegalArgumentException("Las personalidades de los brawlers no pueden ser nulas");
        }

        // Si la personalidad del brawler1 coincide con la preferencia del brawler2
        if (brawler1.getPersonalidadBrawler().equals(brawler2.getPreferenciaPersonalidad())) {
            compatibilidad += 1.0;  // Alta compatibilidad
        }

        // Si las personalidades son iguales
        if (brawler1.getPersonalidadBrawler().equals(brawler2.getPersonalidadBrawler())) {
            compatibilidad += 0.8;  // Buena compatibilidad
        }

        // Si la preferencia de personalidad del brawler1 coincide con la personalidad del brawler2
        if (brawler1.getPreferenciaPersonalidad().equals(brawler2.getPersonalidadBrawler())) {
            compatibilidad += 0.6;  // Moderada compatibilidad
        }

        // Si no hay coincidencia directa
        if (compatibilidad == 0.0) {
            compatibilidad += 0.2; // Baja compatibilidad
        }

        return compatibilidad;
    }

    // Calcula la compatibilidad basada en atributos físicos
    private double calcularCompatibilidadAtributos(brawlers brawler1, brawlers brawler2) {
        // Asegurarse de que los atributos no sean nulos (en caso de que usen tipos Integer, que pueden ser null)
        if (brawler1.getSaludBrawler() < 0 || brawler2.getSaludBrawler() < 0 ||
            brawler1.getDañoBrawler() < 0 || brawler2.getDañoBrawler() < 0 ||
            brawler1.getSpeedBrawler() < 0 || brawler2.getSpeedBrawler() < 0) {
            throw new IllegalArgumentException("Los atributos de los brawlers no pueden ser negativos");
        }

        // Comparar los atributos utilizando el operador '==' ya que son de tipo primitivo 'int'
        double compatibilidadSalud = (brawler1.getSaludBrawler() == brawler2.getSaludBrawler()) ? 1.0 : 0.5;
        double compatibilidadDaño = (brawler1.getDañoBrawler() == brawler2.getDañoBrawler()) ? 1.0 : 0.5;
        double compatibilidadVelocidad = (brawler1.getSpeedBrawler() == brawler2.getSpeedBrawler()) ? 1.0 : 0.5;

        return (compatibilidadSalud + compatibilidadDaño + compatibilidadVelocidad) / 3.0;
    }

    public Relacion obtenerRelacion(brawlers brawler1, brawlers brawler2, String uid) {
        // Validar que los brawlers no sean nulos
        if (brawler1 == null || brawler2 == null) {
            throw new IllegalArgumentException("Los brawlers no pueden ser nulos");
        }

        // Si uid es nulo o vacío, asignar "administrador" como valor predeterminado
        final String finalUid = (uid == null || uid.isEmpty()) ? "administrador" : uid;

        // Buscar la relación en ambos órdenes (brawler1 -> brawler2 y brawler2 -> brawler1) con el UID
        Optional<Relacion> relacionOpt = relacionRepository.findByBrawler1AndBrawler2AndUid(brawler1, brawler2, finalUid)
                .or(() -> relacionRepository.findByBrawler1AndBrawler2AndUid(brawler2, brawler1, finalUid));

        if (relacionOpt.isPresent()) {
            // Si la relación existe para el mismo UID, devolverla
            return relacionOpt.get();
        } else {
            // Si la relación no existe para este UID, crear una nueva relación
            Relacion nuevaRelacion = new Relacion();
            nuevaRelacion.setBrawler1(brawler1);
            nuevaRelacion.setBrawler2(brawler2);
            nuevaRelacion.setUid(finalUid); // Asignar el UID a la relación

            // Obtener el nivel de relación "Conocidos" en la primera interacción
            NivelRelacion nivel = nivelRelacionRepository.findByNivelRelacion("Conocidos")
                    .orElseThrow(() -> new RuntimeException("Nivel de relación 'Conocidos' no encontrado"));

            nuevaRelacion.setNivelRelacion(nivel);
            nuevaRelacion.setCompatibilidad(calcularCompatibilidad(brawler1, brawler2)); // Calcular compatibilidad

            return relacionRepository.save(nuevaRelacion);  // Guardar la relación
        }
    }







    public Evento registrarEvento(Long idRelacion, String tipoEvento, String descripcionEvento, String uid) {
        // Validar que los parámetros no sean nulos
        if (idRelacion == null || tipoEvento == null || descripcionEvento == null || uid == null || uid.isEmpty()) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }

        // Obtener la relación
        Relacion relacion = relacionRepository.findById(idRelacion)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada"));

        // Obtener el tipo de evento
        TipoEvento tipo = tipoEventoRepository.findByNombre(tipoEvento)
                .orElseThrow(() -> new RuntimeException("Tipo de evento no encontrado"));

        // Crear el evento
        Evento evento = new Evento();
        evento.setRelacion(relacion);
        evento.setTipoEvento(tipo);

        // Aquí es donde se establece el UID
        evento.setDescripcionEvento(descripcionEvento);
        evento.setFechaEvento(LocalDateTime.now());
        evento.setUid(uid);  // Establecer el UID

        // Determinar el impacto de manera aleatoria (positivo o negativo)
        String tipoImpacto = (Math.random() < 0.5) ? "Positivo" : "Negativo";  // Aleatorio 50% positivo o negativo

        // Buscar el impacto en base al TipoEvento y el TipoImpacto
        Optional<ImpactoEvento> impacto = impactoEventoRepository
                .findByTipoEventoAndTipoImpacto(tipo, tipoImpacto);

        // Usar la descripción desde ImpactoEvento si existe
        String descripcionImpacto = impacto.isPresent() ? impacto.get().getDescripcionImpacto() :
                                    (tipoImpacto.equals("Positivo") ? "Impacto positivo en la relación" : "Impacto negativo en la relación");

        // Asignar el impacto y la descripción
        evento.setTipoImpacto(tipoImpacto);  // Establecer tipoImpacto (Positivo o Negativo)
        evento.setDescripcionImpacto(descripcionImpacto);  // Descripción del impacto desde la base de datos

        // Guardar el evento
        eventoRepository.save(evento);

        // Inicializamos el incremento de compatibilidad
        double incrementoCompatibilidad = 0.0;

        // Incremento de compatibilidad dependiendo del tipo de evento
        if (tipoEvento.equals("Conflicto")) {
            incrementoCompatibilidad = (tipoImpacto.equals("Positivo")) ? 0.11 : -0.05;
        } else if (tipoEvento.equals("Alianza")) {
            incrementoCompatibilidad = (tipoImpacto.equals("Positivo")) ? 0.13 : -0.08;
        } else if (tipoEvento.equals("Desafío")) {
            incrementoCompatibilidad = (tipoImpacto.equals("Positivo")) ? 0.10 : -0.04;
        } else if (tipoEvento.equals("Reunión")) {
            incrementoCompatibilidad = (tipoImpacto.equals("Positivo")) ? 0.17 : -0.10;
        }

        // Actualizamos la compatibilidad
        double compatibilidadActualizada = relacion.getCompatibilidad() + incrementoCompatibilidad;
        if (compatibilidadActualizada > 1.0) compatibilidadActualizada = 1.0;
        if (compatibilidadActualizada < 0.0) compatibilidadActualizada = 0.0;

        // Determinamos el nivel de la relación basado en la compatibilidad
        NivelRelacion nuevoNivelRelacion;

        if (compatibilidadActualizada >= 0.70) {
            nuevoNivelRelacion = nivelRelacionRepository.findByNivelRelacion("Pareja")
                    .orElseThrow(() -> new RuntimeException("Nivel de relación 'Pareja' no encontrado"));
        } else if (compatibilidadActualizada >= 0.49) {
            nuevoNivelRelacion = nivelRelacionRepository.findByNivelRelacion("Amigos")
                    .orElseThrow(() -> new RuntimeException("Nivel de relación 'Amigos' no encontrado"));
        } else {
            nuevoNivelRelacion = nivelRelacionRepository.findByNivelRelacion("Conocidos")
                    .orElseThrow(() -> new RuntimeException("Nivel de relación 'Conocidos' no encontrado"));
        }

        // Asignar el nuevo nivel de relación a la relación
        relacion.setNivelRelacion(nuevoNivelRelacion);
        relacion.setCompatibilidad(compatibilidadActualizada);
        relacionRepository.save(relacion);

        return evento;
    }





    
    public List<Relacion> listarRelaciones() {
        return relacionRepository.findAll();
    }
}
