package com.ms.central.relacionamento.service;

import com.ms.central.relacionamento.controller.request.AtendenteRequestDTO;
import com.ms.central.relacionamento.controller.response.AtendenteResponseDTO;
import com.ms.central.relacionamento.entity.Atendente;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import com.ms.central.relacionamento.exception.NaoEncontradoException;
import com.ms.central.relacionamento.mapper.AtendeteMapper;
import com.ms.central.relacionamento.repository.AtendenteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AtendenteService {

    private final AtendenteRepository atendenteRepository;
    private final AtendeteMapper atendeteMapper;

    public Optional<Long> buscarProximoAtendenteDisponivel(TipoAtendimentoEnum tipo) {
        log.info("AtendenteService.buscarAtendentesDisponiveis - Buscando atendentes disponíveis para o tipo de atendimento {}", tipo);

        return atendenteRepository.findProximoAtendenteDisponivel(tipo);
    }

    public Long criarAtendente(@Valid AtendenteRequestDTO atendenteRequestDTO) {
        log.info("AtendenteService.criarAtendente - Criando novo atendente body={}", atendenteRequestDTO);
        Atendente atendente = atendeteMapper.toAtendente(atendenteRequestDTO);

        atendenteRepository.save(atendente);

        log.info("AtendenteService.criarAtendente - Atendente criado com sucesso idAtendimento={}", atendente.getIdAtendente());
        return atendente.getIdAtendente();
    }

    public Atendente findById(Long id) {
        return atendenteRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Atendente não encontrado."));
    }

    public List<AtendenteResponseDTO> findAll() {
        return atendenteRepository.findAll().stream()
                .map(atendeteMapper::toAtendenteResponseDTO)
                .toList();
    }
}
