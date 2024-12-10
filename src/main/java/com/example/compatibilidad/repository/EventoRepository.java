package com.example.compatibilidad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.compatibilidad.entity.Evento;
import com.example.compatibilidad.entity.Relacion;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByRelacion(Relacion relacion);
}
