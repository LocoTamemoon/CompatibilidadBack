package com.example.compatibilidad.service;

import com.example.compatibilidad.entity.TipoEvento;
import com.example.compatibilidad.repository.TipoEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoEventoService {

    @Autowired
    private TipoEventoRepository tipoEventoRepository;

    public List<TipoEvento> getAllEventos() {
        return tipoEventoRepository.findAll();
    }
}
