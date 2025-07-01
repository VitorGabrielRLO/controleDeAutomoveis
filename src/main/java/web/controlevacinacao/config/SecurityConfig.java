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
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/index.html").permitAll()
                        .requestMatchers("/veiculos/cadastrar", "/veiculos/alterar/**", "/veiculos/remover/**").hasRole("ADMIN")
                        .requestMatchers("/funcionarios/cadastrar", "/funcionarios/alterar/**", "/funcionarios/remover/**").hasRole("ADMIN")
                        .requestMatchers("/usuarios/cadastrar").hasRole("ADMIN")
                        .requestMatchers("/relatorios/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(
                                (request, response, authentication) -> response.addHeader("HX-Redirect", "/login"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("select nome_usuario, senha, ativo from usuario where nome_usuario = ?");

        manager.setAuthoritiesByUsernameQuery(
                "select u.nome_usuario, p.nome from usuario u " +
                "join usuario_papel up on u.codigo = up.codigo_usuario " +
                "join papel p on up.codigo_papel = p.codigo " +
                "where u.nome_usuario = ?");
        
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idEnconder = "argon2";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idEnconder, encoders);
        return passwordEncoder;
    }

}