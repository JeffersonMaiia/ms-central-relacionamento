package com.ms.central.relacionamento.controller.request;

import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SolicitacaoRequestDTO {

    @NotNull(message = "O tipo de atendimento é obrigatório")
    private TipoAtendimentoEnum tipo;

    @NotBlank(message = "O assunto é obrigatório")
    private String assunto;
}
