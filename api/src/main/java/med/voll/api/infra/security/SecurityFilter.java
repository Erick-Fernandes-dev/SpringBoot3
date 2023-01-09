package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        //Necessário para chamar os próximos filtros da aplicação
        filterChain.doFilter(request, response);


    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new RuntimeException("Token JWT não enviado no cabeçalho Authorization!");
        }

        //vai substituir o nome do prefixo em uma string vazia
        return authorizationHeader.replace("Bearer ", "");
    }

    // OBS: Essa classe vai ser executada apenas uma vez, para cada requisicao

    // request - pega coisas da requisicap
    // reponse - Enviar coisas na resposta
    // filterChain - Representa a cadeia de filtros na aplicação
}
