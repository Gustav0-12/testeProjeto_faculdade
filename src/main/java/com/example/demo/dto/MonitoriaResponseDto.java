package com.example.demo.dto;

import com.example.demo.model.enums.DiaSemana;
import com.example.demo.model.enums.StatusMonitoria;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record MonitoriaResponseDto(Long id,
                                   String docente,
                                   String discente,
                                   String disciplina,
                                   String semestre,
                                   Set<DiaSemana> dias,
                                   LocalTime horaInicio,
                                   LocalTime horaFim,
                                   LocalDate dataInicio,
                                   LocalDate dataFim,
                                   StatusMonitoria status) {
}
