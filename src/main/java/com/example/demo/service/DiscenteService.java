package com.example.demo.service;

import com.example.demo.dto.DiscenteResponseDto;
import com.example.demo.dto.RegistrarDiscenteDto;
import com.example.demo.model.*;
import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.model.enums.TipoPerfil;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.DiscenteRepository;
import com.example.demo.repository.RolesRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DiscenteService {

    private final DiscenteRepository discenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final HistoricoService historicoService;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void criar(RegistrarDiscenteDto dto) {
        if (discenteRepository.existsByMatricula(dto.matricula())) {
            throw new IllegalStateException("Já existe um discente com essa matrícula.");
        }

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("Já existe um usuário cadastrado com esse email.");
        }

        Curso curso = cursoRepository.findById(dto.cursoId()).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado."));

        Roles role = rolesRepository.findByTipoPerfil(TipoPerfil.ROLE_USUARIO).orElseThrow(() -> new EntityNotFoundException("Role de usuário não encontrada."));

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setPerfis(Set.of(role));
        usuario.setDataCadastro(LocalDateTime.now());

        usuarioRepository.save(usuario);

        Discente discente = new Discente();
        discente.setMatricula(dto.matricula());
        discente.setTelefone(dto.telefone());
        discente.setUsuario(usuario);
        discente.setCurso(curso);

        discenteRepository.save(discente);

        historicoService.registrar(TipoAcao.CADASTRO, LocalDateTime.now(), "DISCENTE", "Criação de novo discente", discente.getId());
    }

    public void inativar(Long discenteId, String motivo) {
        Discente discente = discenteRepository.findById(discenteId).orElseThrow(() -> new RuntimeException("Docente não encontrado."));

        if (discente.getStatus() == Status.INATIVO) {
            throw new RuntimeException(
                    "O docente já está inativo."
            );
        }

        discente.setStatus(Status.INATIVO);

        discenteRepository.save(discente);

        historicoService.registrar(TipoAcao.INATIVACAO, LocalDateTime.now(), "DISCENTE", motivo, discente.getId());
    }

    @Transactional(readOnly = true)
    public List<DiscenteResponseDto> buscarTodos() {
       return discenteRepository.findAll().stream().map(this::converterParaDto).toList();
    }

    public DiscenteResponseDto converterParaDto(Discente discente) {
        return new DiscenteResponseDto(
                discente.getId(),
                discente.getMatricula(),
                discente.getTelefone(),
                discente.getUsuario().getId(),
                discente.getUsuario().getNome(),
                discente.getUsuario().getEmail(),
                discente.getStatus(),
                discente.getCurso().getId());
    }
}
