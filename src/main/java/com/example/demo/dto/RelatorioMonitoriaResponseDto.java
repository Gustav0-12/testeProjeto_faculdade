package com.example.demo.dto;

import com.example.demo.model.enums.DiaSemana;
import com.example.demo.model.enums.ModalidadeMonitoria;
import com.example.demo.model.enums.StatusMonitoria;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record RelatorioMonitoriaResponseDto(Long id,
                                            String docente,
                                            String semestre,
                                            Set<DiaSemana> dias,
                                            LocalTime horaInicio,
                                            LocalTime horaFim,
                                            LocalDate dataInicio,
                                            LocalDate dataFim,
                                            StatusMonitoria status,
                                            String ocorrencias,
                                            ModalidadeMonitoria modalidade,
                                            String local,
                                            String frequencia,
                                            String consideracoes) {
}
