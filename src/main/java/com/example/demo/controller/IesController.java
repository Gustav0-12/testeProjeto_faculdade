package com.example.demo.controller;

import com.example.demo.dto.AtualizarIesDto;
import com.example.demo.dto.IesResponseDto;
import com.example.demo.dto.RegistrarIesDto;
import com.example.demo.service.IesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ies")
@RequiredArgsConstructor
public class IesController {

    private final IesService iesService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody RegistrarIesDto dto) {
        iesService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}/editar")
    public ResponseEntity<Void> editar(@PathVariable Long id, @RequestBody AtualizarIesDto dto) {
        iesService.editar(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id, @RequestBody String motivo) {
        iesService.inativar(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{id}/reitor/{docenteId}")
    public ResponseEntity<Void> definirReitor(@PathVariable Long id, @PathVariable Long docenteId) {
        iesService.definirReitor(id, docenteId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<IesResponseDto>> buscarTodos() {
        List<IesResponseDto> ies = iesService.buscarTodos();
        return ResponseEntity.ok(ies);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<IesResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(iesService.buscarPorId(id));
    }
}
