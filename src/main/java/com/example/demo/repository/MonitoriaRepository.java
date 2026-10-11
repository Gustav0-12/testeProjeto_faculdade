package com.example.demo.repository;

import com.example.demo.model.Monitoria;
import com.example.demo.model.enums.StatusMonitoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonitoriaRepository extends JpaRepository<Monitoria, Long> {
    boolean existsByDiscenteIdAndSemestre(Long discenteId, String semestre);
    Optional<Monitoria> findByDiscenteIdAndStatus(Long discenteId, StatusMonitoria status);
}
