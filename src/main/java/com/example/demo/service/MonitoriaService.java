package com.example.demo.service;

import com.example.demo.dto.MonitoriaResponseDto;
import com.example.demo.dto.RegistrarMonitoriaDto;
import com.example.demo.model.*;
import com.example.demo.model.enums.StatusMonitoria;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.repository.DiscenteRepository;
import com.example.demo.repository.DisciplinaRepository;
import com.example.demo.repository.DocenteRepository;
import com.example.demo.repository.MonitoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitoriaService {

    private final MonitoriaRepository monitoriaRepository;
    private final DocenteRepository docenteRepository;
    private final DiscenteRepository discenteRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final HistoricoService historicoService;

    @Transactional
    public void criar(RegistrarMonitoriaDto dto) {
        Docente docente = docenteRepository.findById(dto.docenteId()).orElseThrow(() -> new EntityNotFoundException("Docente não encontrado"));
        Discente discente = discenteRepository.findById(dto.discenteId()).orElseThrow(() -> new EntityNotFoundException("Discente não encontrado"));
        Disciplina disciplina = disciplinaRepository.findById(dto.disciplinaId()).orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada"));

        if (monitoriaRepository.existsByDiscenteIdAndSemestre(discente.getId(), dto.semestre())) {
            throw new IllegalArgumentException("O discente já possui uma monitoria cadastrada neste semestre.");
        }

        if (!docente.getCentroAcademico().getId().equals(disciplina.getCurso().getCentroAcademico().getId())) {
            throw new IllegalArgumentException("O docente deve pertencer ao mesmo Centro Acadêmico da disciplina.");
        }

        validarHorario(dto);

        Monitoria monitoria = new Monitoria();
        monitoria.setDocente(docente);
        monitoria.setDiscente(discente);
        monitoria.setSemestre(dto.semestre());
        monitoria.setDisciplina(disciplina);
        monitoria.setDias(dto.dias());
        monitoria.setDataInicio(dto.dataInicio());
        monitoria.setDataFim(dto.dataFim());
        monitoria.setHoraInicio(dto.horarioInicio());
        monitoria.setHoraFim(dto.horarioFim());
        monitoriaRepository.save(monitoria);
        historicoService.registrar(TipoAcao.CADASTRO, LocalDateTime.now(), "MONITORIA", "CADASTRO DE MONITORIA", monitoria.getId());
    }

    @Transactional
    public void cancelar(Long id, String motivo) {
        Monitoria monitoria = monitoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Monitoria não encontrada."));

        if (monitoria.getStatus() != StatusMonitoria.ATIVA) {
            throw new IllegalArgumentException("Somente monitorias ativas podem ser canceladas.");
        }

        monitoria.setStatus(StatusMonitoria.CANCELADA);

        monitoriaRepository.save(monitoria);
        historicoService.registrar(TipoAcao.INATIVACAO, LocalDateTime.now(), "MONITORIA", motivo, monitoria.getId());
    }

    @Transactional
    public void finalizar(Long id, String motivo) {
        Monitoria monitoria = monitoriaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Monitoria não encontrada."));

        if (monitoria.getStatus() != StatusMonitoria.ATIVA) {
            throw new IllegalArgumentException("Somente monitorias ativas podem ser canceladas.");
        }

        monitoria.setStatus(StatusMonitoria.FINALIZADA);

        monitoriaRepository.save(monitoria);
        historicoService.registrar(TipoAcao.INATIVACAO, LocalDateTime.now(), "MONITORIA", motivo, monitoria.getId());
    }

    @Transactional(readOnly = true)
    public List<MonitoriaResponseDto> buscarTodas() {
        return monitoriaRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MonitoriaResponseDto buscarPorId(Long id) {
        Monitoria monitoria = monitoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Monitoria não encontrada"));
        return converterParaDto(monitoria);
    }

    @Transactional(readOnly = true)
    public MonitoriaResponseDto buscarPorDiscenteId(Long usuarioId) {
        Discente discente = discenteRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new EntityNotFoundException("Discente não encontrado"));
        Monitoria monitoria = monitoriaRepository.findByDiscenteIdAndStatus(discente.getId(), StatusMonitoria.ATIVA).orElseThrow(() -> new EntityNotFoundException("Monitoria ativa não encontrada"));
        return converterParaDto(monitoria);
    }

    private void validarHorario(RegistrarMonitoriaDto dto) {
        if (!dto.horarioFim().isAfter(dto.horarioInicio())) {
            throw new IllegalArgumentException("O horário final deve ser posterior ao inicial.");
        }

        long minutos = Duration.between(dto.horarioInicio(), dto.horarioFim()).toMinutes();
        if (minutos > 50) {
            throw new IllegalArgumentException("A duração diária da monitoria não pode ultrapassar 50 minutos.");
        }

        if (dto.dataFim().isBefore(dto.dataInicio())) {
            throw new IllegalArgumentException("A data final deve ser posterior ou igual à data inicial.");
        }
    }

    private MonitoriaResponseDto converterParaDto(Monitoria monitoria) {
        return new MonitoriaResponseDto(
                monitoria.getId(),
                monitoria.getDocente().getUsuario().getNome(),
                monitoria.getDiscente().getUsuario().getNome(),
                monitoria.getDisciplina().getDescricao(),
                monitoria.getSemestre(),
                monitoria.getDias(),
                monitoria.getHoraInicio(),
                monitoria.getHoraFim(),
                monitoria.getDataInicio(),
                monitoria.getDataFim(),
                monitoria.getStatus());
    }
}
