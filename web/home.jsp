<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.User"%>
<%@page import="Modelo.QuestionGroup"%>
<%@page import="Controle.QuestionGroupDAO"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | Banco de Questões</title>
    <style>
        /* Estilos de Layout Geral */
        body { 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            background-color: #f0f2f5; 
            margin: 0; 
            padding: 0; 
            display: flex; 
            justify-content: center; 
            align-items: center; 
            min-height: 100vh;
        }
        .dashboard-container { 
            width: 90%;
            max-width: 1000px;
            background: #ffffff; 
            padding: 40px; 
            border-radius: 12px; 
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1); 
        }

        /* Removendo .top-nav (Não é mais necessário) */

        /* Cabeçalho e Boas-Vindas */
        .header-welcome {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #007bff;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        .header-welcome h1 { color: #333; margin: 0; font-size: 1.8em; }
        .subtitle { color: #6c757d; margin-top: 5px; font-size: 1em; }
        .user-info { color: #007bff; font-weight: 600; }
        
        /* Cartões de Ações */
        .feature-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }
        .card {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 25px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
            transition: transform 0.2s, box-shadow 0.2s;
            text-align: center;
        }
        .card:hover { transform: translateY(-5px); box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1); }
        .card h3 { color: #495057; margin-top: 0; font-size: 1.2em; }
        .card a {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 600;
            transition: background-color 0.2s;
        }
        
        /* Estilos de Cores dos Cartões */
        .card-management a { background-color: #007bff; color: white; }
        .card-profile a { background-color: #28a745; color: white; }
        .card-logout a { background-color: #dc3545; color: white; }

        /* Seção de Estudo */
        .study-section {
            padding-top: 20px;
            border-top: 1px solid #dee2e6;
        }
        .study-section h2 {
            color: #495057;
            text-align: center;
            margin-bottom: 25px;
        }
        .study-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }
        .group-card {
            background: #f8f8f8;
            border: 1px solid #e9ecef;
            padding: 20px;
            border-radius: 8px;
        }
        .group-card h4 {
            color: #007bff;
            margin-top: 0;
            margin-bottom: 10px;
        }
        .btn-start {
            display: block;
            width: 100%;
            padding: 10px 0;
            margin-top: 15px;
            background-color: #ffc107;
            color: #333;
            text-align: center;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 600;
            transition: background-color 0.2s;
        }
        .btn-start:hover {
            background-color: #e0a800;
        }

        /* Footer */
        .footer-note {
            text-align: center;
            margin-top: 30px;
            font-size: 0.8em;
            color: #adb5bd;
        }
    </style>
</head>
<body>
    <%
        // Obtém o objeto User da sessão e verifica o login
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return; 
        }
        
        // **LÓGICA OTIMIZADA PARA CARREGAR GRUPOS**
        QuestionGroupDAO groupDAO = new QuestionGroupDAO();
        List<QuestionGroup> publicGroups = null;
        try {
            // Chama o método robusto findAll() que você implementou
            publicGroups = groupDAO.findAll(); 
        } catch (RuntimeException e) {
            System.err.println("ERRO ao carregar grupos para a Home: " + e.getMessage());
            // Garante que a lista seja vazia em caso de falha para o JSP não quebrar
            publicGroups = java.util.Collections.emptyList();
        }
    %>
    
    <div class="dashboard-container">
        
        <div class="header-welcome">
            <div>
                <h1>Bem-vindo(a), <span class="user-info"><%= user.getFullName() %></span>!</h1>
                <p class="subtitle">Seu Painel de Controle e Gestão de Questões. 🎉</p>
            </div>
            <p>Login: <%= user.getUsername() %></p>
        </div>
        
        <%-- Seção de Ações Principais (Grid de Cartões) --%>
        <div class="feature-grid">
            
            <%-- Cartão 1: Gerenciamento de Conteúdo --%>
            <div class="card card-management">
                <h3>📚 Gerenciar Conteúdo</h3>
                <p>Crie, edite e organize seus grupos e o conteúdo das questões.</p>
                <a href="GroupServlet?action=list">Gerenciar Conteúdo</a>
            </div>

            <%-- Cartão 2: Perfil do Usuário --%>
            <div class="card card-profile">
                <h3>👤 Meu Perfil</h3>
                <p>Atualize seu nome, email e senha de acesso.</p>
                <a href="UserServlet?action=profile">Editar Perfil</a> 
            </div>

            <%-- Cartão 3: Logout --%>
            <div class="card card-logout">
                <h3>🚪 Sair do Sistema</h3>
                <p>Encerre sua sessão de forma segura.</p>
                <a href="LogoutServlet">Fazer Logout</a>
            </div>
            
        </div>
        
        <hr>

        <%-- Seção de Modo Estudo (Integração) --%>
        <div class="study-section">
            <h2>🔎 Tópicos para Estudo (Quiz Público)</h2>
            <p style="text-align: center; margin-bottom: 20px;">Use este conteúdo para praticar. O progresso não é salvo.</p>

            <% if (publicGroups != null && !publicGroups.isEmpty()) { %>
                <div class="study-grid">
                    <% for (QuestionGroup group : publicGroups) { %>
                        <div class="group-card">
                            <h4><%= group.getTitle() %></h4>
                            <p style="font-size: 0.9em;"><%= group.getDescription() %></p>
                            
                            <%-- Link para iniciar o Quiz (PublicServlet) --%>
                            <a href="PublicServlet?action=startQuiz&groupId=<%= group.getGroupId() %>" class="btn-start">
                                Iniciar Quiz
                            </a>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <p style="text-align: center; color: #dc3545;">Nenhum grupo de questões disponível para estudo no momento.</p>
            <% } %>
        </div>
        
        <div class="footer-note">
            Sistema de Banco de Questões | Tomcat & JSP
        </div>
    </div>
</body>
</html>