<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.QuestionGroup"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Resultado do Quiz</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; }
        .result-container { max-width: 600px; width: 90%; background: white; padding: 40px; border-radius: 10px; box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1); text-align: center; }
        h1 { color: #333; margin-bottom: 5px; }
        .group-title { color: #007bff; margin-bottom: 30px; font-size: 1.2em; }
        .score-box { background-color: #eaf6ff; border: 2px solid #007bff; padding: 30px; border-radius: 8px; margin-bottom: 30px; }
        .score-text { font-size: 1.5em; color: #444; }
        .score-number { font-size: 3em; font-weight: bold; color: #28a745; }
        .actions a { 
            display: inline-block; background-color: #007bff; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; margin: 10px; font-weight: bold; transition: background-color 0.2s;
        }
        .actions a:hover { background-color: #0056b3; }
        /* Novo estilo para o botão principal de retorno */
        .actions .btn-dashboard {
            background-color: #6c757d; /* Cinza para distinção */
        }
        .actions .btn-dashboard:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
    <div class="result-container">
        <% 
            QuestionGroup group = (QuestionGroup) request.getAttribute("group");
            Integer score = (Integer) request.getAttribute("score");
            Integer totalQuestions = (Integer) request.getAttribute("totalQuestions");
            
            // Verifica se a pontuação é válida
            if (score == null || totalQuestions == null) {
                // Redireciona para o índice público se faltarem dados
                response.sendRedirect("PublicServlet?action=home"); 
                return;
            }
        %>

        <h1>Resultado do Quiz</h1>
        <p class="group-title">Grupo: <%= group.getTitle() %></p>
        
        <div class="score-box">
            <p class="score-text">Você acertou:</p>
            <p class="score-number"><%= score %></p>
            <p class="score-text">de <%= totalQuestions %> questões.</p>
        </div>

        <div class="actions">
            
            <%-- Link para tentar novamente o mesmo grupo (mantém o fluxo público) --%>
            <a href="PublicServlet?action=startQuiz&groupId=<%= group.getGroupId() %>">Tentar Novamente</a>
            
            <%-- LINK AJUSTADO: Volta diretamente para o Dashboard (home.jsp) --%>
            <a href="home.jsp" class="btn-dashboard">Voltar para o Painel</a> 
            
        </div>
    </div>
</body>
</html>