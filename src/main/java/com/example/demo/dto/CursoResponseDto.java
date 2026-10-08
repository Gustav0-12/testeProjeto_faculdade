package com.example.demo.dto;

import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.Turno;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record CursoResponseDto(
        Long id,
        String sigla,
        String descricao,
        Set<Turno> turnos,
        Status status,
        LocalDateTime dataCadastro,
        Long centroAcademicoId,
        DocenteResumoDto coordenador,
        List<DisciplinaResumoDto> disciplinas
) {}