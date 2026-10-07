package com.bytebank.bytebank_patterns.service;

import com.bytebank.bytebank_patterns.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String gerarToken(Usuario usuario) {

        Instant agora = Instant.now();
        Instant expiracao = agora.plus(2, ChronoUnit.HOURS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("bytebank")
                .issuedAt(agora)
                .expiresAt(expiracao)
                .subject(usuario.getEmail())
                .claim("usuarioId", usuario.getId())
                .claim("contaId", usuario.getConta().getId())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        JwtEncoderParameters parametros =
                JwtEncoderParameters.from(header, claims);

        return jwtEncoder
                .encode(parametros)
                .getTokenValue();
    }
}
