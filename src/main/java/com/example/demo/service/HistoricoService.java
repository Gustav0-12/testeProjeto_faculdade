package com.example.demo.service;

import com.example.demo.dto.HistoricoResponseDto;
import com.example.demo.model.Historico;
import com.example.demo.model.Usuario;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.repository.HistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    public void registrar(TipoAcao tipoAcao, LocalDateTime dataHora, String entidade, String motivo, Long entidadeId) {
        Historico historico = new Historico(
                tipoAcao,
                dataHora,
                entidade,
                motivo,
                entidadeId);

        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historico.setUsuarioResponsavel(usuario);

        historicoRepository.save(historico);
    }

    public List<HistoricoResponseDto> buscarTodos() {
        return historicoRepository.findAll().stream()
                .map(historico -> new HistoricoResponseDto(
                        historico.getId(),
                        historico.getTipoAcao(),
                        historico.getEntidade(),
                        historico.getEntidadeId(),
                        historico.getMotivo(),
                        historico.getDataHora(),
                        historico.getUsuarioResponsavel().getNome(),
                        historico.getUsuarioResponsavel().getEmail()))
                .toList();
    }
}
