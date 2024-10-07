package com.ms.central.relacionamento.entity;

import com.ms.central.relacionamento.enums.TipoAtendimentoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "atendente")
public class Atendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atendente")
    private Long idAtendente;

    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoAtendimentoEnum time;

    @OneToMany
    @JoinColumn(name = "id_solicitacao")
    private List<Solicitacao> solicitacoes;
}
