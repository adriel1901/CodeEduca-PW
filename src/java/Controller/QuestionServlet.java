package Controller;

import Controle.QuestionDAO;
import Controle.QuestionGroupDAO;
import Modelo.Alternative;
import Modelo.Question;
import Modelo.QuestionGroup;
import Modelo.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {

    private QuestionDAO questionDAO;
    private QuestionGroupDAO groupDAO; // Para buscar o título do grupo

    @Override
    public void init() throws ServletException {
        super.init();
        this.questionDAO = new QuestionDAO();
        this.groupDAO = new QuestionGroupDAO();
    }

    private User getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {
            response.sendRedirect("login.jsp");
        }
        return user;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        // O group_id é fundamental para esta seção
        int groupId = 0;
        try {
            groupId = Integer.parseInt(request.getParameter("groupId"));
            request.getSession().setAttribute("currentGroupId", groupId); // Armazena na sessão
        } catch (NumberFormatException e) {
            // Se groupId não for passado, tenta pegar da sessão
            Integer sessionGroupId = (Integer) request.getSession().getAttribute("currentGroupId");
            if (sessionGroupId != null) {
                groupId = sessionGroupId;
            } else {
                response.sendRedirect("GroupServlet?action=list"); // Se não houver ID, volta para grupos
                return;
            }
        }

        try {
            switch (action) {
                case "new":
                    showNewQuestionForm(request, response, groupId);
                    break;
                case "edit": // NOVO: Carrega dados para edição
                    showEditForm(request, response, groupId);
                    break;
                case "delete": // NOVO: Processa a exclusão
                    deleteQuestion(request, response, groupId);
                    break;
                case "list":
                default:
                    listQuestions(request, response, groupId);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Carrega a questão e alternativas para o formulário de edição.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, int groupId)
            throws ServletException, IOException {

        int questionId = Integer.parseInt(request.getParameter("id"));
        Question question = questionDAO.findByIdWithAlternatives(questionId);

        if (question == null || question.getGroupId() != groupId) {
            request.setAttribute("message", "Questão não encontrada no grupo.");
            listQuestions(request, response, groupId);
            return;
        }

        QuestionGroup group = groupDAO.findById(groupId); // Para exibir o título

        request.setAttribute("question", question); // Passa a questão completa
        request.setAttribute("group", group);
        request.setAttribute("isEditing", true); // Flag para o JSP

        RequestDispatcher dispatcher = request.getRequestDispatcher("question_form.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Lista todas as questões de um grupo.
     */
    private void listQuestions(HttpServletRequest request, HttpServletResponse response, int groupId)
            throws ServletException, IOException {

        // 1. Busca o título do grupo (para exibição)
        QuestionGroup group = groupDAO.findById(groupId);
        if (group == null) {
            response.sendRedirect("GroupServlet?action=list");
            return;
        }

        // 2. Busca as questões e alternativas
        List<Question> listQuestions = questionDAO.findAllByGroupId(groupId);

        // 3. Coloca os dados no Request
        request.setAttribute("group", group);
        request.setAttribute("listQuestions", listQuestions);

        RequestDispatcher dispatcher = request.getRequestDispatcher("question_management.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Exibe o formulário de criação de nova questão.
     */
    private void showNewQuestionForm(HttpServletRequest request, HttpServletResponse response, int groupId)
            throws ServletException, IOException {

        // Carrega o objeto QuestionGroup para exibir o título no formulário
        QuestionGroup group = groupDAO.findById(groupId);
        if (group == null) {
            response.sendRedirect("GroupServlet?action=list");
            return;
        }

        request.setAttribute("group", group);
        RequestDispatcher dispatcher = request.getRequestDispatcher("question_form.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "insert":
                    insertQuestion(request, response);
                    break;
                case "update": // NOVO: Processa a atualização
                    updateQuestion(request, response);
                    break;
                default:
                    response.sendRedirect("GroupServlet?action=list");
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Processa a atualização da Questão e Alternativas.
     */
    private void updateQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int groupId = Integer.parseInt(request.getParameter("groupId"));
        int questionId = Integer.parseInt(request.getParameter("questionId")); // NOVO: ID da Questão
        String questionText = request.getParameter("question_text");
        String difficulty = request.getParameter("difficulty");
        String correctAlternativeIndex = request.getParameter("correct_alternative");

        // 1. Criar o objeto Question atualizado
        Question updatedQuestion = new Question();
        updatedQuestion.setQuestionId(questionId);
        updatedQuestion.setGroupId(groupId);
        updatedQuestion.setQuestionText(questionText);
        updatedQuestion.setDifficulty(difficulty);

        // 2. Recriar a lista de Alternativas (Lógica igual ao insert)
        List<Alternative> alternatives = new ArrayList<>();
        char altId = 'A';
        for (int i = 1; i <= 5; i++) {
            String altText = request.getParameter("alternative_" + i + "_text");
            if (altText == null || altText.trim().isEmpty()) {
                altId++;
                continue;
            }

            Alternative alt = new Alternative();
            alt.setAlternativeText(altText);
            alt.setCorrect(String.valueOf(i).equals(correctAlternativeIndex));
            alt.setIdentifier(String.valueOf(altId));

            alternatives.add(alt);
            altId++;
        }

        try {
            questionDAO.update(updatedQuestion, alternatives); // Chama o método de Update transacional

            request.setAttribute("message", "Questão e alternativas atualizadas com sucesso!");
            request.setAttribute("messageType", "success");
            listQuestions(request, response, groupId);

        } catch (RuntimeException e) {
            request.setAttribute("message", "Erro ao atualizar questão: " + e.getMessage());
            // Passa a questão para o formulário para evitar perda de dados
            request.setAttribute("question", updatedQuestion);
            request.setAttribute("isEditing", true);
            showEditForm(request, response, groupId); // Retorna ao formulário de edição
        }
    }

    /**
     * Processa a exclusão da questão.
     */
    private void deleteQuestion(HttpServletRequest request, HttpServletResponse response, int groupId)
            throws ServletException, IOException {

        int questionId = Integer.parseInt(request.getParameter("id"));

        try {
            questionDAO.delete(questionId);
            request.setAttribute("message", "Questão e suas alternativas foram excluídas com sucesso!");
            request.setAttribute("messageType", "success");
        } catch (RuntimeException e) {
            request.setAttribute("message", "Erro ao deletar questão: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }
        listQuestions(request, response, groupId);
    }

    /**
     * Processa o formulário, cria a Questão e suas Alternativas, e chama o DAO.
     */
    private void insertQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int groupId = Integer.parseInt(request.getParameter("groupId"));
        String questionText = request.getParameter("question_text");
        String difficulty = request.getParameter("difficulty");
        String correctAlternativeIndex = request.getParameter("correct_alternative");

        // 1. Criar o objeto Question
        Question newQuestion = new Question();
        newQuestion.setGroupId(groupId);
        newQuestion.setQuestionText(questionText);
        newQuestion.setDifficulty(difficulty);

        // 2. Criar a lista de Alternativas
        List<Alternative> alternatives = new ArrayList<>();
        char altId = 'A';
        for (int i = 1; i <= 5; i++) {
            String altText = request.getParameter("alternative_" + i + "_text");
            // Se o texto estiver vazio, ignora esta alternativa (útil se forem menos de 5)
            if (altText == null || altText.trim().isEmpty()) {
                altId++;
                continue;
            }

            Alternative alt = new Alternative();
            alt.setAlternativeText(altText);
            // Compara o índice da alternativa com o índice do radio button marcado
            alt.setCorrect(String.valueOf(i).equals(correctAlternativeIndex));
            alt.setIdentifier(String.valueOf(altId));

            alternatives.add(alt);
            altId++;
        }

        // 3. Validação Adicional
        if (alternatives.size() < 2) { // Garante um mínimo de 2 alternativas
            request.setAttribute("message", "É necessário ter pelo menos 2 alternativas válidas.");
            showNewQuestionForm(request, response, groupId);
            return;
        }

        // 4. Chamar o DAO (o método save que criamos na etapa anterior)
        try {
            questionDAO.save(newQuestion, alternatives);

            request.setAttribute("message", "Questão salva com sucesso!");
            request.setAttribute("messageType", "success");

            // Retorna para a listagem para ver o resultado
            listQuestions(request, response, groupId);

        } catch (RuntimeException e) {
            request.setAttribute("message", "Erro ao salvar questão: " + e.getMessage());
            // Se falhar, retorna ao formulário
            showNewQuestionForm(request, response, groupId);
        }
    }
    // ... (doPost e outros métodos virão na próxima etapa)
}
