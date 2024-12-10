package com.example.compatibilidad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.compatibilidad.entity.TipoEvento;

@Repository
public interface TipoEventoRepository extends JpaRepository<TipoEvento, Long> {
    Optional<TipoEvento> findByNombre(String nombre);
}
