<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | Banco de Questões</title>
    
    <style>
        /* Estilos de Layout Geral */
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background-color: #f0f2f5; /* Fundo mais suave */
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
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15); /* Sombra mais profunda e moderna */
            width: 90%;
            max-width: 380px; 
            border-top: 5px solid #007bff; /* Detalhe de cor no topo */
        }
        
        /* Cabeçalho */
        .header {
            text-align: center; 
            margin-bottom: 30px;
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
            font-weight: 600; /* Mais destaque */
            color: #495057;
            font-size: 0.95em;
        }
        input[type="text"], input[type="password"] {
            width: 100%; 
            padding: 12px; 
            margin-top: 8px; 
            border: 1px solid #ced4da; 
            border-radius: 5px; 
            box-sizing: border-box;
            transition: border-color 0.2s;
        }
        input[type="text"]:focus, input[type="password"]:focus {
            border-color: #007bff; /* Foco azul */
            outline: none;
        }
        
        /* Botão de Submissão */
        .btn-login {
            width: 100%; 
            background-color: #007bff; /* Azul primário */
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
        .btn-login:hover { 
            background-color: #0056b3; 
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
        
        /* Link de Cadastro */
        .register-link {
            text-align: center; 
            margin-top: 25px;
            font-size: 0.9em;
        }
        .register-link a {
            color: #28a745; /* Verde para ação de Cadastro */
            text-decoration: none;
            font-weight: 600;
        }
        .register-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>🔑 Login</h2>
            <p>Entre com suas credenciais.</p>
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
            // Limpa a mensagem após exibir
            request.removeAttribute("message");
            request.removeAttribute("messageType");
        %>
        
        <form action="UserServlet" method="POST">
            <input type="hidden" name="action" value="login">
            
            <label for="username">Login (Nome de Usuário):</label>
            <input type="text" id="username" name="username" required autocomplete="username">
            
            <label for="password">Senha:</label>
            <input type="password" id="password" name="password" required autocomplete="current-password">

            <input type="submit" value="Fazer Login" class="btn-login">
        </form>
        
        <p class="register-link"><a href="cadastro.jsp">Ainda não tem conta? Cadastre-se</a></p>
    </div>
</body>
</html>
