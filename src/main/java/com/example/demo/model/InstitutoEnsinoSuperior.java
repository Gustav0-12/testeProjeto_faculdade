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
@Table(name = "tb_ies")
public class InstitutoEnsinoSuperior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sigla;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String emailCentral;

    @Column(nullable = false)
    private String telefoneCentral;

    @Column(name = "dataCadastro", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reitor_id", nullable = true)
    private Docente reitor;

    @OneToMany(mappedBy = "ies")
    private List<CentroAcademico> centrosAcademicos;
}
