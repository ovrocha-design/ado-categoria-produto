package br.senac.sp.tads.dsw.adocategoriaproduto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

// Cria o botao "Authorize" no Swagger para mandar o cabecalho Authorization: Bearer <token>
@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@SpringBootApplication
public class AdoCategoriaProdutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdoCategoriaProdutoApplication.class, args);
	}

}
