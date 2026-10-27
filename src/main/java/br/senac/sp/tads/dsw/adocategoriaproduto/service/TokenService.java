package br.senac.sp.tads.dsw.adocategoriaproduto.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.senac.sp.tads.dsw.adocategoriaproduto.model.Usuario;

@Service
public class TokenService {

  // Le a chave secreta configurada no application.properties
  @Value("${api.security.token.secret}")
  private String secret;

  public String gerarToken(Usuario usuario) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer("API E-commerce TADS")
          .withSubject(usuario.getUsername())
          .withExpiresAt(gerarDataExpiracao())
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Erro ao gerar token JWT", exception);
    }
  }

  public String validarToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
          .withIssuer("API E-commerce TADS")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      return "";
    }
  }

  // O cracha vale por 2 horas
  private Instant gerarDataExpiracao() {
    return Instant.now().plus(2, ChronoUnit.HOURS);
  }

}
