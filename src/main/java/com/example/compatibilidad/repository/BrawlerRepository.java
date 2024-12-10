package com.example.compatibilidad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.compatibilidad.entity.brawlers;

@Repository
public interface BrawlerRepository extends JpaRepository<brawlers, Long> {
    List<brawlers> findAll();
}
