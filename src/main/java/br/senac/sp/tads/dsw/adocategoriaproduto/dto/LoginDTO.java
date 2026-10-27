package br.senac.sp.tads.dsw.adocategoriaproduto.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank String login,
    @NotBlank String senha) {
}
