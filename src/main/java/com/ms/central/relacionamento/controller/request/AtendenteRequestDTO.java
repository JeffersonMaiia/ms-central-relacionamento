package com.ms.central.relacionamento.controller.request;

import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtendenteRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "O time é obrigatório")
    private TipoAtendimentoEnum time;
}
