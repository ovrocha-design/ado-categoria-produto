package br.senac.sp.tads.dsw.adocategoriaproduto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoRequestDTO(
    @NotBlank String nome,
    @NotNull @Positive Double preco,
    @NotNull Long categoriaId) {
}
