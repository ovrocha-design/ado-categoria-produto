package br.senac.sp.tads.dsw.adocategoriaproduto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.senac.sp.tads.dsw.adocategoriaproduto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  boolean existsByLogin(String login);

  Optional<UserDetails> findByLogin(String login);

}
