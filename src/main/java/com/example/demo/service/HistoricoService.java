package com.example.demo.service;

import com.example.demo.model.Historico;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.repository.HistoricoRepository;
import lombok.RequiredArgsConstructor;
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

        historicoRepository.save(historico);
    }

    public List<Historico> buscarTodos() {
        return historicoRepository.findAll();
    }
}
