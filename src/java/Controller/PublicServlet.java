package Controller;

import Controle.QuestionGroupDAO;
import Controle.QuestionDAO;
import Modelo.Alternative;
import Modelo.QuestionGroup;
import Modelo.Question;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/PublicServlet"})
public class PublicServlet extends HttpServlet {

    private QuestionGroupDAO groupDAO;
    private QuestionDAO questionDAO;

    // --- 1. INICIALIZAÇÃO E TRATAMENTO DE ERROS NA CONEXÃO ---
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Instanciação segura dos DAOs. Se a ConnectionFactory falhar, o erro será capturado aqui.
            this.groupDAO = new QuestionGroupDAO();
            this.questionDAO = new QuestionDAO();
        } catch (Exception e) {
            // Captura qualquer erro na inicialização (ex: driver não encontrado)
            throw new ServletException("Erro ao inicializar DAOs no PublicServlet.", e);
        }
    }

    // -----------------------------------------------------------------
    // --- 2. GERENCIAMENTO DE REQUISIÇÕES (DO GET E DO POST) ---
    // -----------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "home"; 
        }

        try {
            switch (action) {
                case "viewGroup":
                    viewGroupQuestions(request, response);
                    break;
                case "startQuiz":
                    startQuiz(request, response);
                    break;
                case "home":
                default:
                    showHomePage(request, response);
                    break;
            }
        } catch (Exception ex) {
            // Captura exceções não tratadas nas sub-rotinas e loga
            System.err.println("ERRO FATAL NO DOGET do PublicServlet: " + ex.getMessage());
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocorreu um erro interno ao processar a requisição.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("submitQuiz".equals(action)) {
                submitQuiz(request, response);
            } else {
                // Se o POST não for reconhecido, volta para o GET da Home
                doGet(request, response);
            }
        } catch (Exception ex) {
            System.err.println("ERRO FATAL NO DOPOST (Quiz): " + ex.getMessage());
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar o envio do Quiz.");
        }
    }

    // -----------------------------------------------------------------
    // --- 3. MÉTODOS DE AÇÃO ---
    // -----------------------------------------------------------------

    /**
     * Exibe a página inicial, carregando todos os grupos públicos.
     */
    private void showHomePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<QuestionGroup> publicGroups = Collections.emptyList(); // Usa lista vazia por padrão
        try {
            publicGroups = groupDAO.findAll();
        } catch (RuntimeException e) {
            System.err.println("Erro ao carregar grupos para Home: " + e.getMessage());
            // A exceção é logada, mas a página continua a carregar com lista vazia.
        }

        request.setAttribute("publicGroups", publicGroups);
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Prepara e exibe o formulário do Quiz.
     */
    private void startQuiz(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obter e Validar o groupId
        int groupId = getValidatedGroupId(request, response);
        if (groupId == 0) return; // Redirecionamento já ocorreu

        // 2. Busca do Grupo e Questões
        QuestionGroup group = groupDAO.findById(groupId);
        List<Question> questions = Collections.emptyList();
        
        if (group != null) {
            questions = questionDAO.findAllByGroupId(groupId);
        } else {
            // Se o grupo não for encontrado, redireciona para a home
            response.sendRedirect("PublicServlet?action=home");
            return;
        }

        // 3. Encaminha para o Quiz
        request.setAttribute("group", group);
        request.setAttribute("questions", questions);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("quiz.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Exibe as questões de um grupo, sem ser no formato de quiz (Modo Estudo Simples).
     */
    private void viewGroupQuestions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int groupId = getValidatedGroupId(request, response);
        if (groupId == 0) return;

        QuestionGroup group = groupDAO.findById(groupId);
        if (group == null) {
            response.sendRedirect("PublicServlet?action=home");
            return;
        }
        
        List<Question> questions = questionDAO.findAllByGroupId(groupId);

        request.setAttribute("group", group);
        request.setAttribute("listQuestions", questions);

        RequestDispatcher dispatcher = request.getRequestDispatcher("public_questions.jsp");
        dispatcher.forward(request, response);
    }


    /**
     * Processa o formulário de respostas do Quiz e calcula o score.
     */
    private void submitQuiz(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int groupId = getValidatedGroupId(request, response);
        if (groupId == 0) return;

        // Rebusca as questões para garantir que o resultado seja calculado com dados frescos
        List<Question> questions = questionDAO.findAllByGroupId(groupId);
        
        int totalQuestions = questions.size();
        int score = 0;

        for (Question question : questions) {
            String selectedAlternativeIdStr = request.getParameter("q_" + question.getQuestionId());

            if (selectedAlternativeIdStr != null && !selectedAlternativeIdStr.isEmpty()) {
                try {
                    int selectedAlternativeId = Integer.parseInt(selectedAlternativeIdStr);

                    // Verifica a resposta correta
                    for (Alternative alt : question.getAlternatives()) {
                        if (alt.getAlternativeId() == selectedAlternativeId && alt.isCorrect()) {
                            score++;
                            break; 
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("ID de alternativa inválido submetido: " + selectedAlternativeIdStr);
                    // Continua para a próxima questão, ignorando esta resposta inválida
                }
            }
        }

        // Encaminha para o resultado
        QuestionGroup group = groupDAO.findById(groupId);
        request.setAttribute("group", group);
        request.setAttribute("score", score);
        request.setAttribute("totalQuestions", totalQuestions);

        RequestDispatcher dispatcher = request.getRequestDispatcher("quiz_result.jsp");
        dispatcher.forward(request, response);
    }
    
    // -----------------------------------------------------------------
    // --- 4. MÉTODOS DE UTILIDADE ---
    // -----------------------------------------------------------------
    
    /**
     * Tenta obter o groupId da requisição e trata NumberFormatException, redirecionando em caso de falha.
     * @return O groupId válido ou 0 se a validação falhar e o redirecionamento for acionado.
     */
    private int getValidatedGroupId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String groupIdStr = request.getParameter("groupId");
        if (groupIdStr == null || groupIdStr.isEmpty()) {
            response.sendRedirect("PublicServlet?action=home");
            return 0;
        }
        try {
            return Integer.parseInt(groupIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("PublicServlet?action=home");
            return 0;
        }
    }
}
