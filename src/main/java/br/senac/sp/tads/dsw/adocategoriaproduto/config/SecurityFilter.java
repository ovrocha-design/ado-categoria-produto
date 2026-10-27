package br.senac.sp.tads.dsw.adocategoriaproduto.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.senac.sp.tads.dsw.adocategoriaproduto.repository.UsuarioRepository;
import br.senac.sp.tads.dsw.adocategoriaproduto.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final UsuarioRepository usuarioRepository;

  public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
    this.tokenService = tokenService;
    this.usuarioRepository = usuarioRepository;
  }

  // Roda em CADA requisicao, antes de chegar aos controllers
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 1. Pega o token do cabecalho Authorization
    var token = this.recuperarToken(request);

    if (token != null) {
      // 2. Confere a assinatura e a validade; devolve o login
      var login = tokenService.validarToken(token);

      if (!login.isBlank()) {
        // 3. Busca o usuario no banco para saber os crachas (roles)
        UserDetails usuario = usuarioRepository.findByLogin(login).orElse(null);

        if (usuario != null) {
          // 4. Marca o usuario como autenticado so nesta requisicao
          var authentication = new UsernamePasswordAuthenticationToken(
              usuario, null, usuario.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }

    // 5. Segue em frente (se a rota for privada e nao tiver token, o Spring barra)
    filterChain.doFilter(request, response);
  }

  private String recuperarToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return null;
    }
    return authHeader.substring(7);
  }

}
