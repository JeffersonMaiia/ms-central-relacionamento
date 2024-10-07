package com.ms.central.relacionamento.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ms.central.relacionamento.controller.request.AtendenteRequestDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AtendenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @DisplayName("Deve retornar erro ao criar um novo atendente com nome vazio ou time nulo")
    @Test
    void deveRetornarErroAoCriarAtendenteComNomeVazio() throws Exception {
        var atendenteRequestDTO = AtendenteRequestDTO.builder()
                .nome("")
                .time(null)
                .build();

        ResultActions perform = this.mockMvc.perform(post("/v1/atendente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atendenteRequestDTO)))
                .andDo(print());

        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("O nome é obrigatório"))
                .andExpect(jsonPath("$.time").value("O time é obrigatório"));
    }

    @DisplayName("Deve criar um novo atendente")
    @Test
    void deveCriarAtendente() throws Exception {
        var atendenteRequestDTO = AtendenteRequestDTO.builder()
                .nome("João")
                .time(TipoAtendimentoEnum.CARTOES)
                .build();

        ResultActions perform = this.mockMvc.perform(post("/v1/atendente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atendenteRequestDTO)))
                .andDo(print());

        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @DisplayName("Deve retornar a lista de atendentes")
    @Test
    void deveListarAtendentes() throws Exception {
        ResultActions perform = this.mockMvc.perform(get("/v1/atendente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].idAtendente").value(1))
                .andExpect(jsonPath("$[0].nome").value("ATENDENTE-1"))
                .andExpect(jsonPath("$[0].time").value("CARTOES"))
                .andExpect(jsonPath("$[1].idAtendente").value(2))
                .andExpect(jsonPath("$[1].nome").value("ATENDENTE-2"))
                .andExpect(jsonPath("$[1].time").value("EMPRESTIMOS"))
                .andExpect(jsonPath("$[2].idAtendente").value(3))
                .andExpect(jsonPath("$[2].nome").value("ATENDENTE-3"))
                .andExpect(jsonPath("$[2].time").value("OUTROS"));
    }
}
