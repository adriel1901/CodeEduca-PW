<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.QuestionGroup"%>
<%@page import="Modelo.Question"%>
<%@page import="Modelo.Alternative"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Questões do Grupo</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
        .container { max-width: 1000px; margin: 0 auto; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
        h1 { color: #007bff; text-align: center; }
        h2 { border-bottom: 2px solid #ccc; padding-bottom: 10px; margin-top: 20px; }
        .question-box { background: #f9f9f9; border: 1px solid #ddd; padding: 15px; margin-bottom: 20px; border-radius: 6px; }
        .question-text { font-size: 1.1em; font-weight: bold; margin-bottom: 10px; }
        .alternatives ul { list-style: none; padding-left: 0; }
        .alternatives li { padding: 5px 0; border-bottom: 1px dotted #eee; }
        .correct { color: green; font-weight: bold; }
        .actions a { margin-right: 15px; color: #007bff; text-decoration: none; }
        .actions a:hover { text-decoration: underline; }
        .difficulty { color: orange; font-style: italic; }
    </style>
</head>
<body>
    <div class="container">
        <% 
            QuestionGroup group = (QuestionGroup) request.getAttribute("group");
            List<Question> listQuestions = (List<Question>) request.getAttribute("listQuestions");
        %>
        
        <h1>📚 Questões do Grupo: <%= group.getTitle() %></h1>
        <p>Descrição: <em><%= group.getDescription() %></em></p>
        <hr>
        
        <a href="GroupServlet?action=list">⬅️ Voltar para Grupos</a>
        <a href="QuestionServlet?action=new&groupId=<%= group.getGroupId() %>" style="float: right;">➕ Adicionar Nova Questão</a>

        <% if (listQuestions == null || listQuestions.isEmpty()) { %>
            <p style="text-align: center; margin-top: 50px;">Nenhuma questão encontrada neste grupo. Comece a adicionar uma!</p>
        <% } else { %>
            <h2>Total de Questões: <%= listQuestions.size() %></h2>
            
            <% int qNum = 1; %>
            <% for (Question q : listQuestions) { %>
                <div class="question-box">
                    <div class="question-text">
                        <%= qNum++ %>. <%= q.getQuestionText() %> 
                        (<span class="difficulty">Dificuldade: <%= q.getDifficulty() %></span>)
                    </div>
                    
                    <div class="alternatives">
                        <ul>
                            <% 
                                // OBS: Para simplificar, o loop abaixo assume que 
                                // você adicionou a chamada q.setAlternatives(...) no DAO.
                                if (q.getAlternatives() != null) {
                                    char id = 'A';
                                    for (Alternative alt : q.getAlternatives()) {
                                        String style = alt.isCorrect() ? "correct" : "";
                            %>
                                    <li class="<%= style %>">
                                        <%= id++ %>) <%= alt.getAlternativeText() %>
                                        <%= alt.isCorrect() ? " (CORRETA)" : "" %>
                                    </li>
                            <%
                                    }
                                }
                            %>
                        </ul>
                    </div>
                    
                    <div class="actions">
                        <a href="QuestionServlet?action=edit&id=<%= q.getQuestionId() %>&groupId=<%= group.getGroupId() %>">Editar</a>
                        <a href="QuestionServlet?action=delete&id=<%= q.getQuestionId() %>&groupId=<%= group.getGroupId() %>" 
                           onclick="return confirm('Tem certeza que deseja excluir esta questão e suas alternativas?');">Excluir</a>
                    </div>
                </div>
            <% } %>
        <% } %>
    </div>
</body>
</html>
