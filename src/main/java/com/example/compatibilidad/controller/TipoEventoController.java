package com.example.compatibilidad.controller;

import com.example.compatibilidad.entity.TipoEvento;
import com.example.compatibilidad.service.TipoEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/tipoevento")
public class TipoEventoController {

    @Autowired
    private TipoEventoService tipoEventoService;

    @GetMapping("/all")
    public List<TipoEvento> getAllTipoEventos() {
        return tipoEventoService.getAllEventos();
    }
}
