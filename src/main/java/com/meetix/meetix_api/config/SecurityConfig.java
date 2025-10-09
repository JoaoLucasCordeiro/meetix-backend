package com.meetix.meetix_api.config;

import com.meetix.meetix_api.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Classe central que define a postura de segurança da aplicação.
 * Aqui configuramos quais endpoints são públicos, quais são protegidos,
 * como os tokens JWT são validados e as políticas de CORS.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Ativa a segurança a nível de método (ex: @PreAuthorize)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Injeção de dependências via construtor, a prática recomendada pelo Spring.
    @Autowired
    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * O Bean principal que configura toda a cadeia de filtros de segurança HTTP.
     * É aqui que a "mágica" do Spring Security acontece.
     *
     * @param http O objeto de configuração do HttpSecurity.
     * @return A cadeia de filtros de segurança construída.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desativa a proteção CSRF, pois não é necessária para APIs REST stateless.
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Aplica as configurações de CORS definidas no bean corsConfigurationSource.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Define as regras de autorização para cada endpoint. A ORDEM É CRÍTICA!
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos que não exigem autenticação.
                        .requestMatchers("/auth/**", "/health", "/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Documentação da API

                        // Permite que qualquer um visualize eventos (GET), mas protege as outras operações (POST, PUT, DELETE).
                        .requestMatchers(HttpMethod.GET, "/events", "/events/**").permitAll()

                        // Qualquer outra requisição para /events/** (POST, PUT, DELETE) precisa de autenticação.
                        .requestMatchers("/events/**").authenticated()

                        // 4. Garante que todas as outras requisições não listadas acima exijam autenticação.
                        .anyRequest().authenticated()
                )

                // 5. Configura o gerenciamento de sessão para ser "stateless".
                // Isso significa que o servidor não guarda estado de sessão; cada requisição é autenticada por si só (via JWT).
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 6. Define nosso provedor de autenticação customizado.
                .authenticationProvider(authenticationProvider())

                // 7. Adiciona nosso filtro de JWT para rodar antes do filtro padrão de username/password do Spring.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Define o provedor de autenticação que o Spring Security usará.
     * Ele conecta nosso CustomUserDetailsService (que busca usuários no banco)
     * e o PasswordEncoder (que sabe como verificar as senhas).
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expõe o AuthenticationManager do Spring como um Bean.
     * Necessário para o processo de autenticação manual no nosso AuthenticationController.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define o algoritmo de criptografia de senhas.
     * Usamos o BCrypt, que é o padrão ouro atual para hashing de senhas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura a política de CORS (Cross-Origin Resource Sharing) para a aplicação.
     * Essencial para permitir que um frontend hospedado em um domínio diferente acesse esta API.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Em desenvolvimento, permitir qualquer origem é aceitável.
        // Em produção, restrinja isso a domínios específicos! Ex: "https://meufrontend.com"
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        // Define os métodos HTTP que são permitidos (GET, POST, etc.).
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Define quais headers a requisição pode conter.
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permite que o navegador envie credenciais (como cookies ou tokens de autenticação).
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração de CORS para todas as rotas da nossa API.
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}