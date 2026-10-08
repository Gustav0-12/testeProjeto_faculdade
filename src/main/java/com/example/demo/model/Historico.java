package com.example.demo.model;

import com.example.demo.model.enums.TipoAcao;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_historico")
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoAcao tipoAcao;

    @Column(nullable = false)
    private String entidade;

    @Column(nullable = false)
    private Long entidadeId;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public Historico(TipoAcao tipoAcao, LocalDateTime dataHora, String entidade, String motivo, Long entidadeId) {
        this.tipoAcao = tipoAcao;
        this.dataHora = dataHora;
        this.entidade = entidade;
        this.motivo = motivo;
        this.entidadeId = entidadeId;
    }
}
