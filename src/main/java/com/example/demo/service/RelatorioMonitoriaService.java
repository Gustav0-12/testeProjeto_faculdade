package com.example.demo.service;

import com.example.demo.dto.AtualizarRelatorioDto;
import com.example.demo.dto.RegistrarRelatorioDto;
import com.example.demo.dto.RelatorioMonitoriaResponseDto;
import com.example.demo.model.Discente;
import com.example.demo.model.Monitoria;
import com.example.demo.model.RelatorioMonitoria;
import com.example.demo.model.enums.StatusMonitoria;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.repository.DiscenteRepository;
import com.example.demo.repository.MonitoriaRepository;
import com.example.demo.repository.RelatorioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RelatorioMonitoriaService {

    private final RelatorioRepository relatorioRepository;
    private final MonitoriaRepository monitoriaRepository;
    private final DiscenteRepository discenteRepository;
    private final HistoricoService historicoService;

    @Transactional
    public void criar(RegistrarRelatorioDto dto, Long usuarioId) {

        Discente discente = discenteRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new EntityNotFoundException("Discente não encontrado."));
        Monitoria monitoria = monitoriaRepository.findByDiscenteIdAndStatus(discente.getId(), StatusMonitoria.ATIVA).orElseThrow(() -> new EntityNotFoundException("Monitoria ativa não encontrada."));

        if (relatorioRepository.existsByMonitoriaId(monitoria.getId())) {
            throw new IllegalStateException("Essa monitoria já possui um relatório.");
        }

        RelatorioMonitoria relatorio = new RelatorioMonitoria();

        relatorio.setMonitoria(monitoria);
        relatorio.setOcorrencias(dto.ocorrencias());
        relatorio.setModalidade(dto.modalidade());
        relatorio.setLocal(dto.local());
        relatorio.setFrequencia(dto.frequencia());
        relatorio.setConsideracoes(dto.consideracoes());

        relatorioRepository.save(relatorio);
        historicoService.registrar(TipoAcao.CADASTRO, LocalDateTime.now(), "RELATORIO_MONITORIA", "CRIAÇÃO DE RELATÓRIO", relatorio.getId());
    }

    @Transactional
    public void editar(AtualizarRelatorioDto dto, Long usuarioId) {
        Discente discente = discenteRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new EntityNotFoundException("Discente não encontrado."));
        Monitoria monitoria = monitoriaRepository.findByDiscenteIdAndStatus(discente.getId(), StatusMonitoria.ATIVA).orElseThrow(() -> new EntityNotFoundException("Monitoria ativa não encontrada."));

        RelatorioMonitoria relatorioMonitoria = relatorioRepository.findByMonitoriaId(monitoria.getId()).orElseThrow(() ->
                new EntityNotFoundException("Relatório não encontrado"));

        relatorioMonitoria.setOcorrencias(dto.ocorrencias());
        relatorioMonitoria.setModalidade(dto.modalidade());
        relatorioMonitoria.setLocal(dto.local());
        relatorioMonitoria.setFrequencia(dto.frequencia());
        relatorioMonitoria.setConsideracoes(dto.consideracoes());
        historicoService.registrar(TipoAcao.EDICAO, LocalDateTime.now(), "RELATORIO_MONITORIA", "EDIÇÃO DE RELATÓRIO", relatorioMonitoria.getId());
    }

    @Transactional
    public RelatorioMonitoriaResponseDto emitirRelatorio(Long usuarioId) {
       Discente discente = discenteRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new EntityNotFoundException("Discente não encontrado."));
       Monitoria monitoria = monitoriaRepository.findByDiscenteIdAndStatus(discente.getId(), StatusMonitoria.ATIVA).orElseThrow(() -> new EntityNotFoundException("Monitoria ativa não encontrada."));

       RelatorioMonitoria relatorioMonitoria = relatorioRepository.findByMonitoriaId(monitoria.getId()).orElseThrow(() ->
               new EntityNotFoundException("Relatório não encontrado"));

       return converterParaDto(relatorioMonitoria);
    }

    private RelatorioMonitoriaResponseDto converterParaDto(RelatorioMonitoria relatorioMonitoria) {
        return new RelatorioMonitoriaResponseDto(
                relatorioMonitoria.getId(),
                relatorioMonitoria.getMonitoria().getDocente().getUsuario().getNome(),
                relatorioMonitoria.getMonitoria().getSemestre(),
                relatorioMonitoria.getMonitoria().getDias(),
                relatorioMonitoria.getMonitoria().getHoraInicio(),
                relatorioMonitoria.getMonitoria().getHoraFim(),
                relatorioMonitoria.getMonitoria().getDataInicio(),
                relatorioMonitoria.getMonitoria().getDataFim(),
                relatorioMonitoria.getMonitoria().getStatus(),
                relatorioMonitoria.getOcorrencias(),
                relatorioMonitoria.getModalidade(),
                relatorioMonitoria.getLocal(),
                relatorioMonitoria.getFrequencia(),
                relatorioMonitoria.getConsideracoes());
    }
}
