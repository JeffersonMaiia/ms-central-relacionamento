package com.ms.central.relacionamento.controller.response;

import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtendenteResponseDTO {

    private Long idAtendente;
    private String nome;
    private TipoAtendimentoEnum time;
}
