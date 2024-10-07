package com.ms.central.relacionamento.controller;

import com.ms.central.relacionamento.controller.api.AtendenteApi;
import com.ms.central.relacionamento.controller.request.AtendenteRequestDTO;
import com.ms.central.relacionamento.controller.response.AtendenteResponseDTO;
import com.ms.central.relacionamento.service.AtendenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/atendente")
public class AtendenteController implements AtendenteApi {

    private final AtendenteService atendenteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public Long novoAtendente(@Valid @RequestBody AtendenteRequestDTO atendenteRequestDTO) {
        log.info("AtendenteController.novoAtendente - Iniciando cadastro de novo atendente");
        return atendenteService.criarAtendente(atendenteRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<AtendenteResponseDTO> listarAtendentes() {
        log.info("AtendenteController.listarAtendentes - Listando atendentes");
       return atendenteService.findAll();
    }
}
