package com.example.demo.dto;

import com.example.demo.model.enums.Status;
import java.time.LocalDateTime;
import java.util.List;

public record IesResponseDto(
        Long id,
        String sigla,
        String nome,
        String emailCentral,
        String telefoneCentral,
        LocalDateTime dataCadastro,
        Status status,
        DocenteResumoDto reitor,
        List<CentroAcademicoResumoDto> centrosAcademicos) {
}