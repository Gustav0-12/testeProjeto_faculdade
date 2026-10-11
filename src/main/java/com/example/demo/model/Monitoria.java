package com.example.demo.model;

import com.example.demo.model.enums.DiaSemana;
import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.StatusMonitoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne
    @JoinColumn(name = "discente_id", nullable = false)
    private Discente discente;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(nullable = false, length = 6)
    private String semestre;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DiaSemana> dias = new HashSet<>();

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "horario_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "horario_fim", nullable = false)
    private LocalTime horaFim;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status", nullable = false)
    private StatusMonitoria status = StatusMonitoria.ATIVA;

}
