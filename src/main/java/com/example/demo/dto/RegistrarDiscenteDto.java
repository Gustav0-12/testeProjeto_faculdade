package com.example.demo.dto;

public record RegistrarDiscenteDto(
        String nome,
        String email,
        String senha,
        String matricula,
        String telefone,
        Long cursoId
) {}