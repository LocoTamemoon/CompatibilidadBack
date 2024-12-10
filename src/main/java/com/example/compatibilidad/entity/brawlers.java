package com.example.compatibilidad.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class brawlers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBrawler;

    private String nombreBrawler;

    @ManyToOne
    @JoinColumn(name = "personalidad_brawler", referencedColumnName = "idPersonalidad")  // Aquí referenciamos `idPersonalidad` en `Personalidades`
    private personalidades personalidadBrawler;

    @ManyToOne
    @JoinColumn(name = "preferencia_personalidad", referencedColumnName = "idPersonalidad")  // Igualmente para la preferencia
    private personalidades preferenciaPersonalidad;

    private int saludBrawler;
    private int dañoBrawler;
    private int speedBrawler;
    private String descripcionBrawler;
    private String imagenBrawler;
}
