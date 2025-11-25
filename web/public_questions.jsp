<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.QuestionGroup"%>
<%@page import="Modelo.Question"%>
<%@page import="Modelo.Alternative"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Estudo: <%= ((QuestionGroup)request.getAttribute("group")).getTitle() %></title>
    <style>
        /* Reutiliza estilos de questions/index */
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
        h1 { color: #007bff; text-align: center; }
        .question-box { background: #f9f9f9; border: 1px solid #ddd; padding: 15px; margin-bottom: 20px; border-radius: 6px; }
        .question-text { font-size: 1.1em; font-weight: bold; margin-bottom: 10px; }
        .alternatives ul { list-style: none; padding-left: 0; }
        .alternatives li { padding: 5px 0; border-bottom: 1px dotted #eee; cursor: pointer; }
        .correct { color: green; font-weight: bold; }
        .wrong { color: red; }
        .answer-reveal { margin-top: 10px; padding: 10px; border-radius: 4px; display: none; }
        .difficulty { color: orange; font-style: italic; }
    </style>
</head>
<body>
    <div class="container">
        <% 
            QuestionGroup group = (QuestionGroup) request.getAttribute("group");
            List<Question> listQuestions = (List<Question>) request.getAttribute("listQuestions");
        %>
        
        <h1>📚 Modo Estudo: <%= group.getTitle() %></h1>
        <p>Clique nas alternativas para revelar a resposta correta.</p>
        <hr>
        
        <a href="index.jsp">⬅️ Voltar para a Home</a>

        <% if (listQuestions == null || listQuestions.isEmpty()) { %>
            <p style="text-align: center; margin-top: 50px;">Nenhuma questão encontrada para estudo neste grupo.</p>
        <% } else { %>
            
            <% int qNum = 1; %>
            <% for (Question q : listQuestions) { %>
                <div class="question-box">
                    <div class="question-text">
                        <%= qNum++ %>. <%= q.getQuestionText() %> 
                        (<span class="difficulty">Dificuldade: <%= q.getDifficulty() %></span>)
                    </div>
                    
                    <div class="alternatives" id="alternatives_<%= q.getQuestionId() %>">
                        <ul>
                            <% 
                                // O método findAllByGroupId do DAO precisa carregar as alternativas
                                if (q.getAlternatives() != null) {
                                    char id = 'A';
                                    for (Alternative alt : q.getAlternatives()) {
                                        // Usamos um ID para o JavaScript
                            %>
                                    <li data-is-correct="<%= alt.isCorrect() %>" 
                                        data-alt-id="<%= alt.getAlternativeId() %>" 
                                        onclick="checkAnswer(this, <%= q.getQuestionId() %>)">
                                        <%= id++ %>) <%= alt.getAlternativeText() %>
                                    </li>
                            <%
                                    }
                                }
                            %>
                        </ul>
                    </div>
                </div>
            <% } %>
        <% } %>
    </div>
    
    <script>
        function checkAnswer(selectedLi, questionId) {
            const isCorrect = selectedLi.getAttribute('data-is-correct') === 'true';
            const alternativesContainer = document.getElementById('alternatives_' + questionId);
            
            // 1. Desabilita cliques futuros (previne múltiplas respostas)
            alternativesContainer.querySelectorAll('li').forEach(li => {
                li.onclick = null;
            });
            
            // 2. Marca a selecionada
            if (isCorrect) {
                selectedLi.classList.add('correct');
            } else {
                selectedLi.classList.add('wrong');
                // Revela a correta
                alternativesContainer.querySelectorAll('li').forEach(li => {
                    if (li.getAttribute('data-is-correct') === 'true') {
                        li.classList.add('correct');
                    }
                });
            }
        }
    </script>
</body>
</html>
