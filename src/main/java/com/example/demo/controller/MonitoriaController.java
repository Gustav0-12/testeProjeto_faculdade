package com.example.demo.controller;

import com.example.demo.dto.MonitoriaResponseDto;
import com.example.demo.dto.RegistrarMonitoriaDto;
import com.example.demo.model.Usuario;
import com.example.demo.service.MonitoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitorias")
@RequiredArgsConstructor
public class MonitoriaController {

    private final MonitoriaService monitoriaService;

    @PreAuthorize("hasRole('COLABORADOR')")
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody RegistrarMonitoriaDto dto) {
        monitoriaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, @RequestBody String motivo) {
        monitoriaService.cancelar(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizar(@PathVariable Long id, @RequestBody String motivo) {
        monitoriaService.finalizar(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<MonitoriaResponseDto>> buscarTodas() {
        return ResponseEntity.ok(monitoriaService.buscarTodas());
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<MonitoriaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(monitoriaService.buscarPorId(id));
    }

    @PreAuthorize("hasRole('USUARIO')")
    @GetMapping("/discente")
    public ResponseEntity<MonitoriaResponseDto> buscarPorDiscenteId(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(monitoriaService.buscarPorDiscenteId(usuario.getId()));
    }
}
