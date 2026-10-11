package com.example.demo.dto;

import com.example.demo.model.enums.TipoAcao;

import java.time.LocalDateTime;

public record HistoricoResponseDto(
        Long id,
        TipoAcao tipoAcao,
        String entidade,
        Long entidadeId,
        String motivo,
        LocalDateTime dataHora,
        String responsavelNome,
        String responsavelEmail) {}