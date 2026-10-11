package com.example.demo.dto;

import com.example.demo.model.enums.ModalidadeMonitoria;

public record AtualizarRelatorioDto(String ocorrencias,
                                    ModalidadeMonitoria modalidade,
                                    String local,
                                    String frequencia,
                                    String consideracoes) {
}
