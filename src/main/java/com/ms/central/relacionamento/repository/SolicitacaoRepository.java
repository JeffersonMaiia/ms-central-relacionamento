package com.ms.central.relacionamento.repository;

import com.ms.central.relacionamento.entity.Solicitacao;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    @Query("""
            SELECT s FROM Solicitacao s
            WHERE s.status = 'AGUARDANDO_ATENDIMENTO'
            AND s.tipo = :tipo
            ORDER BY s.dataSolicitacao
            LIMIT 1
            """)
    Optional<Solicitacao> findProximaSolicitacaoByTipo(TipoAtendimentoEnum tipo);
}
