package com.example.demo.dto;

import com.example.demo.model.enums.Status;

public record CursoResumoDto(Long id, String sigla, String descricao, Status status) {
}
