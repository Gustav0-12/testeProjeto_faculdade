package com.example.demo.model;

import com.example.demo.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_monitoria")
public class Monitoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Docente docente;

    @ManyToOne
    @JoinColumn(name = "discente_id")
    private Discente discente;

    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String semestre;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @OneToMany(mappedBy = "monitoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonitoriaHorario> horarios;
}
