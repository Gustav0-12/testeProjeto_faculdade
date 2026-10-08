package com.example.demo.model;

import com.example.demo.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_ca")
public class CentroAcademico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String emailCentral;

    @Column(nullable = false)
    private String telefoneCentral;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ATIVO;

    @Column(name = "dataCadastro")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordenador_id")
    private Docente coordenador;

    @OneToMany(mappedBy = "centroAcademico")
    private List<Docente> docentes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ies_id", nullable = false)
    private InstitutoEnsinoSuperior ies;

    @OneToMany(mappedBy = "centroAcademico")
    private List<Curso> cursos;

}
