<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bem-vindo ao Banco de Questões</title>
    <link rel="icon" type="image/x-icon" href="imagens/favicon.ico">
    <style>
        /* Variáveis de Cor para Consistência */
        :root {
            --primary-color: #007bff;
            --success-color: #28a745;
            --background-color: #f0f2f5;
            --text-dark: #343a40;
            --text-light: #495057;
            --shadow-light: rgba(0, 0, 0, 0.1);
            --shadow-strong: rgba(0, 0, 0, 0.15);
        }

        /* Estilos de Layout Geral */
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background-color: var(--background-color); 
            display: flex; 
            justify-content: center; 
            align-items: center; 
            min-height: 100vh; 
            margin: 0; 
        }
        
        /* Contêiner Principal (Card Central) */
        .container { 
            background: white; 
            padding: 50px; 
            border-radius: 12px; 
            box-shadow: 0 10px 30px var(--shadow-strong);
            width: 90%;
            max-width: 850px; /* Aumentado um pouco mais */
            display: flex;
            gap: 50px;
            align-items: flex-start;
        }

        /* LOGO e TÍTULO - Componente de Boas-Vindas */
        .welcome-section {
            flex: 2;
            padding-right: 30px;
            border-right: 1px solid #dee2e6;
            text-align: center; /* Centraliza logo e título */
        }
        .logo-container {
            margin-bottom: 0px; 
        }
        .logo-container img {
            max-width: 130px; /* Logo maior e mais impactante */
            height: auto;
            border-radius: 12px; 
        }
        .welcome-section h1 {
            color: var(--primary-color);
            font-size: 2.5em;
            margin-top: 10px;
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 15px;
        }
        .welcome-section p {
            color: var(--text-light);
            line-height: 1.7;
            text-align: left;
            margin-top: 20px;
        }
        .highlight-text {
            display: block;
            margin-top: 35px;
            font-weight: 700;
            color: var(--text-dark);
            font-size: 1.1em;
        }

        /* Coluna de Acesso (Login/Cadastro) */
        .access-section {
            flex: 1;
            text-align: center;
            padding-top: 50px; /* Alinhamento visual com o conteúdo do lado esquerdo */
        }
        .access-section h2 {
            color: var(--text-dark);
            font-size: 1.5em;
            margin-bottom: 30px;
        }
        .access-link {
            display: block;
            width: 100%;
            padding: 14px; /* Botões maiores */
            margin-bottom: 20px;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
            font-size: 1.1em;
            transition: background-color 0.2s, transform 0.1s;
        }
        
        /* Estilos específicos dos botões */
        .btn-login { 
            background-color: var(--primary-color); 
            color: white; 
            border: none;
        }
        .btn-login:hover { 
            background-color: #0056b3; 
            transform: translateY(-2px);
            box-shadow: 0 4px 8px var(--shadow-light);
        }

        .btn-register { 
            background-color: var(--success-color); 
            color: white; 
            border: none;
        }
        .btn-register:hover { 
            background-color: #1e7e34;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px var(--shadow-light);
        }
        
        /* Links adicionais */
        .access-section .info-text { 
            font-size: 0.9em; 
            margin-top: 25px;
            color: #6c757d;
        }
        
        /* Responsividade */
        @media (max-width: 768px) {
            .container { 
                flex-direction: column; 
                align-items: stretch; 
                padding: 30px;
            }
            .welcome-section {
                border-right: none;
                border-bottom: 1px solid #dee2e6;
                padding-right: 0;
                padding-bottom: 30px;
                margin-bottom: 20px;
                text-align: center;
            }
            .access-section {
                padding-top: 0;
            }
            .welcome-section p {
                text-align: center;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        
        <div class="welcome-section">
            
            <%-- NOVO: INSERÇÃO E ESTILIZAÇÃO DO LOGO --%>
            <div class="logo-container">
                <%-- Corrigido para apontar para a pasta 'imagens/' --%>
                <img src="logo2.png" alt="Logo do Banco de Questões"> 
            </div>
            
            <h1>Banco de Questões Online</h1>
            <p>Seja bem-vindo(a)! Nossa plataforma é o seu centro de controle para criar, organizar e gerenciar seus próprios grupos de questões de forma eficiente.</p>
            <p>Utilize o Modo Estudo para realizar quizzes e praticar tópicos criados por você ou pela comunidade. O sistema foi construído para ser rápido e intuitivo.</p>
            
            <span class="highlight-text">Para começar sua jornada de gestão de conteúdo, utilize as opções de acesso ao lado.</span>
        </div>
        
        <div class="access-section">
            <h2>Acesso Rápido</h2>
            
            <a href="login.jsp" class="access-link btn-login">
                Fazer Login
            </a>
            
            <a href="cadastro.jsp" class="access-link btn-register">
                Cadastre-se Agora
            </a>
            
            <div class="info-text">
                <hr style="margin: 20px 0;">
                Se você já está logado, seu dashboard será exibido automaticamente.
            </div>
        </div>
        
    </div>
</body>
</html>
