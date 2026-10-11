package com.example.demo.repository;

import com.example.demo.model.Discente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscenteRepository extends JpaRepository<Discente, Long> {
    boolean existsByMatricula(String matricula);
    Optional<Discente> findByUsuarioId(Long usuarioId);
}
