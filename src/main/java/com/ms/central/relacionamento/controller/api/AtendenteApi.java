package com.ms.central.relacionamento.controller.api;

import com.ms.central.relacionamento.controller.request.AtendenteRequestDTO;
import com.ms.central.relacionamento.controller.response.AtendenteResponseDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@OpenAPIDefinition(info = @Info(
        title = "Atendente API",
        version = "1.0",
        description = "API para gerenciamento de atendentes"))
public interface AtendenteApi {

    @Operation(summary = "Cadastra um novo atendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    Long novoAtendente(@Valid @RequestBody AtendenteRequestDTO atendenteRequestDTO);

    @Operation(summary = "Lista todas os atendentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    List<AtendenteResponseDTO> listarAtendentes();
}
