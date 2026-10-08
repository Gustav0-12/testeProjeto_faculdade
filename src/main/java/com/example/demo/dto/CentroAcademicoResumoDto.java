package com.example.demo.dto;

import com.example.demo.model.enums.Status;

public record CentroAcademicoResumoDto(Long id, String nome, Status status) {
}
