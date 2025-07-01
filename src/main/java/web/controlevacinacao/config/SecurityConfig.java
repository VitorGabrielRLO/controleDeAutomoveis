package web.controlevacinacao.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(configurer -> configurer
                // Permite acesso público a recursos estáticos e à página inicial
                .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/index.html").permitAll()
                // Define permissões de ADMIN para as rotas de cadastro, alteração e remoção
                .requestMatchers("/veiculos/cadastrar", "/veiculos/alterar/**", "/veiculos/remover/**").hasRole("ADMIN")
                .requestMatchers("/funcionarios/cadastrar", "/funcionarios/alterar/**", "/funcionarios/remover/**").hasRole("ADMIN")
                .requestMatchers("/usuarios/cadastrar").hasRole("ADMIN")
                .requestMatchers("/relatorios/**").hasRole("ADMIN")
                // Qualquer outra requisição precisa de autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true) // Redireciona para a raiz após o login
                .permitAll()
            )
            .logout(logout -> logout
    .logoutUrl("/logout")
    .logoutSuccessHandler(
        (request, response, authentication) -> response.addHeader("HX-Redirect", "/")) // <-- Alterado para "/"
    .invalidateHttpSession(true)
    .deleteCookies("JSESSIONID"))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // Busca o usuário pelo nome de usuário [cite: 311-313]
        manager.setUsersByUsernameQuery("select nome_usuario, senha, ativo from usuario where nome_usuario = ?");

        // Busca as permissões (roles) do usuário [cite: 314-318]
        manager.setAuthoritiesByUsernameQuery(
            "select u.nome_usuario, p.nome from usuario u " +
            "join usuario_papel up on u.codigo = up.codigo_usuario " +
            "join papel p on up.codigo_papel = p.codigo " +
            "where u.nome_usuario = ?");
            
        return manager;
    }

    @Bean
public PasswordEncoder passwordEncoder() {
    // Para o teste, vamos usar "noop" como o codificador padrão.
    String idEnconder = "noop"; 
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
    encoders.put("noop", NoOpPasswordEncoder.getInstance());
    PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idEnconder, encoders);
    return passwordEncoder;
}
}