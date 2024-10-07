package com.ms.central.relacionamento.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoAtendimentoEnum {

    CARTOES("Problemas com cartão"),
    EMPRESTIMOS("Contratação de empréstimo"),
    OUTROS("Outros Assuntos");

    private final String assunto;
}
