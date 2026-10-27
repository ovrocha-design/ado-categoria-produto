package br.senac.sp.tads.dsw.adocategoriaproduto.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.senac.sp.tads.dsw.adocategoriaproduto.dto.CategoriaRequestDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.dto.CategoriaResponseDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.model.Categoria;
import br.senac.sp.tads.dsw.adocategoriaproduto.repository.CategoriaRepository;

@Service
public class CategoriaService {

  private final CategoriaRepository repository;

  public CategoriaService(CategoriaRepository repository) {
    this.repository = repository;
  }

  public List<CategoriaResponseDTO> listarTodos() {
    return repository.findAll()
        .stream()
        .map(c -> new CategoriaResponseDTO(c.getId(), c.getNome()))
        .toList();
  }

  public CategoriaResponseDTO buscarPorId(Long id) {
    Categoria c = repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new CategoriaResponseDTO(c.getId(), c.getNome());
  }

  public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
    Categoria c = new Categoria();
    c.setNome(dto.nome());

    c = repository.save(c);

    return new CategoriaResponseDTO(c.getId(), c.getNome());
  }

  public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
    Categoria c = repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    c.setNome(dto.nome());

    c = repository.save(c);

    return new CategoriaResponseDTO(c.getId(), c.getNome());
  }

  public void apagar(Long id) {
    if (!repository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    repository.deleteById(id);
  }

}
