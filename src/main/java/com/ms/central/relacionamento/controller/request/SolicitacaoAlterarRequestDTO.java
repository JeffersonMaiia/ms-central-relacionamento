package com.ms.central.relacionamento.controller.request;

import com.ms.central.relacionamento.enums.SolicitacaoStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoAlterarRequestDTO {

    @NotNull(message = "O status da solicitação é obrigatório")
    private SolicitacaoStatusEnum status;
}
