package com.example.demo.service;

import com.example.demo.config.TokenProvider;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private Long expirationTime;

    public TokenResponseDto login(LoginRequestDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));
            String token = tokenProvider.gerarToken(authentication);

            return new TokenResponseDto(token, expirationTime);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciais inválidas");
        } catch (Exception e) {
            throw e;
        }
    }
}
