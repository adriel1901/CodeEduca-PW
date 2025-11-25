package Controller;

import Controle.UserDAO;
import Modelo.User;
import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Mapeia o Servlet para a URL "/UserServlet"
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // O filtro de autenticação já garante que o currentUser não é nulo
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "profile": // NOVO: Mostra a página de edição de perfil
                    showProfile(request, response, currentUser.getUserId());
                    break;
                // ... (outros casos do CRUD se houver)
                default:
                    response.sendRedirect("home.jsp");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Carrega os dados do usuário atual e encaminha para o JSP.
     */
    private void showProfile(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        User user = userDAO.findById(userId);

        // Coloca o objeto User no request (para o caso de ser a primeira vez)
        request.setAttribute("userProfile", user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
        dispatcher.forward(request, response);
    }

    // Inicializa o DAO quando o Servlet é iniciado
    @Override
    public void init() throws ServletException {
        super.init();
        this.userDAO = new UserDAO();
    }

    // Recebe requisições POST (como o envio do formulário)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Define o encoding para garantir que caracteres especiais sejam lidos corretamente
        request.setCharacterEncoding("UTF-8");

        // Determina a ação que o formulário solicitou
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        try {
            switch (action) {
                case "register":
                    registerUser(request, response);
                    break;
                case "login":
                    loginUser(request, response);
                    break;
                case "update_profile": // NOVO
                    updateProfile(request, response, currentUser);
                    break;
                case "change_password": // NOVO
                    changePassword(request, response, currentUser);
                    break;
                default:
                    response.sendRedirect("home.jsp");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Processa a atualização de nome e e-mail.
     */
    private void updateProfile(HttpServletRequest request, HttpServletResponse response, User sessionUser)
            throws ServletException, IOException {

        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");

        // Cria um objeto User com os novos dados
        User userToUpdate = new User();
        userToUpdate.setUserId(sessionUser.getUserId());
        userToUpdate.setFullName(fullName);
        userToUpdate.setEmail(email);

        try {
            userDAO.updateProfile(userToUpdate);

            // Atualiza o objeto User na sessão para refletir as mudanças imediatamente
            sessionUser.setFullName(fullName);
            sessionUser.setEmail(email);
            request.getSession().setAttribute("currentUser", sessionUser);

            request.setAttribute("message", "Perfil atualizado com sucesso!");
            request.setAttribute("messageType", "success");

        } catch (RuntimeException e) {
            // Trata a falha (Ex: e-mail já em uso - UNIQUE constraint)
            request.setAttribute("message", "Erro ao atualizar: O e-mail pode já estar em uso.");
            request.setAttribute("messageType", "error");
            request.setAttribute("userProfile", userToUpdate); // Mantém os dados no formulário
        }

        // Retorna para a página de perfil
        showProfile(request, response, sessionUser.getUserId());
    }

    /**
     * Processa a alteração de senha.
     */
    private void changePassword(HttpServletRequest request, HttpServletResponse response, User sessionUser)
            throws ServletException, IOException {

        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        // 1. Validação de Nova Senha
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("message", "A nova senha e a confirmação não conferem.");
            request.setAttribute("messageType", "error");
            showProfile(request, response, sessionUser.getUserId());
            return;
        }

        // 2. Validação de Senha Atual (Simples - Autenticação)
        // Usamos o método authenticate para verificar se a senha atual é válida
        User authCheck = userDAO.authenticate(sessionUser.getUsername(), currentPassword);

        if (authCheck == null) {
            request.setAttribute("message", "A senha atual está incorreta.");
            request.setAttribute("messageType", "error");
        } else {
            // 3. Atualização da Senha
            try {
                // OBS: Você precisará adicionar o método updatePassword ao UserDAO
                userDAO.updatePassword(sessionUser.getUserId(), newPassword);
                request.setAttribute("message", "Senha alterada com sucesso!");
                request.setAttribute("messageType", "success");
            } catch (RuntimeException e) {
                request.setAttribute("message", "Erro ao alterar a senha: " + e.getMessage());
                request.setAttribute("messageType", "error");
            }
        }

        // Retorna para a página de perfil
        showProfile(request, response, sessionUser.getUserId());
    }

    /**
     * Processa o cadastro de um novo usuário.
     */
    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Coleta os parâmetros do formulário
        String fullName = request.getParameter("full_name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        // 2. Validação básica de campos
        if (!password.equals(confirmPassword)) {
            // Define a mensagem de erro e redireciona de volta para o JSP
            request.setAttribute("message", "As senhas não conferem!");
            request.setAttribute("messageType", "error");

            RequestDispatcher dispatcher = request.getRequestDispatcher("cadastro.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // 3. Cria o objeto Model
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); // A senha será usada como hash pelo DAO

        // 4. Chama a camada DAO para salvar
        try {
            userDAO.save(newUser);

            // Sucesso: define a mensagem e redireciona para o login ou home
            request.setAttribute("message", "Cadastro realizado com sucesso! Faça login.");
            request.setAttribute("messageType", "success");

            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);

        } catch (RuntimeException e) {
            // Trata erros do banco (ex: username ou email já existem, quebra da UNIQUE constraint)
            request.setAttribute("message", "Erro: O Login ou E-mail já estão em uso.");
            request.setAttribute("messageType", "error");

            RequestDispatcher dispatcher = request.getRequestDispatcher("cadastro.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Processa a autenticação de um usuário.
     */
    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 1. Chama o DAO para autenticar
        User authenticatedUser = userDAO.authenticate(username, password);

        if (authenticatedUser != null) {
            // 2. Autenticação bem-sucedida

            // 3. Cria ou obtém a sessão HTTP
            HttpSession session = request.getSession();

            // 4. Armazena o objeto User na sessão para acesso futuro
            // Isso mantém o usuário "logado" enquanto a sessão estiver ativa
            session.setAttribute("currentUser", authenticatedUser);

            // 5. Redireciona para a página principal (ou painel de controle)
            response.sendRedirect("home.jsp"); // Precisaremos criar esta página

        } else {
            // 6. Falha na autenticação
            request.setAttribute("message", "Login ou Senha inválidos.");
            request.setAttribute("messageType", "error");

            // Redireciona de volta para a página de login
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }
}
