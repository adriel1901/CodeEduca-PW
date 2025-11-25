<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.QuestionGroup"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gerenciar Grupo de Questões</title>
    <style>
        /* CSS omitido por brevidade, mas o mesmo do passo anterior */
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
        h2 { text-align: center; color: #333; }
        label { display: block; margin-top: 15px; font-weight: bold; }
        input[type="text"], textarea { width: 100%; padding: 10px; margin-top: 5px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        textarea { resize: vertical; height: 100px; }
        input[type="submit"] { background-color: #007bff; color: white; padding: 10px 15px; margin-top: 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        input[type="submit"]:hover { background-color: #0056b3; }
        .message { padding: 10px; margin-bottom: 20px; border-radius: 4px; text-align: center; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .back-link { display: block; margin-top: 20px; text-align: center; }
    </style>
</head>
<body>
    <div class="container">
        <% 
            // 1. Determina se é Edição ou Criação
            QuestionGroup group = (QuestionGroup) request.getAttribute("group");
            boolean isEditing = (group != null && group.getGroupId() > 0);
            
            // 2. Define variáveis com base no modo
            String title = (group != null) ? group.getTitle() : "";
            String description = (group != null) ? group.getDescription() : "";
            String formTitle = (isEditing) ? "📝 Editar Grupo de Questões" : "➕ Criar Novo Grupo de Questões";
            String actionValue = (isEditing) ? "update" : "insert";
            
            String msg = (String) request.getAttribute("message");
            String type = (String) request.getAttribute("messageType");
            if (msg != null && "error".equals(type)) {
        %>
            <p class="message error"><%= msg %></p>
        <%
            }
        %>

        <h2><%= formTitle %></h2>

        <form action="GroupServlet" method="POST">
            <%-- Campos escondidos para a Ação e o ID --%>
            <input type="hidden" name="action" value="<%= actionValue %>">
            <% if (isEditing) { %>
                <input type="hidden" name="group_id" value="<%= group.getGroupId() %>">
            <% } %>
            
            <label for="title">Título do Grupo:</label>
            <input type="text" id="title" name="title" value="<%= title %>" required>

            <label for="description">Descrição (Opcional):</label>
            <textarea id="description" name="description"><%= description %></textarea>
            
            <input type="submit" value="<%= isEditing ? "Salvar Alterações" : "Salvar Grupo" %>">
        </form>
        
        <a href="GroupServlet?action=list" class="back-link">Voltar para a lista de grupos</a>
    </div>
</body>
</html>
