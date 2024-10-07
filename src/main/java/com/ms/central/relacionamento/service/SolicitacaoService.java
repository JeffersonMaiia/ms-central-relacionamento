package com.ms.central.relacionamento.service;

import com.ms.central.relacionamento.controller.request.SolicitacaoAlterarRequestDTO;
import com.ms.central.relacionamento.controller.request.SolicitacaoRequestDTO;
import com.ms.central.relacionamento.controller.response.SolicitacaoResponseDTO;
import com.ms.central.relacionamento.entity.Atendente;
import com.ms.central.relacionamento.entity.Solicitacao;
import com.ms.central.relacionamento.enums.SolicitacaoStatusEnum;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import com.ms.central.relacionamento.exception.NaoEncontradoException;
import com.ms.central.relacionamento.exception.RegraNegocioException;
import com.ms.central.relacionamento.mapper.SolicitacaoMapper;
import com.ms.central.relacionamento.repository.SolicitacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final SolicitacaoMapper solicitacaoMapper;
    private final AtendenteService atendenteService;

    @Transactional
    public Long criarSolicitacao(SolicitacaoRequestDTO solicitacaoRequestDTO) {
        log.info("SolicitacaoService.criarSolicitacao - criando nova solicitação body={}", solicitacaoRequestDTO);

        var solicitacao = solicitacaoMapper.toEntity(solicitacaoRequestDTO);

        atendenteService.buscarProximoAtendenteDisponivel(solicitacao.getTipo())
                .ifPresent(solicitacao::emAtendimento);

        solicitacaoRepository.save(solicitacao);

        log.info("SolicitacaoService.criarSolicitacao - solicitação criada com sucesso idSolicitacao={}",
                solicitacao.getIdSolicitacao());

        return solicitacao.getIdSolicitacao();
    }

    @Transactional
    public void finalizarSolicitacao(Long idSolicitacao,
                                     Long idAtendente,
                                     SolicitacaoAlterarRequestDTO solicitacaoAlterarRequestDTO) {

        log.info("SolicitacaoService.finalizarSolicitacao - inicio -finalizando solicitação idSolicitacao={} idAtendente={}",
                idSolicitacao, idAtendente);

        if(!solicitacaoAlterarRequestDTO.getStatus().equals(SolicitacaoStatusEnum.FINALIZADO)) {
            log.error("SolicitacaoService.finalizarSolicitacao - status da solicitação não pode ser alterado para {}",
                    solicitacaoAlterarRequestDTO.getStatus());

            throw new RegraNegocioException("O status apenas pode ser FINALIZADO.");
        }

        var solicitacao = findById(idSolicitacao);

        solicitacao.finalizar();

        solicitacaoRepository.save(solicitacao);

        solicitacaoRepository.findProximaSolicitacaoByTipo(solicitacao.getTipo())
                .ifPresent(proximaSolicitacao -> proximaSolicitacao.emAtendimento(idAtendente));

        log.info("SolicitacaoService.finalizarSolicitacao - fim - finalizando solicitação idSolicitacao={} idAtendente={}",
                idSolicitacao, idAtendente);
    }

    public List<SolicitacaoResponseDTO> findAll() {
        return solicitacaoRepository.findAll().stream()
                .map(solicitacaoMapper::toSolicitacaoResponseDTO)
                .toList();
    }

    public void atribuirSolicitacao(Long idSolicitacao, Long idAtendente) {
        log.info("SolicitacaoService.atribuirSolicitacao - atribuindo solicitação idSolicitacao={} para atendente idAtendente={}",
                idSolicitacao, idAtendente);

        var solicitacao = findById(idSolicitacao);

        var atendente = atendenteService.findById(idAtendente);

        solicitacao.emAtendimento(atendente.getIdAtendente());

        solicitacaoRepository.save(solicitacao);
    }

    private Solicitacao findById(Long idSolicitacao) {
        return solicitacaoRepository.findById(idSolicitacao)
                .orElseThrow(() -> new NaoEncontradoException("Solicitação não encontrada."));
    }
}
