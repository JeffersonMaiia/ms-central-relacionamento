package com.ms.central.relacionamento.controller;

import com.ms.central.relacionamento.controller.api.SolicitacaoApi;
import com.ms.central.relacionamento.controller.request.SolicitacaoAlterarRequestDTO;
import com.ms.central.relacionamento.controller.request.SolicitacaoRequestDTO;
import com.ms.central.relacionamento.controller.response.SolicitacaoResponseDTO;
import com.ms.central.relacionamento.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/solicitacao")
public class SolicitacaoController implements SolicitacaoApi {

    private final SolicitacaoService solicitacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public Long criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO solicitacaoRequestDTO) {
        log.info("SolicitacaoController.criarSolicitacao - Iniciando cadastro de nova solicitação");

        return solicitacaoService.criarSolicitacao(solicitacaoRequestDTO);
    }

    @PatchMapping("/{idSolicitacao}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public void finalizarSolicitacao(@PathVariable Long idSolicitacao,
                                     @RequestParam Long idAtendente,
                                     @Valid @RequestBody SolicitacaoAlterarRequestDTO solicitacaoAlterarRequestDTO) {

        log.info("SolicitacaoController.finalizarSolicitacao - Iniciando finalização de solicitação idSolicitacao={} idAtendente={}",
                idSolicitacao, idAtendente);

        solicitacaoService.finalizarSolicitacao(idSolicitacao, idAtendente, solicitacaoAlterarRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Override
    public List<SolicitacaoResponseDTO> listarSolicitacoes() {
        log.info("SolicitacaoController.listarSolicitacoes - Listando todas as solicitações");

        return solicitacaoService.findAll();
    }

    @PatchMapping("/{idSolicitacao}/atendente/{idAtendente}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public void atribuirSolicitacao(@PathVariable Long idSolicitacao, @PathVariable Long idAtendente) {
        log.info("SolicitacaoController.atribuirSolicitacao - Atribuindo solicitação idSolicitacao={} para atendente idAtendente={}",
                idSolicitacao, idAtendente);

        solicitacaoService.atribuirSolicitacao(idSolicitacao, idAtendente);
    }
}
