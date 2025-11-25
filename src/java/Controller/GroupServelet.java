package Controller;

import Controle.QuestionGroupDAO;
import Modelo.QuestionGroup;
import Modelo.User;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/GroupServlet")
public class GroupServelet extends HttpServlet {

    private QuestionGroupDAO groupDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.groupDAO = new QuestionGroupDAO();
    }
    
    // Método utilitário para verificar a autenticação
    private User getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        
        if (user == null) {
            // Se não estiver logado, redireciona para a página de login
            response.sendRedirect("login.jsp");
        }
        return user;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
       User currentUser = getAuthenticatedUser(request, response);

        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Ação padrão é listar
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit": // NOVO: Mostra o formulário preenchido para edição
                    showEditForm(request, response);
                    break;
                case "delete": // NOVO: Processa a exclusão
                    deleteGroup(request, response, currentUser.getUserId());
                    break;
                case "list":
                default:
                    listGroups(request, response, currentUser.getUserId());
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Pega o ID do grupo na URL (parâmetro 'id')
        int id = Integer.parseInt(request.getParameter("id"));
        QuestionGroup existingGroup = groupDAO.findById(id);
        
        // Coloca o objeto no Request para o JSP
        request.setAttribute("group", existingGroup); 
        
        // Reutiliza o group_form.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("group_form.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        User currentUser = getAuthenticatedUser(request, response);
        
        
        request.setCharacterEncoding("UTF-8"); 

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "insert":
                    insertGroup(request, response, currentUser.getUserId());
                    break;
                case "update": // NOVO: Processa a atualização dos dados
                    updateGroup(request, response, currentUser.getUserId());
                    break;
                // O delete foi movido para o doGet para simplificar a URL de link
                default:
                    listGroups(request, response, currentUser.getUserId());
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
    
    /**
     * Processa a atualização de um grupo existente.
     */
    private void updateGroup(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        
        // Pega o ID (escondido no formulário) para saber qual grupo atualizar
        int id = Integer.parseInt(request.getParameter("group_id")); 
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        QuestionGroup group = new QuestionGroup();
        group.setGroupId(id);
        group.setUserId(userId); 
        group.setTitle(title);
        group.setDescription(description);

        try {
            groupDAO.update(group);
            request.setAttribute("message", "Grupo atualizado com sucesso!");
            request.setAttribute("messageType", "success");
        } catch (RuntimeException e) {
            request.setAttribute("message", "Erro ao atualizar grupo: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }
        listGroups(request, response, userId); // Volta para a listagem
    }

    /**
     * Processa a exclusão de um grupo (método movido para o doGet, mas mantido aqui para referência).
     */
    private void deleteGroup(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        
        // Pega o ID do grupo a ser deletado
        int id = Integer.parseInt(request.getParameter("id")); 

        try {
            groupDAO.delete(id);
            request.setAttribute("message", "Grupo deletado com sucesso! (ID: " + id + ")");
            request.setAttribute("messageType", "success");
        } catch (RuntimeException e) {
            request.setAttribute("message", "Erro ao deletar grupo: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }
        listGroups(request, response, userId); // Volta para a listagem
    }

    // Exibe o formulário de criação de novo grupo
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("group_form.jsp");
        dispatcher.forward(request, response);
    }
    
    // Processa a inserção de um novo grupo
    private void insertGroup(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        QuestionGroup newGroup = new QuestionGroup();
        newGroup.setUserId(userId); // Define o criador do grupo
        newGroup.setTitle(title);
        newGroup.setDescription(description);

        try {
            groupDAO.save(newGroup);
            
            // Sucesso: adiciona mensagem e redireciona para a listagem
            request.setAttribute("message", "Grupo criado com sucesso!");
            request.setAttribute("messageType", "success");
            listGroups(request, response, userId);

        } catch (RuntimeException e) {
            // Falha: redireciona de volta para o formulário com erro
            request.setAttribute("message", "Erro ao criar grupo: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.setAttribute("group", newGroup); // Mantém os dados no formulário
            showNewForm(request, response); // Reutiliza a função para exibir o formulário
        }
    }

    // Lista os grupos do usuário
    private void listGroups(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        
        List<QuestionGroup> listGroup = groupDAO.findAllByUserId(userId);
        
        // Coloca a lista no Request para ser acessada pelo JSP
        request.setAttribute("listGroup", listGroup); 
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("group_management.jsp");
        dispatcher.forward(request, response);
    }
    
    
}
