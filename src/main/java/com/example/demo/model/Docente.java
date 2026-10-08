package com.example.demo.model;

import com.example.demo.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_docente")
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String matricula;

    @Column(nullable = false)
    private String telefone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_academico_id", nullable = false)
    private CentroAcademico centroAcademico;
}
