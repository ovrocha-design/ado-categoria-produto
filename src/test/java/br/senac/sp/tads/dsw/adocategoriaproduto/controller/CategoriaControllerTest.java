package br.senac.sp.tads.dsw.adocategoriaproduto.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.senac.sp.tads.dsw.adocategoriaproduto.dto.CategoriaRequestDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.model.Categoria;
import br.senac.sp.tads.dsw.adocategoriaproduto.repository.CategoriaRepository;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(roles = "ADMIN")
class CategoriaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CategoriaRepository repository;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void deveCriarCategoriaComSucesso() throws Exception {
    CategoriaRequestDTO dto = new CategoriaRequestDTO("Brinquedos");

    String jsonRequisicao = objectMapper.writeValueAsString(dto);

    mockMvc.perform(post("/api/categorias")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequisicao))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.nome").value("Brinquedos"));
  }

  @Test
  void deveRetornarErro400AoCriarCategoriaSemNome() throws Exception {
    CategoriaRequestDTO dto = new CategoriaRequestDTO("");

    String jsonRequisicao = objectMapper.writeValueAsString(dto);

    mockMvc.perform(post("/api/categorias")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequisicao))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deveListarTodasAsCategorias() throws Exception {
    Categoria categoria = new Categoria();
    categoria.setNome("Higiene");
    repository.save(categoria);

    mockMvc.perform(get("/api/categorias"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome").value("Higiene"));
  }

  @Test
  void deveBuscarCategoriaPorId() throws Exception {
    Categoria categoria = new Categoria();
    categoria.setNome("Alimentacao");
    Categoria categoriaSalva = repository.save(categoria);

    mockMvc.perform(get("/api/categorias/" + categoriaSalva.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Alimentacao"));
  }

  @Test
  void deveAtualizarCategoria() throws Exception {
    Categoria categoria = new Categoria();
    categoria.setNome("Acessorio");
    Categoria categoriaSalva = repository.save(categoria);

    CategoriaRequestDTO dto = new CategoriaRequestDTO("Acessorios");
    String jsonAtualizado = objectMapper.writeValueAsString(dto);

    mockMvc.perform(put("/api/categorias/" + categoriaSalva.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonAtualizado))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Acessorios"));
  }

  @Test
  void deveApagarCategoria() throws Exception {
    Categoria categoria = new Categoria();
    categoria.setNome("Limpeza");
    Categoria categoriaSalva = repository.save(categoria);

    mockMvc.perform(delete("/api/categorias/" + categoriaSalva.getId()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/categorias/" + categoriaSalva.getId()))
        .andExpect(status().isNotFound());
  }

}
