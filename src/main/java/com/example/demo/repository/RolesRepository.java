package com.example.demo.repository;

import com.example.demo.model.Roles;
import com.example.demo.model.enums.TipoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByTipoPerfil(TipoPerfil tipoPerfil);
}
