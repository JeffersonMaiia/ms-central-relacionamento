package com.ms.central.relacionamento.controller.response;

import com.ms.central.relacionamento.enums.SolicitacaoStatusEnum;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitacaoResponseDTO {

    private Long idSolicitacao;
    private TipoAtendimentoEnum tipo;
    private String assunto;
    private SolicitacaoStatusEnum status;
    private LocalDateTime dataSolicitacao;
    private SolicitacaoAtendenteResponseDTO atendente;
}
