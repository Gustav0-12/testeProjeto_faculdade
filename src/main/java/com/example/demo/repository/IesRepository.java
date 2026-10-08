package com.example.demo.repository;

import com.example.demo.model.InstitutoEnsinoSuperior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IesRepository extends JpaRepository<InstitutoEnsinoSuperior, Long> {
    boolean existsBySigla(String sigla);
}
