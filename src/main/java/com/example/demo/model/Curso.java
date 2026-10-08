package com.example.demo.model;

import com.example.demo.model.enums.Status;
import com.example.demo.model.enums.Turno;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sigla;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @Column(name = "dataCadastro")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;

    @Enumerated(EnumType.STRING)
    private Set<Turno> turnos;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordenador_id", nullable = false)
    private Docente coordenador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_academico_id", nullable = false)
    private CentroAcademico centroAcademico;

    @OneToMany(mappedBy = "curso")
    private List<Disciplina> disciplinas;

    @OneToMany(mappedBy = "curso")
    private List<Discente> discentes;

}
