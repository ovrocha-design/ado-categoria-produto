package br.senac.sp.tads.dsw.adocategoriaproduto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.senac.sp.tads.dsw.adocategoriaproduto.dto.ProdutoRequestDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.dto.ProdutoResponseDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.service.ProdutoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

  private final ProdutoService service;

  public ProdutoController(ProdutoService service) {
    this.service = service;
  }

  @GetMapping
  public List<ProdutoResponseDTO> listarTodos() {
    return service.listarTodos();
  }

  @GetMapping("/{id}")
  public ProdutoResponseDTO buscarPorId(@PathVariable Long id) {
    return service.buscarPorId(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProdutoResponseDTO criar(@RequestBody @Valid ProdutoRequestDTO dto) {
    return service.criar(dto);
  }

  @PutMapping("/{id}")
  public ProdutoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
    return service.atualizar(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void apagar(@PathVariable Long id) {
    service.apagar(id);
  }

}
