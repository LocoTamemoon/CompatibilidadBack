package com.example.compatibilidad.service;

import com.example.compatibilidad.entity.Evento;
import com.example.compatibilidad.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> obtenerTodosEventos() {
        return eventoRepository.findAll();  // Obtiene todos los eventos
    }
}
