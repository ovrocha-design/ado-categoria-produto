package br.senac.sp.tads.dsw.adocategoriaproduto.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.senac.sp.tads.dsw.adocategoriaproduto.dto.ProdutoRequestDTO;
import br.senac.sp.tads.dsw.adocategoriaproduto.model.Categoria;
import br.senac.sp.tads.dsw.adocategoriaproduto.model.Produto;
import br.senac.sp.tads.dsw.adocategoriaproduto.repository.CategoriaRepository;
import br.senac.sp.tads.dsw.adocategoriaproduto.repository.ProdutoRepository;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProdutoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private CategoriaRepository categoriaRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private Categoria categoriaPadrao;

  @BeforeEach
  void setUp() {
    Categoria categoria = new Categoria();
    categoria.setNome("Brinquedos");
    categoriaPadrao = categoriaRepository.save(categoria);
  }

  @Test
  void deveCriarProdutoComSucesso() throws Exception {
    ProdutoRequestDTO dto = new ProdutoRequestDTO("Bola de borracha", 25.90, categoriaPadrao.getId());

    String jsonRequisicao = objectMapper.writeValueAsString(dto);

    mockMvc.perform(post("/api/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequisicao))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.nome").value("Bola de borracha"))
        .andExpect(jsonPath("$.nomeCategoria").value("Brinquedos"));
  }

  @Test
  void deveRetornarErro400AoCriarProdutoComPrecoNegativo() throws Exception {
    ProdutoRequestDTO dto = new ProdutoRequestDTO("Osso", -10.0, categoriaPadrao.getId());

    String jsonRequisicao = objectMapper.writeValueAsString(dto);

    mockMvc.perform(post("/api/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequisicao))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deveListarTodosOsProdutos() throws Exception {
    Produto produto = new Produto();
    produto.setNome("Mordedor");
    produto.setPreco(45.90);
    produto.setCategoria(categoriaPadrao);
    produtoRepository.save(produto);

    mockMvc.perform(get("/api/produtos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome").value("Mordedor"));
  }

  @Test
  void deveBuscarProdutoPorId() throws Exception {
    Produto produto = new Produto();
    produto.setNome("Arranhador");
    produto.setPreco(39.90);
    produto.setCategoria(categoriaPadrao);
    Produto produtoSalvo = produtoRepository.save(produto);

    mockMvc.perform(get("/api/produtos/" + produtoSalvo.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Arranhador"));
  }

  @Test
  void deveAtualizarProduto() throws Exception {
    Produto produto = new Produto();
    produto.setNome("Coleira");
    produto.setPreco(30.0);
    produto.setCategoria(categoriaPadrao);
    Produto produtoSalvo = produtoRepository.save(produto);

    ProdutoRequestDTO dto = new ProdutoRequestDTO("Coleira", 35.0, categoriaPadrao.getId());
    String jsonAtualizado = objectMapper.writeValueAsString(dto);

    mockMvc.perform(put("/api/produtos/" + produtoSalvo.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonAtualizado))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.preco").value(35.0));
  }

  @Test
  void deveApagarProduto() throws Exception {
    Produto produto = new Produto();
    produto.setNome("Shampoo");
    produto.setPreco(29.90);
    produto.setCategoria(categoriaPadrao);
    Produto produtoSalvo = produtoRepository.save(produto);

    mockMvc.perform(delete("/api/produtos/" + produtoSalvo.getId()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/produtos/" + produtoSalvo.getId()))
        .andExpect(status().isNotFound());
  }

}
