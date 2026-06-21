package br.edu.ifsudeste.demo.security;

import br.edu.ifsudeste.demo.model.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(
                chaveAssinatura.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String gerarToken(Usuario usuario) {

        long expString = Long.parseLong(expiracao);

        LocalDateTime dataHoraExpiracao =
                LocalDateTime.now().plusDays(expString);

        Instant instant =
                dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();

        Date data = Date.from(instant);

        return Jwts.builder()
                .subject(usuario.getLogin())
                .expiration(data)
                .signWith(getSignKey())
                .compact();
    }

    private Claims obterClaims(String token) throws ExpiredJwtException {

        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean tokenValido(String token) {

        try {

            Claims claims = obterClaims(token);

            Date dataExpiracao = claims.getExpiration();

            LocalDateTime data =
                    dataExpiracao.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

            return !LocalDateTime.now().isAfter(data);

        } catch (Exception e) {

            return false;
        }
    }

    public String obterLoginUsuario(String token)
            throws ExpiredJwtException {

        return obterClaims(token).getSubject();
    }
}