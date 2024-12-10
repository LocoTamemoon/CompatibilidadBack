package com.example.compatibilidad.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.compatibilidad.entity.ImpactoEvento;
import com.example.compatibilidad.entity.TipoEvento;

@Repository
public interface ImpactoEventoRepository extends JpaRepository<ImpactoEvento, Long> {

    // Ejemplo: Buscar por tipo de impacto
    List<ImpactoEvento> findByTipoImpacto(String tipoImpacto);
    
    Optional<ImpactoEvento> findByTipoEventoAndTipoImpacto(TipoEvento tipoEvento, String tipoImpacto);

}
