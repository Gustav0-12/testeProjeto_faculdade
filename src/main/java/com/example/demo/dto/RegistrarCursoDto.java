package com.example.demo.dto;

import com.example.demo.model.enums.Turno;
import java.util.Set;

public record RegistrarCursoDto(String sigla, String descricao, Set<Turno> turnos, Long centroAcademicoId, Long coordenadorId) {
}