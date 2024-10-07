package com.ms.central.relacionamento.repository;

import com.ms.central.relacionamento.entity.Atendente;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtendenteRepository extends JpaRepository<Atendente, Long> {

    @Query("""
             SELECT a.idAtendente FROM Atendente a
             LEFT JOIN Solicitacao s ON a.idAtendente = s.atendente.idAtendente
             WHERE a.time = :tipo
             OR s.status = 'EM_ANDAMENTO'
             GROUP BY a.idAtendente
             HAVING COUNT(s.idSolicitacao) < 3
             ORDER BY MIN (s.dataSolicitacao) ASC
             LIMIT 1
            """)
    Optional<Long> findProximoAtendenteDisponivel(TipoAtendimentoEnum tipo);
}
