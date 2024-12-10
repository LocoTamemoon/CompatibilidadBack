package com.example.compatibilidad.controller;

import com.example.compatibilidad.entity.brawlers;
import com.example.compatibilidad.repository.BrawlerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brawlers")
@CrossOrigin(origins = "http://localhost:3000")  // Permitir acceso desde el frontend
public class BrawlerController {

    @Autowired
    private BrawlerRepository brawlerRepository;

    // Endpoint para obtener todos los brawlers
    @GetMapping("/")
    public ResponseEntity<List<brawlers>> obtenerTodosBrawlers() {
        try {
            List<brawlers> brawlersList = brawlerRepository.findAll();  // Recupera todos los brawlers de la base de datos
            return ResponseEntity.ok(brawlersList);  // Respuesta con lista de brawlers
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);  // Error en caso de problemas
        }
    }
}
