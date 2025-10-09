package com.meetix.meetix_api.config;

import com.meetix.meetix_api.service.CustomUserDetailsService;
import com.meetix.meetix_api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de segurança que atua como o "porteiro" da nossa API.
 * Ele intercepta todas as requisições para verificar se elas contêm um token JWT válido,
 * garantindo que apenas usuários autenticados acessem os endpoints protegidos.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Construtor para injeção de dependências.
     * Esta é a forma recomendada pelo Spring para injetar os serviços necessários.
     */
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * O coração do filtro. Este método é executado para cada requisição que passa por ele.
     * A lógica principal de validação do token acontece aqui.
     *
     * @param request A requisição HTTP que chegou.
     * @param response A resposta HTTP que será enviada.
     * @param filterChain Objeto que nos permite passar a requisição para o próximo filtro na cadeia.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Se a requisição não tem o header 'Authorization' ou o token não é do tipo 'Bearer',
        // não há o que validar. Apenas deixamos a requisição seguir seu fluxo.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // O token JWT vem depois de "Bearer ". (7 caracteres)
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractEmail(jwt);

            // Se conseguimos extrair o email e o usuário ainda não foi autenticado nesta requisição...
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // ...buscamos os detalhes do usuário no banco de dados.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Verificamos se o token é válido (não expirou e pertence a este usuário).
                if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {

                    // Se o token for válido, criamos um objeto de autenticação para o Spring Security.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Não precisamos de credenciais (senha) aqui
                            userDetails.getAuthorities()
                    );

                    // Anexamos detalhes da requisição web ao nosso token de autenticação.
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Finalmente, atualizamos o SecurityContextHolder. A partir daqui, o Spring sabe que o usuário está autenticado.
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    logger.info("Usuário '{}' autenticado com sucesso.", userEmail);
                }
            }
        } catch (Exception e) {
            // Em caso de qualquer erro (token expirado, malformado, etc.),
            // logamos o erro e garantimos que o contexto de segurança seja limpo.
            logger.error("Não foi possível processar a autenticação do token JWT: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        // Passamos a requisição (e a resposta) para o próximo filtro na cadeia.
        filterChain.doFilter(request, response);
    }

    /**
     * Define para quais requisições este filtro NÃO deve ser aplicado.
     * Usamos este método para criar uma "lista branca" de endpoints públicos.
     *
     * @param request A requisição HTTP que chegou.
     * @return true se o filtro deve ser pulado, false se deve ser aplicado.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Lista de rotas públicas que não precisam de autenticação.
        // Adicione aqui qualquer outra rota que deva ser acessível a todos.
        boolean isPublicRoute =
                path.startsWith("/auth/") ||             // Endpoints de login e registro
                        path.startsWith("/swagger-ui/") ||       // Documentação da API
                        path.startsWith("/v3/api-docs") ||       // Definição do OpenAPI
                        (path.startsWith("/events") && "GET".equalsIgnoreCase(method)); // Listagem e busca de eventos

        return isPublicRoute;
    }
}