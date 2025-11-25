<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.User"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Editar Perfil</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; }
        .container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1); width: 450px; }
        h2 { text-align: center; color: #007bff; margin-bottom: 25px; }
        label { display: block; margin-top: 15px; font-weight: bold; }
        input[type="text"], input[type="email"], input[type="password"] {
            width: 100%; padding: 10px; margin-top: 5px; border: 1px solid #ccc; border-radius: 5px; box-sizing: border-box;
        }
        input[type="submit"] {
            width: 100%; background-color: #28a745; color: white; padding: 12px; margin-top: 25px; border: none; border-radius: 5px; cursor: pointer; font-size: 1em;
            transition: background-color 0.2s;
        }
        input[type="submit"]:hover { background-color: #1e7e34; }
        .message { padding: 10px; margin-bottom: 20px; border-radius: 4px; text-align: center; }
        .success { background-color: #dff0d8; color: #3c763d; border: 1px solid #d6e9c6; }
        .error { background-color: #f2dede; color: #a94442; border: 1px solid #ebccd1; }
        .section-separator { border-top: 1px dashed #ccc; margin: 30px 0; padding-top: 20px; }
        .back-link { display: block; text-align: center; margin-top: 20px; color: #007bff; text-decoration: none; }
    </style>
</head>
<body>
    <div class="container">
        <% 
            // Assume que o usuário já está logado e o objeto User está na sessão
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // Pega o objeto de usuário que pode ter sido setado pelo Servlet após uma falha de atualização
            User displayUser = (User) request.getAttribute("userProfile") != null ? 
                               (User) request.getAttribute("userProfile") : currentUser;
            
            String msg = (String) request.getAttribute("message");
            String type = (String) request.getAttribute("messageType");
            if (msg != null && type != null) {
        %>
            <p class="message <%= type %>"><%= msg %></p>
        <%
            }
        %>
        
        <h2>👤 Edição de Perfil</h2>
        
        <form action="UserServlet" method="POST">
            <input type="hidden" name="action" value="update_profile">
            <input type="hidden" name="user_id" value="<%= displayUser.getUserId() %>">

            <label for="full_name">Nome Completo:</label>
            <input type="text" id="full_name" name="full_name" value="<%= displayUser.getFullName() %>" required>

            <label for="email">E-mail:</label>
            <input type="email" id="email" name="email" value="<%= displayUser.getEmail() %>" required>
            
            <label for="username">Login (Não pode ser alterado):</label>
            <input type="text" id="username" value="<%= displayUser.getUsername() %>" disabled>

            <input type="submit" value="Salvar Alterações do Perfil">
        </form>

        <div class="section-separator">
            <h3>🔒 Alterar Senha</h3>
            <p style="font-size: 0.9em; color: #dc3545;">*Por segurança, a senha é alterada separadamente.</p>
            
            <form action="UserServlet" method="POST">
                <input type="hidden" name="action" value="change_password">
                <input type="hidden" name="user_id" value="<%= displayUser.getUserId() %>">

                <label for="current_password">Senha Atual:</label>
                <input type="password" id="current_password" name="current_password" required>

                <label for="new_password">Nova Senha:</label>
                <input type="password" id="new_password" name="new_password" required>

                <label for="confirm_password">Confirmar Nova Senha:</label>
                <input type="password" id="confirm_password" name="confirm_password" required>

                <input type="submit" value="Mudar Senha">
            </form>
        </div>

        <a href="home.jsp" class="back-link">Voltar para o Painel</a>
    </div>
</body>
</html>