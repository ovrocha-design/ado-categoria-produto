package br.senac.sp.tads.dsw.adocategoriaproduto.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.senac.sp.tads.dsw.adocategoriaproduto.dto.RegisterDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.model.Usuario;
import br.senac.sp.tads.dsw.adocategoriaproduto.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

  private final UsuarioRepository repository;
  private final PasswordEncoder passwordEncoder;

  public AutenticacaoService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  // O Spring Security chama este metodo no login para buscar o usuario no banco
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByLogin(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
  }

  public void registrar(RegisterDTO dto) {
    if (repository.existsByLogin(dto.login())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este login já está em uso!");
    }
    // Criptografa a senha com BCrypt antes de salvar no banco
    String senhaCriptografada = passwordEncoder.encode(dto.senha());
    Usuario novoUsuario = new Usuario(dto.login(), senhaCriptografada, dto.role());
    repository.save(novoUsuario);
  }

}
