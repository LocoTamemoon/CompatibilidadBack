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
public class Relacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRelacion;

    @ManyToOne
    @JoinColumn(name = "brawler_1")
    private brawlers brawler1;

    @ManyToOne
    @JoinColumn(name = "brawler_2")
    private brawlers brawler2;

    
    @ManyToOne
    @JoinColumn(name = "nivel_relacion") 
    private NivelRelacion nivelRelacion;
    
    
    private double compatibilidad;
    
    
}
