package com.ms.central.relacionamento.service;

import com.ms.central.relacionamento.controller.request.SolicitacaoAlterarRequestDTO;
import com.ms.central.relacionamento.controller.request.SolicitacaoRequestDTO;
import com.ms.central.relacionamento.entity.Atendente;
import com.ms.central.relacionamento.entity.Solicitacao;
import com.ms.central.relacionamento.enums.SolicitacaoStatusEnum;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import com.ms.central.relacionamento.exception.NaoEncontradoException;
import com.ms.central.relacionamento.exception.RegraNegocioException;
import com.ms.central.relacionamento.mapper.SolicitacaoMapper;
import com.ms.central.relacionamento.repository.SolicitacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SolicitacaoServiceTest {

    @InjectMocks
    private SolicitacaoService solicitacaoService;

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private SolicitacaoMapper solicitacaoMapper;

    @Mock
    private AtendenteService atendenteService;

    @DisplayName("Deve criar uma solicitação")
    @Test
    void deveCriarUmaSolicitacao() {
        var request = SolicitacaoRequestDTO.builder()
                .tipo(TipoAtendimentoEnum.CARTOES)
                .assunto("Problema com cartão")
                .build();

        var solicitacao = Solicitacao.builder()
                .tipo(TipoAtendimentoEnum.CARTOES)
                .assunto("Problema com cartão")
                .build();

        when(atendenteService.buscarProximoAtendenteDisponivel(any(TipoAtendimentoEnum.class)))
                .thenReturn(Optional.of(1L));

        when(solicitacaoMapper.toEntity(any(SolicitacaoRequestDTO.class)))
                .thenReturn(solicitacao);

        when(solicitacaoRepository.save(any(Solicitacao.class)))
                .thenReturn(Solicitacao.builder().idSolicitacao(1L).build());

        Long idSolicitacao = solicitacaoService.criarSolicitacao(request);

        verify(solicitacaoRepository).save(solicitacao);
        Assertions.assertEquals(Long.getLong("1"), idSolicitacao);
    }

    @DisplayName("Deve lançar exceção ao tentar finalizar uma solicitação com status diferente de FINALIZADO")
    @Test
    void deveLancarExcecaoAoTentarFinalizarUmaSolicitacaoComStatusDiferenteDeFinalizado() {
        var solicitacao = Solicitacao.builder()
                .idSolicitacao(1L)
                .tipo(TipoAtendimentoEnum.CARTOES)
                .assunto("Problema com cartão")
                .build();

        when(solicitacaoRepository.findById(any()))
                .thenReturn(Optional.of(solicitacao));

        var request = SolicitacaoAlterarRequestDTO.builder()
                .status(SolicitacaoStatusEnum.AGUARDANDO_ATENDIMENTO)
                .build();

        Throwable throwable = catchThrowable(() -> solicitacaoService.finalizarSolicitacao(1L, 1L, request));

        assertThat(throwable)
                .isNotNull()
                .hasMessage("O status apenas pode ser FINALIZADO.")
                .isExactlyInstanceOf(RegraNegocioException.class);
    }

    @DisplayName("Deve finalizar uma solicitação")
    @Test
    void deveFinalizarUmaSolicitacao() {
        var solicitacao = Solicitacao.builder()
                .idSolicitacao(1L)
                .tipo(TipoAtendimentoEnum.CARTOES)
                .assunto("Problema com cartão")
                .status(SolicitacaoStatusEnum.AGUARDANDO_ATENDIMENTO)
                .build();

        when(solicitacaoRepository.findById(any()))
                .thenReturn(Optional.of(solicitacao));

        var request = SolicitacaoAlterarRequestDTO.builder()
                .status(SolicitacaoStatusEnum.FINALIZADO)
                .build();

        solicitacaoService.finalizarSolicitacao(1L, 1L, request);

        verify(solicitacaoRepository).save(solicitacao);
    }

    @DisplayName("Deve retornar uma lista de solicitações")
    @Test
    void deveRetornarUmaListaDeSolicitacoes() {
        var solicitacao = Solicitacao.builder()
                .idSolicitacao(1L)
                .tipo(TipoAtendimentoEnum.CARTOES)
                .assunto("Problema com cartão")
                .status(SolicitacaoStatusEnum.AGUARDANDO_ATENDIMENTO)
                .build();

        when(solicitacaoRepository.findAll())
                .thenReturn(Collections.singletonList(solicitacao));

        var solicitacoes = solicitacaoService.findAll();

        assertThat(solicitacoes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @DisplayName("Deve retornar erro ao tentar atribuir uma solicitação inexiste")
    @Test
    void deveRetornarErroAoTentarAtribuirUmaSolicitacaoInexistente() {
        when(solicitacaoRepository.findById(any()))
                .thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> solicitacaoService.atribuirSolicitacao(1L, 1L));

        assertThat(throwable)
                .isNotNull()
                .hasMessage("Solicitação não encontrada.")
                .isExactlyInstanceOf(NaoEncontradoException.class);
    }

    @DisplayName("Deve atribuir uma solicitação")
    @Test
    void deveAtribuirUmaSolicitacao() {
        var solicitacao = Solicitacao.builder()
                .idSolicitacao(1L)
                .tipo(TipoAtendimentoEnum.CARTOES)
                .assunto("Problema com cartão")
                .status(SolicitacaoStatusEnum.AGUARDANDO_ATENDIMENTO)
                .build();

        when(solicitacaoRepository.findById(any()))
                .thenReturn(Optional.of(solicitacao));

        var atendente = Atendente.builder()
                .idAtendente(1L)
                .build();

        when(atendenteService.findById(any()))
                .thenReturn(atendente);

        solicitacaoService.atribuirSolicitacao(1L, 1L);

        verify(solicitacaoRepository).save(solicitacao);
    }

}
