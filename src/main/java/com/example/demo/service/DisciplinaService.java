package com.example.demo.service;

import com.example.demo.dto.AtualizarDisciplinaDto;
import com.example.demo.dto.DisciplinaResponseDto;
import com.example.demo.dto.RegistrarDisciplinaDto;
import com.example.demo.model.Curso;
import com.example.demo.model.Disciplina;
import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.DisciplinaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final HistoricoService historicoService;

    @Transactional
    public void criar(RegistrarDisciplinaDto dto) {

        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        Disciplina disciplina = new Disciplina();

        disciplina.setSigla(dto.sigla());
        disciplina.setDescricao(dto.descricao());
        disciplina.setCargaHoraria(dto.cargaHoraria());
        disciplina.setCurso(curso);
        disciplina.setDataCadastro(LocalDateTime.now());
        disciplina.setStatus(Status.ATIVO);

        disciplinaRepository.save(disciplina);
        historicoService.registrar(TipoAcao.CADASTRO, LocalDateTime.now(), "DISCIPLINA", "Criação de disciplina", disciplina.getId());
    }

    @Transactional
    public void editar(Long disciplinaId, AtualizarDisciplinaDto dto) {

        Disciplina disciplina = disciplinaRepository.findById(disciplinaId).orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));

        Curso curso = cursoRepository.findById(dto.cursoId()).orElseThrow(() -> new EntityNotFoundException("Curso não encontrado."));

        disciplina.setSigla(dto.sigla());
        disciplina.setDescricao(dto.descricao());
        disciplina.setCargaHoraria(dto.cargaHoraria());
        disciplina.setCurso(curso);

        disciplinaRepository.save(disciplina);

        historicoService.registrar(TipoAcao.EDICAO, LocalDateTime.now(), "DISCIPLINA", "Edição de disciplina", disciplina.getId());
    }

    @Transactional
    public void inativar(Long disciplinaId, String motivo) {

        Disciplina disciplina = disciplinaRepository.findById(disciplinaId).orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));

        if (disciplina.getStatus() == Status.INATIVO) {
            throw new IllegalStateException("A disciplina já está inativa.");
        }

        disciplina.setStatus(Status.INATIVO);

        disciplinaRepository.save(disciplina);

        historicoService.registrar(TipoAcao.INATIVACAO, LocalDateTime.now(), "DISCIPLINA", motivo, disciplina.getId());
    }

    public List<DisciplinaResponseDto> buscarTodos() {
        return disciplinaRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    public DisciplinaResponseDto buscarPorId(Long id) {
        Disciplina disciplina = disciplinaRepository.findById(id).orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
        return converterParaDto(disciplina);
    }

    private DisciplinaResponseDto converterParaDto(Disciplina disciplina) {
        return new DisciplinaResponseDto(
                disciplina.getId(),
                disciplina.getSigla(),
                disciplina.getDescricao(),
                disciplina.getCargaHoraria(),
                disciplina.getDataCadastro(),
                disciplina.getStatus(),
                disciplina.getCurso().getId());
    }
}