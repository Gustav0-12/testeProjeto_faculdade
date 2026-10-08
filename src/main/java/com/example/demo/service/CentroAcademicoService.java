package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.CentroAcademico;
import com.example.demo.model.Docente;
import com.example.demo.model.InstitutoEnsinoSuperior;
import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.repository.CentroAcademicoRepository;
import com.example.demo.repository.DocenteRepository;
import com.example.demo.repository.IesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CentroAcademicoService {

    private final CentroAcademicoRepository centroAcademicoRepository;
    private final IesRepository iesRepository;
    private final HistoricoService historicoService;
    private final DocenteRepository docenteRepository;

    @Transactional
    public void criar(RegistrarCaDto dto) {

        InstitutoEnsinoSuperior ies = iesRepository.findById(dto.iesId()).orElseThrow(() -> new RuntimeException("IES não encontrada"));

        if (ies.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Não é possível cadastrar um CA em uma IES inativa.");
        }

        CentroAcademico ca = new CentroAcademico();

        ca.setNome(dto.nome());
        ca.setEmailCentral(dto.emailCentral());
        ca.setTelefoneCentral(dto.telefoneCentral());
        ca.setDataCadastro(LocalDateTime.now());
        ca.setStatus(Status.ATIVO);
        ca.setIes(ies);

        centroAcademicoRepository.save(ca);

        historicoService.registrar(
                TipoAcao.CADASTRO,
                LocalDateTime.now(),
                "CENTRO_ACADEMICO",
                "Criação de novo Centro Acadêmico",
                ca.getId()
        );
    }

    @Transactional
    public void editar(Long caId, AtualizarCaDto dto) {

        CentroAcademico ca = centroAcademicoRepository.findById(caId).orElseThrow(() -> new RuntimeException("CA não encontrado"));

        if (ca.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Não é possível editar um CA inativo.");
        }

        ca.setNome(dto.nome());
        ca.setEmailCentral(dto.emailCentral());
        ca.setTelefoneCentral(dto.telefoneCentral());

        centroAcademicoRepository.save(ca);

        historicoService.registrar(
                TipoAcao.EDICAO,
                LocalDateTime.now(),
                "CENTRO_ACADEMICO",
                "Atualização dos dados do Centro Acadêmico",
                ca.getId()
        );
    }

    @Transactional
    public void inativar(Long caId, String motivo) {

        CentroAcademico ca = centroAcademicoRepository.findById(caId).orElseThrow(() -> new RuntimeException("CA não encontrado"));

        if (ca.getStatus() == Status.INATIVO) {
            throw new RuntimeException("O Centro Acadêmico já está inativo.");
        }

        ca.setStatus(Status.INATIVO);

        centroAcademicoRepository.save(ca);

        historicoService.registrar(
                TipoAcao.INATIVACAO,
                LocalDateTime.now(),
                "CENTRO_ACADEMICO",
                motivo,
                ca.getId()
        );
    }

    @Transactional
    public void definirCoordenador(Long caId, Long docenteId) {

        CentroAcademico ca = centroAcademicoRepository.findById(caId).orElseThrow(() -> new RuntimeException("CA não encontrado"));

        if (ca.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Não é possível definir coordenador para um CA inativo.");
        }

        if (ca.getCoordenador() != null) {
            throw new RuntimeException("Este Centro Acadêmico já possui um coordenador.");
        }

        Docente docente = docenteRepository.findById(docenteId).orElseThrow(() -> new RuntimeException("Docente não encontrado"));

        if (docente.getStatus() == Status.INATIVO) {
            throw new RuntimeException("O docente está inativo.");
        }

        if (!docente.getCentroAcademico().getId().equals(ca.getId())) {
            throw new RuntimeException("O docente não pertence a este Centro Acadêmico.");
        }

        ca.setCoordenador(docente);
        centroAcademicoRepository.save(ca);

        historicoService.registrar(
                TipoAcao.EDICAO,
                LocalDateTime.now(),
                "CENTRO_ACADEMICO",
                "Definição do coordenador do Centro Acadêmico",
                ca.getId()
        );
    }

    @Transactional(readOnly = true)
    public List<CentroAcademicoResponseDto> buscarTodos() {
        return centroAcademicoRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CentroAcademicoResponseDto buscarPorId(Long caId) {
        CentroAcademico centroAcademico = centroAcademicoRepository.findById(caId).orElseThrow(() -> new RuntimeException("CA não encontrado"));
        return converterParaDto(centroAcademico);
    }

    private CentroAcademicoResponseDto converterParaDto(CentroAcademico ca) {

        DocenteResumoDto coordenador = null;
        if (ca.getCoordenador() != null) {
            coordenador = new DocenteResumoDto(ca.getCoordenador().getId(), ca.getCoordenador().getUsuario().getNome(), ca.getCoordenador().getMatricula());
        }

        List<CursoResumoDto> cursos = ca.getCursos().stream()
                .map(curso -> new CursoResumoDto(
                        curso.getId(),
                        curso.getSigla(),
                        curso.getDescricao(),
                        curso.getStatus()))
                .toList();

        return new CentroAcademicoResponseDto(
                ca.getId(),
                ca.getNome(),
                ca.getEmailCentral(),
                ca.getTelefoneCentral(),
                ca.getStatus(),
                ca.getDataCadastro(),
                ca.getIes().getId(),
                coordenador,
                cursos);
    }
}