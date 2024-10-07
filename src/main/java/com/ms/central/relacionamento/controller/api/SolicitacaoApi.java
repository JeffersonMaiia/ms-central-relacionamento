package com.ms.central.relacionamento.controller.api;

import com.ms.central.relacionamento.controller.request.SolicitacaoAlterarRequestDTO;
import com.ms.central.relacionamento.controller.request.SolicitacaoRequestDTO;
import com.ms.central.relacionamento.controller.response.SolicitacaoResponseDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@OpenAPIDefinition(info = @Info(
        title = "Solicitação API",
        version = "1.0",
        description = "API para gerenciamento solicitações de atendimento"))
public interface SolicitacaoApi {

    @Operation(summary = "Cadastra uma nova solicitação e envia para fila de atendimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    Long criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO solicitacaoRequestDTO);

    @Operation(summary = "Finaliza uma solicitação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "Solicitação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    void finalizarSolicitacao(@PathVariable Long idSolicitacao,
                              @RequestParam Long idAtendente,
                              @Valid @RequestBody SolicitacaoAlterarRequestDTO solicitacaoAlterarRequestDTO);

    @Operation(summary = "Lista todas as solicitações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    List<SolicitacaoResponseDTO> listarSolicitacoes();

    @Operation(summary = "Lista todas as solicitações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "204", description = "Solicitação ou Atendente não encontrada"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    void atribuirSolicitacao(@PathVariable Long idSolicitacao, @PathVariable Long idAtendente);
}
