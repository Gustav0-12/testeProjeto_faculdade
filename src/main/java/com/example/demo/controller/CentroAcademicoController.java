package com.example.demo.controller;

import com.example.demo.dto.AtualizarCaDto;
import com.example.demo.dto.CentroAcademicoResponseDto;
import com.example.demo.dto.RegistrarCaDto;
import com.example.demo.service.CentroAcademicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/centros-academicos")
@RequiredArgsConstructor
public class CentroAcademicoController {

    private final CentroAcademicoService centroAcademicoService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody RegistrarCaDto dto) {
        centroAcademicoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}/editar")
    public ResponseEntity<Void> editar(@PathVariable Long id, @RequestBody AtualizarCaDto dto) {
        centroAcademicoService.editar(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id, @RequestBody String motivo) {
        centroAcademicoService.inativar(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{id}/coordenador/{docenteId}")
    public ResponseEntity<Void> definirCoordenador(@PathVariable Long id, @PathVariable Long docenteId) {
        centroAcademicoService.definirCoordenador(id, docenteId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<CentroAcademicoResponseDto>> buscarTodos() {
        return ResponseEntity.ok(centroAcademicoService.buscarTodos());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<CentroAcademicoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(centroAcademicoService.buscarPorId(id));
    }
}