package com.example.demo.controller;

import com.example.demo.dto.DiscenteResponseDto;
import com.example.demo.dto.RegistrarDiscenteDto;
import com.example.demo.service.DiscenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discentes")
@RequiredArgsConstructor
public class DiscenteController {

    private final DiscenteService discenteService;

    @PreAuthorize("hasRole('COLABORADOR')")
    @PostMapping("/criar")
    public ResponseEntity<Void> criar(@RequestBody RegistrarDiscenteDto dto) {
        discenteService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('COLABORADOR')")
    @GetMapping
    public ResponseEntity<List<DiscenteResponseDto>> buscarTodos() {
        return ResponseEntity.ok(discenteService.buscarTodos());
    }
}
