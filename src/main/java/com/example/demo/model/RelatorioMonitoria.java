package com.example.demo.model;

import com.example.demo.model.enums.ModalidadeMonitoria;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_relatorio_monitoria")
public class RelatorioMonitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "monitoria_id", nullable = false, unique = true)
    private Monitoria monitoria;

    @Column(nullable = false, length = 3000)
    private String ocorrencias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadeMonitoria modalidade;

    private String local;

    @Column(nullable = false, length = 1000)
    private String frequencia;

    @Column(length = 3000)
    private String consideracoes;
}