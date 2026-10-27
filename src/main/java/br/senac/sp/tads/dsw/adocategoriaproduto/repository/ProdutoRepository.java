package br.senac.sp.tads.dsw.adocategoriaproduto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senac.sp.tads.dsw.adocategoriaproduto.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
