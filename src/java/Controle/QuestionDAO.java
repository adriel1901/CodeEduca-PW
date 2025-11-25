package Controle;

import Modelo.Alternative;
import Modelo.Question;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class QuestionDAO {

    /**
     * Salva uma Questão e suas Alternativas em uma transação.
     */
    public void save(Question question, List<Alternative> alternatives) {
        String sqlQuestion = "INSERT INTO Question (group_id, question_text, difficulty) VALUES (?, ?, ?)";
        String sqlAlternative = "INSERT INTO Alternative (question_id, alternative_text, is_correct, identifier) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmtQuestion = null;
        PreparedStatement stmtAlternative = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia a transação!

            // 1. Salvar a Questão
            // Statement.RETURN_GENERATED_KEYS é crucial para pegar o ID da questão recém-inserida
            stmtQuestion = conn.prepareStatement(sqlQuestion, Statement.RETURN_GENERATED_KEYS);
            stmtQuestion.setInt(1, question.getGroupId());
            stmtQuestion.setString(2, question.getQuestionText());
            stmtQuestion.setString(3, question.getDifficulty());
            stmtQuestion.executeUpdate();

            // 2. Recuperar o ID gerado para a Questão
            int generatedQuestionId = 0;
            try (ResultSet rs = stmtQuestion.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedQuestionId = rs.getInt(1);
                } else {
                    throw new SQLException("Falha ao criar questão, nenhum ID gerado.");
                }
            }

            // 3. Salvar as Alternativas usando o ID gerado
            stmtAlternative = conn.prepareStatement(sqlAlternative);
            for (Alternative alt : alternatives) {
                stmtAlternative.setInt(1, generatedQuestionId);
                stmtAlternative.setString(2, alt.getAlternativeText());
                stmtAlternative.setBoolean(3, alt.isCorrect());
                stmtAlternative.setString(4, alt.getIdentifier());
                stmtAlternative.addBatch(); // Adiciona ao lote para execução eficiente
            }
            stmtAlternative.executeBatch(); // Executa o lote

            conn.commit(); // Confirma a transação
            System.out.println("✅ Questão e alternativas salvas com sucesso (ID: " + generatedQuestionId + ")");

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Desfaz tudo em caso de erro
                    System.err.println("❗ Transação desfeita. Erro: " + e.getMessage());
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao tentar fazer rollback: " + ex.getMessage());
            }
            throw new RuntimeException("Falha ao salvar a questão e alternativas.", e);
        } finally {
            // Fechar recursos (simplificado)
            try {
                if (stmtQuestion != null) {
                    stmtQuestion.close();
                }
                if (stmtAlternative != null) {
                    stmtAlternative.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Restaura o auto-commit
                }
            } catch (SQLException ignore) {
                /* Ignorar erros ao fechar */ }
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Busca todas as alternativas de uma questão.
     */
    public List<Alternative> findAlternativesByQuestionId(int questionId) {
        String sql = "SELECT * FROM Alternative WHERE question_id = ? ORDER BY alternative_id";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Alternative> alternatives = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, questionId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Alternative alt = new Alternative();
                alt.setAlternativeId(rs.getInt("alternative_id"));
                alt.setQuestionId(rs.getInt("question_id"));
                alt.setAlternativeText(rs.getString("alternative_text"));
                alt.setCorrect(rs.getBoolean("is_correct"));
                alt.setIdentifier(rs.getString("identifier"));
                alternatives.add(alt);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alternativas.", e);
        } finally {
            // Fechar recursos
            ConnectionFactory.closeConnection(conn);
        }
        return alternatives;
    }

    /**
     * Lista todas as questões de um grupo, carregando suas alternativas.
     */
    public List<Question> findAllByGroupId(int groupId) {
        // SQL para buscar as questões
        String sql = "SELECT * FROM Question WHERE group_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Question> questions = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, groupId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Question q = new Question();
                int questionId = rs.getInt("question_id");

                q.setQuestionId(questionId);
                q.setGroupId(rs.getInt("group_id"));
                q.setQuestionText(rs.getString("question_text"));
                q.setDifficulty(rs.getString("difficulty"));

                List<Alternative> alternatives = findAlternativesByQuestionId(questionId);
                q.setAlternatives(alternatives); // POPULA O OBJETO QUESTION
                questions.add(q);
            }

            // ⚠️ OBSERVAÇÃO: Para carregar as alternativas, você deve adicionar
            // private List<Alternative> alternatives;
            // ao POJO Question. E aqui, chamar o método findAlternativesByQuestionId.
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar questões.", e);
        } finally {
            // Fechar recursos
            ConnectionFactory.closeConnection(conn);
        }
        return questions;
    }

    /**
     * Busca uma Questão e suas alternativas.
     */
    public Question findByIdWithAlternatives(int questionId) {
        Question question = null;

        // 1. Busca a Questão
        String sqlQ = "SELECT * FROM Question WHERE question_id = ?";
        Connection conn = null;
        PreparedStatement stmtQ = null;
        ResultSet rsQ = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmtQ = conn.prepareStatement(sqlQ);
            stmtQ.setInt(1, questionId);
            rsQ = stmtQ.executeQuery();

            if (rsQ.next()) {
                question = new Question();
                question.setQuestionId(questionId);
                question.setGroupId(rsQ.getInt("group_id"));
                question.setQuestionText(rsQ.getString("question_text"));
                question.setDifficulty(rsQ.getString("difficulty"));

                // 2. Busca e anexa as Alternativas
                List<Alternative> alternatives = findAlternativesByQuestionId(questionId);
                question.setAlternatives(alternatives);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar questão para edição.", e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return question;
    }

    /**
     * Atualiza a Questão e suas Alternativas em uma transação.
     */
    public void update(Question question, List<Alternative> alternatives) {
        String sqlQ = "UPDATE Question SET question_text = ?, difficulty = ? WHERE question_id = ?";
        String sqlDeleteA = "DELETE FROM Alternative WHERE question_id = ?";
        String sqlInsertA = "INSERT INTO Alternative (question_id, alternative_text, is_correct, identifier) VALUES (?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia a transação!

            // 1. Atualizar a Questão
            try (PreparedStatement stmtQ = conn.prepareStatement(sqlQ)) {
                stmtQ.setString(1, question.getQuestionText());
                stmtQ.setString(2, question.getDifficulty());
                stmtQ.setInt(3, question.getQuestionId());
                stmtQ.executeUpdate();
            }

            // 2. Deletar Alternativas Antigas
            try (PreparedStatement stmtDeleteA = conn.prepareStatement(sqlDeleteA)) {
                stmtDeleteA.setInt(1, question.getQuestionId());
                stmtDeleteA.executeUpdate();
            }

            // 3. Inserir Novas Alternativas
            try (PreparedStatement stmtInsertA = conn.prepareStatement(sqlInsertA)) {
                for (Alternative alt : alternatives) {
                    stmtInsertA.setInt(1, question.getQuestionId());
                    stmtInsertA.setString(2, alt.getAlternativeText());
                    stmtInsertA.setBoolean(3, alt.isCorrect());
                    stmtInsertA.setString(4, alt.getIdentifier());
                    stmtInsertA.addBatch();
                }
                stmtInsertA.executeBatch();
            }

            conn.commit(); // Confirma a transação
            System.out.println("✅ Questão e alternativas atualizadas com sucesso (ID: " + question.getQuestionId() + ")");

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Desfaz tudo
                }
            } catch (SQLException ex) {
                /* Logar erro de rollback */ }
            throw new RuntimeException("Falha ao atualizar a questão: " + e.getMessage(), e);
        } finally {
            // Fechar conexão e restaurar auto-commit
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ignore) {
            }
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Deleta a Questão e suas Alternativas.
     */
    public void delete(int questionId) {
        // SQL DELETE na tabela Question (o CASCADE cuida das alternativas)
        String sql = "DELETE FROM Question WHERE question_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, questionId);
            stmt.executeUpdate();

            System.out.println("✅ Questão deletada (ID: " + questionId + ")");

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao deletar a questão.", e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }
}
