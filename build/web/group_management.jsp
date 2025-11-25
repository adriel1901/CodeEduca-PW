<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.QuestionGroup"%>
<%@page import="Modelo.User"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gerenciamento de Grupos de Questões</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
        h1 { text-align: center; color: #333; }
        .message { padding: 10px; margin-bottom: 20px; border-radius: 4px; text-align: center; }
        .success { background-color: #dff0d8; color: #3c763d; border: 1px solid #d6e9c6; }
        .error { background-color: #f2dede; color: #a94442; border: 1px solid #ebccd1; }
        .btn-new {
            display: inline-block; background-color: #28a745; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; margin-bottom: 20px;
        }
        .btn-new:hover { background-color: #1e7e34; }
        .group-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        .group-table th, .group-table td { padding: 12px; border: 1px solid #ddd; text-align: left; }
        .group-table th { background-color: #f2f2f2; }
        .actions a { margin-right: 10px; text-decoration: none; color: #007bff; }
        .actions a:last-child { color: #dc3545; }
        .actions a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="container">
        <% 
            // Garante que o usuário esteja logado (embora o filtro de Servlet faça isso)
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }
        
            // Exibe mensagens de retorno (sucesso na criação, edição ou exclusão)
            String msg = (String) request.getAttribute("message");
            String type = (String) request.getAttribute("messageType");
            if (msg != null && type != null) {
        %>
            <p class="message <%= type %>"><%= msg %></p>
        <%
            }
            
            // Pega a lista do Request (preenchida pelo GroupServlet)
            List<QuestionGroup> listGroup = (List<QuestionGroup>) request.getAttribute("listGroup");
        %>

        <h1>📚 Gerenciar Meus Grupos de Questões</h1>
        
        <p>Usuário: <%= currentUser.getUsername() %> | 
           <a href="home.jsp">🏠 Home</a> |
           <a href="LogoutServlet">🚪 Logout</a>
        </p>
        <hr>

        <a href="GroupServlet?action=new" class="btn-new">➕ Criar Novo Grupo</a>
        
        <% if (listGroup == null || listGroup.isEmpty()) { %>
            <p style="text-align: center;">Você ainda não criou nenhum grupo de questões.</p>
        <% } else { %>
            <table class="group-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Título</th>
                        <th>Descrição</th>
                        <th>Questões</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (QuestionGroup group : listGroup) { %>
                        <tr>
                            <td><%= group.getGroupId() %></td>
                            <td><%= group.getTitle() %></td>
                            <td><%= group.getDescription() %></td>
                            
                            <%-- Ação Principal: Ir para Questões --%>
                            <td>
                                <a href="QuestionServlet?action=list&groupId=<%= group.getGroupId() %>">
                                    Ver/Adicionar Questões ➡️
                                </a>
                            </td>
                            
                            <td class="actions">
                                <%-- Ações de CRUD (Edição e Exclusão) --%>
                                <a href="GroupServlet?action=edit&id=<%= group.getGroupId() %>">Editar</a>
                                
                                <a href="GroupServlet?action=delete&id=<%= group.getGroupId() %>" 
                                   onclick="return confirm('ATENÇÃO: Excluir este grupo também DELETARÁ TODAS AS QUESTÕES e ALTERNATIVAS associadas. Tem certeza?');">
                                   Excluir
                                </a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } %>
    </div>
</body>
</html>
