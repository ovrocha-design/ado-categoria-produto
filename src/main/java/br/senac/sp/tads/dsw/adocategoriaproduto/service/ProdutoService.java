package br.senac.sp.tads.dsw.adocategoriaproduto.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.senac.sp.tads.dsw.adocategoriaproduto.dto.ProdutoRequestDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.dto.ProdutoResponseDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.model.Categoria;
import br.senac.sp.tads.dsw.adocategoriaproduto.model.Produto;
import br.senac.sp.tads.dsw.adocategoriaproduto.repository.CategoriaRepository;
import br.senac.sp.tads.dsw.adocategoriaproduto.repository.ProdutoRepository;

@Service
public class ProdutoService {

  private final ProdutoRepository produtoRepository;
  private final CategoriaRepository categoriaRepository;

  public ProdutoService(ProdutoRepository produtoRepository,
      CategoriaRepository categoriaRepository) {
    this.produtoRepository = produtoRepository;
    this.categoriaRepository = categoriaRepository;
  }

  public List<ProdutoResponseDTO> listarTodos() {
    return produtoRepository.findAll()
        .stream()
        .map(p -> new ProdutoResponseDTO(
            p.getId(), p.getNome(), p.getPreco(), p.getCategoria().getNome()))
        .toList();
  }

  public ProdutoResponseDTO buscarPorId(Long id) {
    Produto p = produtoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new ProdutoResponseDTO(p.getId(), p.getNome(), p.getPreco(), p.getCategoria().getNome());
  }

  public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
    Categoria c = categoriaRepository.findById(dto.categoriaId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    Produto p = new Produto();
    p.setNome(dto.nome());
    p.setPreco(dto.preco());
    p.setCategoria(c);

    p = produtoRepository.save(p);

    return new ProdutoResponseDTO(p.getId(), p.getNome(), p.getPreco(), p.getCategoria().getNome());
  }

  public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
    Produto p = produtoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    Categoria c = categoriaRepository.findById(dto.categoriaId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    p.setNome(dto.nome());
    p.setPreco(dto.preco());
    p.setCategoria(c);

    p = produtoRepository.save(p);

    return new ProdutoResponseDTO(p.getId(), p.getNome(), p.getPreco(), p.getCategoria().getNome());
  }

  public void apagar(Long id) {
    if (!produtoRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    produtoRepository.deleteById(id);
  }

}
