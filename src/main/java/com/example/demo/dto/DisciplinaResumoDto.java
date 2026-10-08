package com.example.demo.dto;

import com.example.demo.model.enums.Status;

public record DisciplinaResumoDto(Long id, String sigla, String descricao, Integer cargaHoraria, Status status) {
}