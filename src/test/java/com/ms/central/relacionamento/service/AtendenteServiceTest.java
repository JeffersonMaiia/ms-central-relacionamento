package com.ms.central.relacionamento.service;

import com.ms.central.relacionamento.controller.request.AtendenteRequestDTO;
import com.ms.central.relacionamento.entity.Atendente;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import com.ms.central.relacionamento.exception.NaoEncontradoException;
import com.ms.central.relacionamento.mapper.AtendeteMapper;
import com.ms.central.relacionamento.repository.AtendenteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AtendenteServiceTest {

    @InjectMocks
    private AtendenteService atendenteService;

    @Mock
    private AtendenteRepository atendenteRepository;

    @Mock
    private AtendeteMapper atendeteMapper;

    @DisplayName("Deve criar um atendente")
    @Test
    void deveCriarUmAtendente() {
        var request = AtendenteRequestDTO.builder()
                .nome("João")
                .time(TipoAtendimentoEnum.CARTOES)
                .build();

        var atendente = Atendente.builder()
                .idAtendente(1L)
                .nome("João")
                .time(TipoAtendimentoEnum.CARTOES)
                .build();

        when(atendenteRepository.save(any(Atendente.class))).thenReturn(atendente);
        when(atendeteMapper.toAtendente(any(AtendenteRequestDTO.class))).thenReturn(atendente);
        when(atendenteRepository.save(any(Atendente.class))).thenReturn(atendente);

        Long response = atendenteService.criarAtendente(request);

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(1L);
    }

    @DisplayName("Deve buscar um atendente disponível")
    @Test
    void deveBuscarUmAtendenteDisponivel() {
        when(atendenteRepository.findProximoAtendenteDisponivel(any(TipoAtendimentoEnum.class)))
                .thenReturn(Optional.of(1L));

        var response = atendenteService.buscarProximoAtendenteDisponivel(TipoAtendimentoEnum.CARTOES);

        assertThat(response).isNotNull();
    }

    @DisplayName("Deve buscar um atendente por id")
    @Test
    void deveBuscarUmAtendentePorId() {
        var atendente = Atendente.builder()
                .idAtendente(1L)
                .nome("João")
                .time(TipoAtendimentoEnum.CARTOES)
                .build();

        when(atendenteRepository.findById(1L)).thenReturn(Optional.of(atendente));

        var response = atendenteService.findById(1L);

        assertThat(response).isNotNull();
    }

    @DisplayName("Deve retornar erro de não encontrado ao buscar um atendente por id")
    @Test
    void deveRetornarErroNaoEncontradoAoBuscarUmAtendentePorId() {
        when(atendenteRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> atendenteService.findById(1L));

        assertThat(throwable)
                .isNotNull()
                .hasMessage("Atendente não encontrado.")
                .isExactlyInstanceOf(NaoEncontradoException.class);
    }

    @DisplayName("Deve retornar uma lista de atendentes")
    @Test
    void deveRetornarUmaListaDeAtendentes() {
        var atendente = Atendente.builder()
                .idAtendente(1L)
                .nome("João")
                .time(TipoAtendimentoEnum.CARTOES)
                .build();

        when(atendenteRepository.findAll())
                .thenReturn(List.of(atendente));

        var response = atendenteService.findAll();

        assertThat(response).isNotNull();
        assertThat(response).isNotEmpty();
        assertThat(response).hasSize(1);
    }

}
