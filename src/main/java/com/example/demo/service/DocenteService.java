package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.CentroAcademico;
import com.example.demo.model.Docente;
import com.example.demo.model.Roles;
import com.example.demo.model.Usuario;
import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.model.enums.TipoPerfil;
import com.example.demo.repository.CentroAcademicoRepository;
import com.example.demo.repository.DocenteRepository;
import com.example.demo.repository.RolesRepository;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CentroAcademicoRepository centroAcademicoRepository;
    private final HistoricoService historicoService;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    @Transactional
    public void criar(RegistrarDocenteDto dto) {

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Já existe um usuário cadastrado com esse email.");
        }

        if (docenteRepository.existsByMatricula(dto.matricula())) {
            throw new RuntimeException("Já existe um docente com essa matrícula.");
        }

        CentroAcademico ca = centroAcademicoRepository.findById(dto.centroAcademicoId()).orElseThrow(() -> new RuntimeException("Centro Acadêmico não encontrado."));

        if (ca.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Não é possível cadastrar um docente em um Centro Acadêmico inativo.");
        }

        Roles role = rolesRepository
                .findByTipoPerfil(TipoPerfil.ROLE_COLABORADOR)
                .orElseThrow(() ->
                        new RuntimeException("Perfil COLABORADOR não encontrado.")
                );

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setPerfis(Set.of(role));
        usuario.setDataCadastro(LocalDateTime.now());

        usuarioRepository.save(usuario);

        Docente docente = new Docente();
        docente.setMatricula(dto.matricula());
        docente.setTelefone(dto.telefone());
        docente.setUsuario(usuario);
        docente.setCentroAcademico(ca);

        docenteRepository.save(docente);

        historicoService.registrar(
                TipoAcao.CADASTRO,
                LocalDateTime.now(),
                "DOCENTE",
                "Criação de novo docente",
                docente.getId()
        );
    }

    @Transactional
    public void inativar(Long docenteId, String motivo) {

        Docente docente = docenteRepository.findById(docenteId).orElseThrow(() -> new RuntimeException("Docente não encontrado."));

        if (docente.getStatus() == Status.INATIVO) {
            throw new RuntimeException(
                    "O docente já está inativo."
            );
        }

        docente.setStatus(Status.INATIVO);

        docenteRepository.save(docente);

        historicoService.registrar(TipoAcao.INATIVACAO, LocalDateTime.now(), "DOCENTE", motivo, docente.getId());
    }

    @Transactional(readOnly = true)
    public List<DocenteResponseDto> buscarTodos() {

        return docenteRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public DocenteResponseDto buscarPorId(Long docenteId) {
        Docente docente = docenteRepository.findById(docenteId).orElseThrow(() -> new RuntimeException("Docente não encontrado."));
        return converterParaDto(docente);
    }

    private DocenteResponseDto converterParaDto(Docente docente) {

        Usuario usuario = docente.getUsuario();
        CentroAcademicoResumoDto centroAcademico = new CentroAcademicoResumoDto(
                docente.getCentroAcademico().getId(),
                docente.getCentroAcademico().getNome(),
                docente.getCentroAcademico().getStatus());

        return new DocenteResponseDto(
                docente.getId(),
                docente.getMatricula(),
                docente.getTelefone(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                docente.getStatus(),
                centroAcademico
        );
    }
}