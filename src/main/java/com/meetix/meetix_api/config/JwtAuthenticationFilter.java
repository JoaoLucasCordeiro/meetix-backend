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
 * Filtro JWT para interceptar e validar tokens em todas as requisições.
 * 
 * Este filtro:
 * - Extrai o token JWT do header Authorization
 * - Valida o token usando o JwtService
 * - Autentica o usuário no contexto do Spring Security
 * - Permite que a requisição continue se o token for válido
 * 
 * O filtro é executado uma vez por requisição antes dos controllers.
 * 
 * @author Meetix Team
 * @version 1.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método principal do filtro que processa cada requisição HTTP.
     * 
     * @param request Requisição HTTP
     * @param response Resposta HTTP
     * @param filterChain Cadeia de filtros
     * @throws ServletException Em caso de erro do servlet
     * @throws IOException Em caso de erro de I/O
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String requestPath = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");

        logger.debug("Processando requisição: {} {}", request.getMethod(), requestPath);

        // Se não há header Authorization ou não começa com "Bearer ", pula a validação
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("Nenhum token JWT encontrado na requisição para: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extrai o token JWT (remove "Bearer " do início)
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractEmail(jwt);

            logger.debug("Token JWT encontrado para email: {}", userEmail);

            // Se o email foi extraído e não há autenticação no contexto
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Carrega os detalhes do usuário
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Valida o token
                if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                    
                    // Cria o token de autenticação do Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    // Adiciona detalhes da requisição
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Define a autenticação no contexto do Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    logger.debug("Usuário autenticado com sucesso: {}", userEmail);
                } else {
                    logger.warn("Token JWT inválido para usuário: {}", userEmail);
                }
            }

        } catch (Exception e) {
            logger.error("Erro ao processar token JWT: {}", e.getMessage());
            // Limpa o contexto de segurança em caso de erro
            SecurityContextHolder.clearContext();
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Determina se este filtro deve ser aplicado para a requisição.
     * 
     * Por padrão, aplica o filtro para todas as requisições exceto
     * endpoints públicos como /auth/**, /events (GET).
     * 
     * @param request Requisição HTTP
     * @return true se o filtro deve ser aplicado, false caso contrário
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // Não aplica o filtro para endpoints de autenticação e health check
        boolean shouldSkip = path.startsWith("/auth/") || 
                           path.equals("/") || 
                           path.startsWith("/actuator/") ||
                           path.startsWith("/h2-console/") ||
                           path.startsWith("/swagger-") ||
                           path.startsWith("/v3/api-docs") ||
                           // Endpoints de eventos são públicos para leitura (GET)
                           (path.startsWith("/events") && "GET".equals(method));
        
        if (shouldSkip) {
            logger.debug("Filtro JWT pulado para endpoint público: {} {}", method, path);
        }
        
        return shouldSkip;
    }
}