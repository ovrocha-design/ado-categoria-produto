package br.senac.sp.tads.dsw.adocategoriaproduto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.senac.sp.tads.dsw.adocategoriaproduto.dto.LoginDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.dto.RegisterDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.dto.TokenDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.model.Usuario;
import br.senac.sp.tads.dsw.adocategoriaproduto.service.AutenticacaoService;
import br.senac.sp.tads.dsw.adocategoriaproduto.service.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

  private final AutenticacaoService service;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  public AutenticacaoController(
      AutenticacaoService service,
      AuthenticationManager authenticationManager,
      TokenService tokenService) {
    this.service = service;
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public void registrar(@RequestBody @Valid RegisterDTO dto) {
    service.registrar(dto);
  }

  @PostMapping("/login")
  public TokenDTO login(@RequestBody @Valid LoginDTO dto) {
    // 1. Empacota o login e a senha digitados
    var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());

    // 2. O Spring chama o AutenticacaoService e confere a senha (BCrypt)
    Authentication auth = this.authenticationManager.authenticate(usernamePassword);

    // 3. Senha certa: gera o cracha (token JWT)
    String token = tokenService.gerarToken((Usuario) auth.getPrincipal());

    // 4. Devolve o token em JSON
    return new TokenDTO(token);
  }

}
