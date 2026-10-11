package com.example.demo.config;

import com.example.demo.model.Roles;
import com.example.demo.model.Usuario;
import com.example.demo.model.enums.TipoPerfil;
import com.example.demo.repository.RolesRepository;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class AdminInicialConfig {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository roleRepository;

    @Bean
    CommandLineRunner criarAdminInicial() {
        return args -> {

            for (TipoPerfil tipo : TipoPerfil.values()) {
                roleRepository.findByTipoPerfil(tipo)
                        .orElseGet(() -> roleRepository.save(new Roles(null, tipo)));
            }

            if (usuarioRepository.existsByEmail("admin@monitoria.com")) {
                return;
            }

            Roles roleAdmin = roleRepository.findByTipoPerfil(TipoPerfil.ROLE_ADMINISTRADOR)
                    .orElseThrow(() -> new RuntimeException(
                            "ROLE_ADMINISTRADOR não encontrada"
                    ));

            Usuario admin = new Usuario();

            admin.setNome("Administrador");
            admin.setEmail("admin@monitoria.com");
            admin.setSenha(passwordEncoder.encode("123"));
            admin.setDataCadastro(LocalDateTime.now());

            admin.setPerfis(Set.of(roleAdmin));

            usuarioRepository.save(admin);
        };
    }
}