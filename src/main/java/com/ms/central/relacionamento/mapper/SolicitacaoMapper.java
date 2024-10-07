package com.ms.central.relacionamento.mapper;

import com.ms.central.relacionamento.controller.request.SolicitacaoRequestDTO;
import com.ms.central.relacionamento.controller.response.SolicitacaoResponseDTO;
import com.ms.central.relacionamento.entity.Solicitacao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {

    Solicitacao toEntity(SolicitacaoRequestDTO solicitacaoRequestDTO);

    SolicitacaoResponseDTO toSolicitacaoResponseDTO(Solicitacao solicitacao);
}
