package com.example.demo.controller;

import com.example.demo.dto.AtualizarRelatorioDto;
import com.example.demo.dto.RegistrarRelatorioDto;
import com.example.demo.dto.RelatorioMonitoriaResponseDto;
import com.example.demo.model.Usuario;
import com.example.demo.service.RelatorioMonitoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorio-monitoria")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioMonitoriaService relatorioMonitoriaService;

    @PreAuthorize("hasRole('USUARIO')")
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody RegistrarRelatorioDto dto, @AuthenticationPrincipal Usuario usuario) {
        relatorioMonitoriaService.criar(dto, usuario.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('USUARIO')")
    @PutMapping("/editar")
    public ResponseEntity<Void> editar(@RequestBody AtualizarRelatorioDto dto, @AuthenticationPrincipal Usuario usuario) {
        relatorioMonitoriaService.editar(dto, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USUARIO')")
    @GetMapping
    public ResponseEntity<RelatorioMonitoriaResponseDto> emitirRelatorio(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(relatorioMonitoriaService.emitirRelatorio(usuario.getId()));
    }
}
