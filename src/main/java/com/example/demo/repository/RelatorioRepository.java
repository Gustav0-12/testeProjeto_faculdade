package com.example.demo.repository;

import com.example.demo.model.RelatorioMonitoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelatorioRepository extends JpaRepository<RelatorioMonitoria, Long> {
    boolean existsByMonitoriaId(Long id);
    Optional<RelatorioMonitoria> findByMonitoriaId(Long monitoriaId);

}
