package br.senac.sp.tads.dsw.adocategoriaproduto.dto;

public record ProdutoResponseDTO(
    Long id,
    String nome,
    Double preco,
    String nomeCategoria) {
}
