package com.example.demo.dto;

public record RegistrarDocenteDto(String nome,
                                  String email,
                                  String senha,
                                  String matricula,
                                  String telefone,
                                  Long centroAcademicoId) {
}