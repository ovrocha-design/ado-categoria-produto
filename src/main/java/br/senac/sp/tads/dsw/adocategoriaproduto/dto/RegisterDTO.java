package br.senac.sp.tads.dsw.adocategoriaproduto.dto;

import br.senac.sp.tads.dsw.adocategoriaproduto.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
    @NotBlank String login,
    @NotBlank String senha,
    @NotNull UserRole role) {
}
