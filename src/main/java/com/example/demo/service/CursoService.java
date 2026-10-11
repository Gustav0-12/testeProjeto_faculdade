package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.CentroAcademico;
import com.example.demo.model.Curso;
import com.example.demo.model.Docente;
import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.repository.CentroAcademicoRepository;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.DocenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CentroAcademicoRepository centroAcademicoRepository;
    private final HistoricoService historicoService;
    private final DocenteRepository docenteRepository;

    @Transactional
    public void criar(RegistrarCursoDto dto) {
        String sigla = dto.sigla().trim().toUpperCase();

        if (cursoRepository.existsBySigla(sigla)) {
            throw new RuntimeException("Já existe um curso cadastrado com essa sigla.");
        }

        CentroAcademico ca = centroAcademicoRepository.findById(dto.centroAcademicoId()).orElseThrow(() -> new RuntimeException("CA não encontrado"));

        if (ca.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Não é possível cadastrar um curso em um Centro Acadêmico inativo.");
        }

        Docente coordenador = docenteRepository.findById(dto.coordenadorId()).orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        if (coordenador.getStatus() == Status.INATIVO) {
            throw new RuntimeException("O docente coordenador está inativo.");
        }

        Curso curso = new Curso();

        curso.setSigla(sigla);
        curso.setDescricao(dto.descricao());
        curso.setTurnos(dto.turnos());
        curso.setCentroAcademico(ca);
        curso.setCoordenador(coordenador);
        curso.setDataCadastro(LocalDateTime.now());

        cursoRepository.save(curso);

        historicoService.registrar(TipoAcao.CADASTRO, LocalDateTime.now(), "CURSO", "Criação de novo curso", curso.getId()
        );
    }

    @Transactional
    public void editar(Long cursoId, AtualizarCursoDto dto) {

        Curso curso = cursoRepository.findById(cursoId).orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        if (curso.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Não é possível editar um curso inativo.");
        }

        String novaSigla = dto.sigla().trim().toUpperCase();

        if (!curso.getSigla().equals(novaSigla) && cursoRepository.existsBySigla(novaSigla)) {
            throw new RuntimeException("Já existe um curso cadastrado com essa sigla.");
        }

        CentroAcademico ca = centroAcademicoRepository.findById(dto.centroAcademicoId()).orElseThrow(() -> new RuntimeException("CA não encontrado"));

        if (ca.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Não é possível vincular o curso a um Centro Acadêmico inativo.");
        }

        Docente coordenador = docenteRepository.findById(dto.coordenadorId()).orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        if (coordenador.getStatus() == Status.INATIVO) {
            throw new RuntimeException("O docente coordenador está inativo.");
        }

        curso.setSigla(novaSigla);
        curso.setDescricao(dto.descricao().trim());
        curso.setTurnos(dto.turnos());
        curso.setCentroAcademico(ca);
        curso.setCoordenador(coordenador);

        cursoRepository.save(curso);

        historicoService.registrar(TipoAcao.EDICAO, LocalDateTime.now(), "CURSO", "Atualização dos dados do curso", curso.getId()
        );
    }

    @Transactional
    public void inativar(Long cursoId, String motivo) {
        Curso curso = cursoRepository.findById(cursoId).orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        if (curso.getStatus() == Status.INATIVO) {
            throw new RuntimeException("O curso já está inativo.");
        }

        curso.setStatus(Status.INATIVO);
        cursoRepository.save(curso);

        historicoService.registrar(TipoAcao.INATIVACAO, LocalDateTime.now(), "CURSO", motivo, curso.getId());
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDto> buscarTodos() {

        return cursoRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CursoResponseDto buscarPorId(Long id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return converterParaDto(curso);
    }

    private CursoResponseDto converterParaDto(Curso curso) {
        DocenteResumoDto coordenador = new DocenteResumoDto(
                    curso.getCoordenador().getId(),
                    curso.getCoordenador().getUsuario().getNome());

        List<DisciplinaResumoDto> disciplinas = curso.getDisciplinas().stream().map(disciplina -> new DisciplinaResumoDto(disciplina.getId(),
                disciplina.getSigla(),
                disciplina.getDescricao(),
                disciplina.getCargaHoraria(),
                disciplina.getStatus())).toList();

        return new CursoResponseDto(
                curso.getId(),
                curso.getSigla(),
                curso.getDescricao(),
                curso.getTurnos(),
                curso.getStatus(),
                curso.getDataCadastro(),
                curso.getCentroAcademico().getId(),
                coordenador,
                disciplinas);
    }
}
