package com.ms.central.relacionamento.entity;

import com.ms.central.relacionamento.enums.SolicitacaoStatusEnum;
import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "solicitacao")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitacao")
    private Long idSolicitacao;

    @Enumerated(EnumType.STRING)
    private TipoAtendimentoEnum tipo;

    @Column(name = "assunto")
    private String assunto;

    @Enumerated(EnumType.STRING)
    private SolicitacaoStatusEnum status;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;

    @ManyToOne
    @JoinColumn(name = "id_atendente")
    private Atendente atendente;

    @PrePersist
    public void prePersist() {
        this.dataSolicitacao = LocalDateTime.now();
        if(Objects.nonNull(atendente)){
            this.status = SolicitacaoStatusEnum.EM_ANDAMENTO;
        } else {
            this.status = SolicitacaoStatusEnum.AGUARDANDO_ATENDIMENTO;
        }
    }

    public void finalizar() {
        this.status = SolicitacaoStatusEnum.FINALIZADO;
    }

    public void emAtendimento(Long idAtendente) {
        this.atendente = new Atendente();
        this.atendente.setIdAtendente(idAtendente);
        this.status = SolicitacaoStatusEnum.EM_ANDAMENTO;
    }
}
