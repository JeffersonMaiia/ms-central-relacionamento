package com.ms.central.relacionamento.mapper;

import com.ms.central.relacionamento.controller.request.AtendenteRequestDTO;
import com.ms.central.relacionamento.controller.response.AtendenteResponseDTO;
import com.ms.central.relacionamento.entity.Atendente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AtendeteMapper {

    Atendente toAtendente(AtendenteRequestDTO atendenteRequestDTO);

    AtendenteResponseDTO toAtendenteResponseDTO(Atendente atendente);
}
