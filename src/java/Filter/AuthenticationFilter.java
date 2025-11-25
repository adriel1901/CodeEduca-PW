package Filter;

import Modelo.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Aplica este filtro a todos os Servlets de gerenciamento
@WebFilter({"/GroupServlet", "/QuestionServlet", "/home.jsp"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro (opcional)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        // 1. Verifica a sessão
        HttpSession session = req.getSession(false); // Não cria nova sessão

        // 2. Verifica se o usuário está logado
        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);
        
        // Se o usuário estiver logado, continua a requisição
        if (isLoggedIn) {
            // Permite que a requisição siga para o Servlet/JSP de destino
            chain.doFilter(request, response); 
        } else {
            // Se NÃO estiver logado:
            // Define uma mensagem de erro temporária
            req.setAttribute("message", "Acesso negado! Por favor, faça login.");
            req.setAttribute("messageType", "error");
            
            // Redireciona para a página de login
            req.getRequestDispatcher("login.jsp").forward(req, res);
        }
    }

    @Override
    public void destroy() {
        // Limpeza de recursos (opcional)
    }
}
