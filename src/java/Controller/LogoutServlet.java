package Controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtém a sessão atual, se houver (o 'false' evita criar uma nova)
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // 2. Invalida a sessão, removendo todos os atributos (incluindo "currentUser")
            session.invalidate();
            System.out.println("Sessão invalidada. Usuário desconectado.");
        }
        
        // 3. Redireciona o usuário para a página de login (ou home page)
        response.sendRedirect("login.jsp");
    }
    
    // Geralmente o logout é tratado via GET por ser uma ação simples de "sair"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
