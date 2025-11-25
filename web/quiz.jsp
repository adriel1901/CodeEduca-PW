<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.QuestionGroup"%>
<%@page import="Modelo.Question"%>
<%@page import="Modelo.Alternative"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Quiz: Responder Questões</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
        .quiz-container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 15px rgba(0, 0, 0, 0.1); }
        h1 { color: #007bff; border-bottom: 2px solid #007bff; padding-bottom: 10px; }
        .question-block { margin-bottom: 30px; padding: 20px; border: 1px solid #ddd; border-radius: 6px; background-color: #f9f9f9; }
        .question-text { font-weight: bold; font-size: 1.1em; margin-bottom: 15px; color: #333; }
        .alternative label { display: block; padding: 10px; margin: 5px 0; border: 1px solid #eee; border-radius: 4px; cursor: pointer; transition: background-color 0.2s; }
        .alternative label:hover { background-color: #e9e9e9; }
        .submit-btn { background-color: #28a745; color: white; padding: 15px 25px; border: none; border-radius: 5px; cursor: pointer; font-size: 1.2em; width: 100%; margin-top: 20px; }
        .submit-btn:hover { background-color: #1e7e34; }
        .back-link { display: block; text-align: center; margin-top: 20px; }
    </style>
</head>
<body>
    <div class="quiz-container">
        <% 
            QuestionGroup group = (QuestionGroup) request.getAttribute("group");
            List<Question> questions = (List<Question>) request.getAttribute("questions");
        %>

        <h1>Quiz: <%= group.getTitle() %></h1>
        <p><%= group.getDescription() %></p>
        <hr>

        <% if (questions == null || questions.isEmpty()) { %>
            <p style="text-align: center; color: #dc3545;">Este grupo não possui questões para iniciar o quiz.</p>
            <div class="back-link"><a href="PublicServlet?action=home">Voltar para a Home</a></div>
        <% } else { %>
        
            <form action="PublicServlet" method="POST">
                <input type="hidden" name="action" value="submitQuiz">
                <input type="hidden" name="groupId" value="<%= group.getGroupId() %>">
                
                <% int qIndex = 1; %>
                <% for (Question question : questions) { %>
                    <div class="question-block">
                        <p class="question-text"><%= qIndex++ %>. <%= question.getQuestionText() %></p>
                        
                        <div class="alternatives-list">
                            <% 
                                List<Alternative> alternatives = question.getAlternatives();
                                if (alternatives != null) {
                                    for (Alternative alternative : alternatives) {
                            %>
                                        <div class="alternative">
                                            <label>
                                                <%-- O nome do radio button deve ser dinâmico: 'q_' + ID da Questão --%>
                                                <input type="radio" 
                                                       name="q_<%= question.getQuestionId() %>" 
                                                       value="<%= alternative.getAlternativeId() %>" 
                                                       required>
                                                <%= alternative.getAlternativeText() %>
                                            </label>
                                        </div>
                            <%      }
                                } %>
                        </div>
                    </div>
                <% } %>
                
                <input type="submit" value="Finalizar Quiz e Ver Resultado" class="submit-btn">
            </form>
            
            <div class="back-link"><a href="PublicServlet?action=home">Voltar sem Finalizar</a></div>
        <% } %>
    </div>
</body>
</html>