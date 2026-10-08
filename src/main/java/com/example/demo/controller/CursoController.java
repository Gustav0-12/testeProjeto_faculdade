package com.example.demo.controller;

import com.example.demo.dto.AtualizarCursoDto;
import com.example.demo.dto.CursoResponseDto;
import com.example.demo.dto.RegistrarCursoDto;
import com.example.demo.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody RegistrarCursoDto dto) {
        cursoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{cursoId}/editar")
    public ResponseEntity<Void> editar(@PathVariable Long cursoId, @RequestBody AtualizarCursoDto dto) {
        cursoService.editar(cursoId, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{cursoId}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long cursoId, @RequestBody String motivo) {
        cursoService.inativar(cursoId, motivo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<CursoResponseDto>> buscarTodos() {
        return ResponseEntity.ok(cursoService.buscarTodos());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'COLABORADOR')")
    @GetMapping("/{cursoId}")
    public ResponseEntity<CursoResponseDto> buscarPorId(@PathVariable Long cursoId) {
        return ResponseEntity.ok(cursoService.buscarPorId(cursoId));
    }
}
