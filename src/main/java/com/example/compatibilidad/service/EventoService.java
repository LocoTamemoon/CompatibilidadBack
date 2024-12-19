package com.example.compatibilidad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.compatibilidad.entity.Evento;
import com.example.compatibilidad.repository.EventoRepository;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> obtenerTodosEventos() {
        return eventoRepository.findAll();  // Obtiene todos los eventos
    }
    
    public List<Evento> listarEventosPorUid(String uid) {
        return eventoRepository.findByUid(uid);
    }
    
    public boolean eliminarEvento(Integer idEvento) {
        // Verificar si el evento existe
        if (eventoRepository.existsById(idEvento)) {
            eventoRepository.deleteById(idEvento);
            return true;  // Evento eliminado exitosamente
        } else {
            return false; // Evento no encontrado
        }
    }
}
