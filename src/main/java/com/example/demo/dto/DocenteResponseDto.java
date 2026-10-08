package com.example.demo.dto;

import com.example.demo.model.enums.Status;

public record DocenteResponseDto(
        Long id,
        String matricula,
        String telefone,
        Long usuarioId,
        String nome,
        String email,
        Status status,
        CentroAcademicoResumoDto centroAcademico
) {
}