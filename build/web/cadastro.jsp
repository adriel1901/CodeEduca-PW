<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Novo Usuário</title>
    <style>
        /* Estilos de Layout Geral */
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background-color: #f0f2f5; /* Fundo suave */
            display: flex; 
            justify-content: center; 
            align-items: center; 
            min-height: 100vh; 
            margin: 0; 
        }
        
        /* Contêiner Principal (Card) */
        .container { 
            background: white; 
            padding: 40px; 
            border-radius: 10px; 
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15); 
            width: 90%;
            max-width: 400px; 
            border-top: 5px solid #28a745; /* Detalhe de cor verde (sucesso/cadastro) */
        }
        
        /* Cabeçalho */
        .header {
            text-align: center; 
            margin-bottom: 25px;
        }
        .header h2 { 
            color: #343a40; 
            margin: 0;
            font-size: 1.8em;
        }
        .header p {
            color: #6c757d;
            margin-top: 5px;
        }

        /* Campos de Formulário */
        label { 
            display: block; 
            margin-top: 15px; 
            font-weight: 600; 
            color: #495057;
            font-size: 0.95em;
        }
        input[type="text"], input[type="email"], input[type="password"] {
            width: 100%; 
            padding: 12px; 
            margin-top: 8px; 
            border: 1px solid #ced4da; 
            border-radius: 5px; 
            box-sizing: border-box;
            transition: border-color 0.2s;
        }
        input[type="text"]:focus, input[type="email"]:focus, input[type="password"]:focus {
            border-color: #28a745; /* Foco verde */
            outline: none;
        }
        
        /* Botão de Submissão */
        .btn-register {
            width: 100%; 
            background-color: #28a745; /* Verde para ação principal */
            color: white; 
            padding: 12px; 
            margin-top: 30px; 
            border: none; 
            border-radius: 5px; 
            cursor: pointer; 
            font-size: 1.1em;
            font-weight: bold;
            transition: background-color 0.2s, transform 0.1s;
        }
        .btn-register:hover { 
            background-color: #1e7e34; 
            transform: translateY(-1px);
        }
        
        /* Mensagens de Feedback */
        .message { 
            padding: 12px; 
            margin-bottom: 20px; 
            border-radius: 5px; 
            font-weight: 600;
        }
        .success { 
            background-color: #d4edda; 
            color: #155724; 
            border: 1px solid #c3e6cb; 
        }
        .error { 
            background-color: #f8d7da; 
            color: #721c24; 
            border: 1px solid #f5c6cb; 
        }
        
        /* Link de Login */
        .login-link {
            text-align: center; 
            margin-top: 25px;
            font-size: 0.9em;
        }
        .login-link a {
            color: #007bff; /* Azul para link secundário (Login) */
            text-decoration: none;
            font-weight: 600;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Cadastre-se</h2>
            <p>Crie sua conta para gerenciar questões.</p>
        </div>
        
        <%-- Exibe mensagens de retorno --%>
        <%  
            String msg = (String) request.getAttribute("message");
            String type = (String) request.getAttribute("messageType");
            if (msg != null && type != null) {
        %>
            <p class="message <%= type %>"><%= msg %></p>
        <%
            }
        %>
        
        <form action="UserServlet" method="POST">
            <input type="hidden" name="action" value="register">
            
            <label for="full_name">Nome Completo:</label>
            <input type="text" id="full_name" name="full_name" required autocomplete="name">

            <label for="username">Login (Nome de Usuário):</label>
            <input type="text" id="username" name="username" required autocomplete="username">
            
            <label for="email">E-mail:</label>
            <input type="email" id="email" name="email" required autocomplete="email">
            
            <label for="password">Senha:</label>
            <input type="password" id="password" name="password" required autocomplete="new-password">
            
            <label for="confirm_password">Confirmar Senha:</label>
            <input type="password" id="confirm_password" name="confirm_password" required autocomplete="new-password">

            <input type="submit" value="Cadastrar" class="btn-register">
        </form>
        
        <p class="login-link"><a href="login.jsp">Já tem conta? Faça Login</a></p>
    </div>
</body>
</html>
