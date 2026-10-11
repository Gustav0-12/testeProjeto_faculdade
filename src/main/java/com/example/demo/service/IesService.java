package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Docente;
import com.example.demo.model.InstitutoEnsinoSuperior;
import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.TipoAcao;
import com.example.demo.repository.DocenteRepository;
import com.example.demo.repository.IesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IesService {

    private final IesRepository iesRepository;
    private final HistoricoService historicoService;
    private final DocenteRepository docenteRepository;

    @Transactional
    public void criar(RegistrarIesDto dto) {
        String sigla = dto.sigla().trim().toUpperCase();

        if (iesRepository.existsBySigla(sigla)) {
            throw new RuntimeException("Já existe uma IES com essa sigla.");
        }

        InstitutoEnsinoSuperior ies = new InstitutoEnsinoSuperior();
        ies.setSigla(sigla);
        ies.setNome(dto.nome());
        ies.setEmailCentral(dto.emailCentral());
        ies.setTelefoneCentral(dto.telefoneCentral());
        ies.setDataCadastro(LocalDateTime.now());
        ies.setStatus(Status.ATIVO);

        iesRepository.save(ies);

        historicoService.registrar(TipoAcao.CADASTRO, LocalDateTime.now(),"IES", "Criação de nova IES", ies.getId());
    }

    @Transactional
    public void editar(Long iesId, AtualizarIesDto dto) {
        String novaSigla = dto.sigla().trim().toUpperCase();
        InstitutoEnsinoSuperior ies = iesRepository.findById(iesId).orElseThrow(() -> new RuntimeException("IES não encontrada"));

        if (!novaSigla.equals(ies.getSigla()) && iesRepository.existsBySigla(novaSigla)) {
            throw new RuntimeException("Já existe outra IES cadastrada com essa sigla.");
        }

        ies.setSigla(novaSigla);
        ies.setNome(dto.nome());
        ies.setEmailCentral(dto.emailCentral());
        ies.setTelefoneCentral(dto.telefoneCentral());
        iesRepository.save(ies);

        historicoService.registrar(TipoAcao.EDICAO, LocalDateTime.now(),"IES", "Atualização dos dados da IES", ies.getId());
    }

    @Transactional
    public void inativar(Long iesId, String motivo) {
        InstitutoEnsinoSuperior ies = iesRepository.findById(iesId).orElseThrow(() -> new RuntimeException("IES não encontrada"));

        if (ies.getStatus() == Status.INATIVO) {
            throw new RuntimeException("A IES já está inativa.");
        }

        ies.setStatus(Status.INATIVO);
        iesRepository.save(ies);

        historicoService.registrar(TipoAcao.INATIVACAO, LocalDateTime.now() ,"IES", motivo, ies.getId());
    }

    @Transactional
    public void definirReitor(Long iesId, Long docenteId) {
        InstitutoEnsinoSuperior ies = iesRepository.findById(iesId).orElseThrow(() -> new RuntimeException("IES não encontrada"));

        if (ies.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Não é possível definir reitor para uma IES inativa.");
        }

        if (ies.getReitor() != null) {
            throw new RuntimeException("Esta IES já possui um reitor.");
        }

        Docente docente = docenteRepository.findById(docenteId).orElseThrow(() -> new RuntimeException("Docente não encontrado"));

        if (docente.getStatus() == Status.INATIVO) {
            throw new RuntimeException("O docente está inativo.");
        }

        if (!docente.getCentroAcademico().getIes().getId().equals(ies.getId())) {
            throw new RuntimeException("O docente não pertence a esta IES.");
        }

        ies.setReitor(docente);
        iesRepository.save(ies);

        historicoService.registrar(TipoAcao.EDICAO, LocalDateTime.now(), "IES", "Definição do reitor da IES", ies.getId());
    }

    @Transactional(readOnly = true)
    public List<IesResponseDto> buscarTodos() {
        return iesRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public IesResponseDto buscarPorId(Long id) {
        InstitutoEnsinoSuperior ies = iesRepository.findById(id).orElseThrow(() -> new RuntimeException("IES não encontrada"));
        return converterParaDto(ies);
    }

    public IesResponseDto converterParaDto(InstitutoEnsinoSuperior ies) {

        DocenteResumoDto reitor = null;
        if (ies.getReitor() != null) {
            reitor = new DocenteResumoDto(
                    ies.getReitor().getId(),
                    ies.getReitor().getUsuario().getNome());
        }

        List<CentroAcademicoResumoDto> centrosAcademicos = ies.getCentrosAcademicos().stream().map(ca -> new CentroAcademicoResumoDto(
                ca.getId(), ca.getNome(), ca.getStatus())).toList();

        return new IesResponseDto(
                ies.getId(),
                ies.getSigla(),
                ies.getNome(),
                ies.getEmailCentral(),
                ies.getTelefoneCentral(),
                ies.getDataCadastro(),
                ies.getStatus(),
                reitor,
                centrosAcademicos);
    }
}
