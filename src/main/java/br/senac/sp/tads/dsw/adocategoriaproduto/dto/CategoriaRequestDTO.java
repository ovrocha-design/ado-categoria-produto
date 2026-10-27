package br.senac.sp.tads.dsw.adocategoriaproduto.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO(
    @NotBlank String nome) {
}
