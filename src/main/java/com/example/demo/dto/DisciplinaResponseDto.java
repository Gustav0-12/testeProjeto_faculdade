package com.example.demo.dto;

import com.example.demo.model.enums.Status;

import java.time.LocalDateTime;

public record DisciplinaResponseDto(Long id, String sigla, String descricao, Integer cargaHoraria, LocalDateTime dataCadastro, Status status, Long cursoId) {
}
