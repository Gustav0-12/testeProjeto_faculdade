package com.example.demo.dto;

import com.example.demo.model.enums.DiaSemana;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record RegistrarMonitoriaDto(Long docenteId,
                                    Long discenteId,
                                    String semestre,
                                    Long disciplinaId,
                                    Set<DiaSemana> dias,
                                    LocalDate dataInicio,
                                    LocalDate dataFim,
                                    LocalTime horarioInicio,
                                    LocalTime horarioFim) {
}
