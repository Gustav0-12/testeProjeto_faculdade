package com.example.demo.dto;

import com.example.demo.model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public record CentroAcademicoResponseDto(
        Long id,
        String nome,
        String emailCentral,
        String telefoneCentral,
        Status status,
        LocalDateTime dataCadastro,
        Long iesId,
        DocenteResumoDto coordenador,
        List<CursoResumoDto> cursos) {
}