package com.ms.central.relacionamento.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SolicitacaoStatusEnum {

    AGUARDANDO_ATENDIMENTO("Aguardando atendimento"),
    EM_ANDAMENTO("Em andamento"),
    FINALIZADO("Finalizado");

    private final String status;
}
