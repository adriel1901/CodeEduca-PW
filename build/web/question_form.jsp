<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.QuestionGroup"%>
<%@page import="Modelo.Question"%>
<%@page import="Modelo.Alternative"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gerenciar Questão</title>
    <style>
        /* (CSS omitido por brevidade) */
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
        h2 { border-bottom: 2px solid #007bff; padding-bottom: 10px; margin-top: 0; }
        label { display: block; margin-top: 15px; font-weight: bold; }
        textarea, input[type="text"], select { width: 100%; padding: 10px; margin-top: 5px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        textarea { resize: vertical; height: 100px; }
        .alternative-section { margin-top: 20px; padding: 15px; border: 1px dashed #bbb; border-radius: 4px; }
        .alternative-item { display: flex; align-items: center; margin-bottom: 10px; }
        .alternative-item input[type="text"] { flex-grow: 1; margin-right: 10px; }
        .alternative-item input[type="radio"] { margin-right: 5px; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 20px; border-radius: 4px; }
        input[type="submit"] { background-color: #28a745; color: white; padding: 10px 15px; margin-top: 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        input[type="submit"]:hover { background-color: #1e7e34; }
    </style>
</head>
<body>
    <div class="container">
        <% 
            QuestionGroup group = (QuestionGroup) request.getAttribute("group");
            Question question = (Question) request.getAttribute("question");
            
            // Determina se está editando (se a flag ou o objeto 'question' estiver presente)
            boolean isEditing = (question != null && request.getAttribute("isEditing") != null);
            
            String formTitle = isEditing ? "✏️ Editar Questão (ID: " + question.getQuestionId() + ")" : "➕ Adicionar Nova Questão";
            String actionValue = isEditing ? "update" : "insert";
            String qText = isEditing ? question.getQuestionText() : "";
            String difficulty = isEditing ? question.getDifficulty() : "";
            List<Alternative> alternatives = isEditing ? question.getAlternatives() : new ArrayList<>();

            String msg = (String) request.getAttribute("message");
        %>
        
        <h2><%= formTitle %> no Grupo: <%= group.getTitle() %></h2>
        
        <% if (msg != null) { %>
            <p class="error"><%= msg %></p>
        <% } %>

        <form action="QuestionServlet" method="POST">
            <input type="hidden" name="action" value="<%= actionValue %>">
            <input type="hidden" name="groupId" value="<%= group.getGroupId() %>">
            
            <% if (isEditing) { %>
                <input type="hidden" name="questionId" value="<%= question.getQuestionId() %>">
            <% } %>
            
            <label for="question_text">Enunciado da Questão:</label>
            <textarea id="question_text" name="question_text" required><%= qText %></textarea>

            <label for="difficulty">Nível de Dificuldade:</label>
            <select id="difficulty" name="difficulty" required>
                <option value="">-- Selecione --</option>
                <option value="Fácil" <%= "Fácil".equals(difficulty) ? "selected" : "" %>>Fácil</option>
                <option value="Médio" <%= "Médio".equals(difficulty) ? "selected" : "" %>>Médio</option>
                <option value="Difícil" <%= "Difícil".equals(difficulty) ? "selected" : "" %>>Difícil</option>
            </select>

            <div class="alternative-section">
                <h3>Opções de Resposta (No máximo 5)</h3>
                <p>Marque a alternativa **correta** abaixo.</p>

                <% char altId = 'A'; %>
                <% for (int i = 1; i <= 5; i++) { %>
                    <% 
                        String altText = "";
                        boolean isCorrect = false;
                        
                        // Pré-preenche se estiver editando
                        if (i <= alternatives.size()) {
                            Alternative alt = alternatives.get(i - 1);
                            altText = alt.getAlternativeText();
                            isCorrect = alt.isCorrect();
                        }
                    %>
                    <label>Alternativa <%= altId %>:</label>
                    <div class="alternative-item">
                        <input type="text" name="alternative_<%= i %>_text" value="<%= altText %>" <%= (i <= 2 && !isEditing) ? "required" : "" %> placeholder="Texto da alternativa <%= altId %>">
                        
                        <input type="radio" name="correct_alternative" value="<%= i %>" id="correct_<%= i %>" <%= isCorrect ? "checked" : "" %> required>
                        <label for="correct_<%= i %>" style="margin: 0; font-weight: normal;">Correta</label>
                    </div>
                    <% altId++; %>
                <% } %>
            </div>

            <input type="submit" value="<%= isEditing ? "Salvar Alterações" : "Salvar Questão" %>">
        </form>
        
        <p><a href="QuestionServlet?action=list&groupId=<%= group.getGroupId() %>">Voltar para a lista de questões</a></p>
    </div>
</body>
</html>