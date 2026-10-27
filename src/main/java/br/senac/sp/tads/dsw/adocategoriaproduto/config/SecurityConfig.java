package br.senac.sp.tads.dsw.adocategoriaproduto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final SecurityFilter securityFilter;

  public SecurityConfig(SecurityFilter securityFilter) {
    this.securityFilter = securityFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        // Deixa o console do H2 abrir (ele usa frames)
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        // API sem sessao: cada requisicao mostra o seu cracha (token)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            // 1. Rotas livres: Swagger, H2, pagina de erro, cadastro e login
            .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/h2-console/**", "/error")
            .permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
            // 2. So ADMIN pode cadastrar, alterar e apagar
            .requestMatchers(HttpMethod.POST, "/api/produtos/**", "/api/categorias/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/produtos/**", "/api/categorias/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/produtos/**", "/api/categorias/**").hasRole("ADMIN")
            // 3. O resto (ex.: GET) so precisa estar logado (USER ou ADMIN)
            .anyRequest().authenticated())
        // O nosso filtro do cracha roda ANTES do filtro de login padrao do Spring
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
