package com.example.compatibilidad.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.compatibilidad.entity.NivelRelacion;

@Repository
public interface NivelRelacionRepository extends JpaRepository<NivelRelacion, Long> {

	public Optional<NivelRelacion> findByNivelRelacion(String nivelRelacion);
}
