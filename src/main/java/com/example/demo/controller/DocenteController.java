package com.example.demo.controller;

import com.example.demo.dto.DocenteResponseDto;
import com.example.demo.dto.RegistrarDocenteDto;
import com.example.demo.service.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService docenteService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody RegistrarDocenteDto dto) {
        docenteService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{docenteId}/inativar")
    public ResponseEntity<Void> editar(@PathVariable Long docenteId, @RequestBody String motivo) {
        docenteService.inativar(docenteId, motivo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<DocenteResponseDto>> buscarTodos() {
        return ResponseEntity.ok(docenteService.buscarTodos());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{docenteId}")
    public ResponseEntity<DocenteResponseDto> buscarPorId(@PathVariable Long docenteId) {
        return ResponseEntity.ok(docenteService.buscarPorId(docenteId));
    }
}
