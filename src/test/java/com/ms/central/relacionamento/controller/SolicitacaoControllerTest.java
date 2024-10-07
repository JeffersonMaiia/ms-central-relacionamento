package com.ms.central.relacionamento.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ms.central.relacionamento.controller.request.SolicitacaoAlterarRequestDTO;
import com.ms.central.relacionamento.controller.request.SolicitacaoRequestDTO;
import com.ms.central.relacionamento.enums.SolicitacaoStatusEnum;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class SolicitacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @DisplayName("Deve criar uma nova solicitação")
    @Test
    void deveCriarSolicitacao() throws Exception {
        var solicitacaoRequestDTO = SolicitacaoRequestDTO.builder()
                .tipo(TipoAtendimentoEnum.CARTOES)
                .assunto("Problema com cartão")
                .build();

        ResultActions perform = this.mockMvc.perform(post("/v1/solicitacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitacaoRequestDTO)))
                .andDo(print());

        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @DisplayName("Deve retornar erro ao criar uma nova solicitação com assunto vazio e tipo de atendimento nulo")
    @Test
    void deveRetornarErroAoCriarSolicitacaoComAssuntoVazio() throws Exception {
        var solicitacaoRequestDTO = SolicitacaoRequestDTO.builder()
                .assunto("")
                .build();

        ResultActions perform = this.mockMvc.perform(post("/v1/solicitacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitacaoRequestDTO)))
                .andDo(print());

        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.assunto").value("O assunto é obrigatório"))
                .andExpect(jsonPath("$.tipo").value("O tipo de atendimento é obrigatório"));
    }

    @DisplayName("Deve retornar erro ao finalizar uma solicitacao com status diferente de FINALIZADO")
    @Test
    void deveRetornarErroAoFinalizarSolicitacaoComStatusDiferenteDeFinalizado() throws Exception {
        var requestDTO = SolicitacaoAlterarRequestDTO.builder()
                .status(SolicitacaoStatusEnum.AGUARDANDO_ATENDIMENTO)
                .build();

        ResultActions perform = this.mockMvc.perform(patch("/v1/solicitacao/1")
                        .queryParam("idAtendente", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print());

        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("O status apenas pode ser FINALIZADO."));
    }

    @DisplayName("Deve finalizar uma solicitação")
    @Test
    void deveFinalizarSolicitacao() throws Exception {
        var requestDTO = SolicitacaoAlterarRequestDTO.builder()
                .status(SolicitacaoStatusEnum.FINALIZADO)
                .build();

        ResultActions perform = this.mockMvc.perform(patch("/v1/solicitacao/1")
                        .queryParam("idAtendente", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print());

        perform.andExpect(status().isOk());
    }

    @DisplayName("Deve listar todas as solicitações deve verificar se a lista contem 3 solicitações")
    @Test
    void deveListarSolicitacoes() throws Exception {
        ResultActions perform = this.mockMvc.perform(get("/v1/solicitacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.[0].idSolicitacao").value(1))
                .andExpect(jsonPath("$.[0].tipo").value("CARTOES"))
                .andExpect(jsonPath("$.[0].assunto").value("Problema no cartão"))
                .andExpect(jsonPath("$.[0].status").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.[0].dataSolicitacao").exists())
                .andExpect(jsonPath("$.[0].atendente.idAtendente").value(1))
                .andExpect(jsonPath("$.[0].atendente.nome").value("ATENDENTE-1"))
                .andExpect(jsonPath("$.[1].idSolicitacao").value(2))
                .andExpect(jsonPath("$.[1].tipo").value("EMPRESTIMOS"))
                .andExpect(jsonPath("$.[1].assunto").value("Problema com emprestimo"))
                .andExpect(jsonPath("$.[1].status").value("AGUARDANDO_ATENDIMENTO"))
                .andExpect(jsonPath("$.[1].dataSolicitacao").exists())
                .andExpect(jsonPath("$.[1].atendente").doesNotExist());



    }

    @DisplayName("Deve retornar erro ao atribuir uma solicitacao inexistente")
    @Test
    void deveRetornarErroAoAtribuirSolicitacaoInexistente() throws Exception {
        ResultActions perform = this.mockMvc.perform(patch("/v1/solicitacao/999/atendente/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        perform.andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value("Solicitação não encontrada."));
    }

    @DisplayName("Deve retornar erro ao atribuir uma solicitacao para um atendente inexistente")
    @Test
    void deveRetornarErroAoAtribuirSolicitacaoParaAtendenteInexistente() throws Exception {
        ResultActions perform = this.mockMvc.perform(patch("/v1/solicitacao/1/atendente/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        perform.andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value("Atendente não encontrado."));
    }

    @DisplayName("Deve atribuir uma solicitação para um atendente")
    @Test
    void deveAtribuirSolicitacaoParaAtendente() throws Exception {
        ResultActions perform = this.mockMvc.perform(patch("/v1/solicitacao/2/atendente/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        perform.andExpect(status().isOk());
    }
}
