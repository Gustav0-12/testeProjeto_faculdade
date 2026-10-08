package com.example.demo.controller;

import com.example.demo.dto.AtualizarDisciplinaDto;
import com.example.demo.dto.DisciplinaResponseDto;
import com.example.demo.dto.RegistrarDisciplinaDto;
import com.example.demo.service.DisciplinaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplinas")
@RequiredArgsConstructor
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR')")
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody RegistrarDisciplinaDto dto) {
        disciplinaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR')")
    @PutMapping("/{disciplinaId}/editar")
    public ResponseEntity<Void> editar(@PathVariable Long disciplinaId, @RequestBody AtualizarDisciplinaDto dto) {
        disciplinaService.editar(disciplinaId, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR')")
    @PutMapping("/{disciplinaId}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long disciplinaId, @RequestBody String motivo) {
        disciplinaService.inativar(disciplinaId, motivo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<DisciplinaResponseDto>> buscarTodos() {
        return ResponseEntity.ok(disciplinaService.buscarTodos());
    }
}
