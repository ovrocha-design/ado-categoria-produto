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

import br.senac.sp.tads.dsw.adocategoriaproduto.dto.CategoriaRequestDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.dto.CategoriaResponseDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.service.CategoriaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerToken")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

  private final CategoriaService service;

  public CategoriaController(CategoriaService service) {
    this.service = service;
  }

  @GetMapping
  public List<CategoriaResponseDTO> listarTodos() {
    return service.listarTodos();
  }

  @GetMapping("/{id}")
  public CategoriaResponseDTO buscarPorId(@PathVariable Long id) {
    return service.buscarPorId(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoriaResponseDTO criar(@RequestBody @Valid CategoriaRequestDTO dto) {
    return service.criar(dto);
  }

  @PutMapping("/{id}")
  public CategoriaResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO dto) {
    return service.atualizar(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void apagar(@PathVariable Long id) {
    service.apagar(id);
  }

}
