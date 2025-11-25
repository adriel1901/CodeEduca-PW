package Controle;

import Modelo.QuestionGroup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionGroupDAO {

    /**
     * Cadastra um novo grupo de questões no banco.
     */
    public void save(QuestionGroup group) {
        String sql = "INSERT INTO QuestionGroup (user_id, title, description) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);

            // Define os valores
            stmt.setInt(1, group.getUserId()); // O ID do usuário criador
            stmt.setString(2, group.getTitle());
            stmt.setString(3, group.getDescription());

            stmt.executeUpdate();

            System.out.println("✅ Grupo de Questões cadastrado: " + group.getTitle());

        } catch (SQLException e) {
            // Em caso de erro (ex: problema de conexão, título duplicado, etc.)
            System.err.println("Erro ao cadastrar Grupo de Questões: " + e.getMessage());
            throw new RuntimeException("Falha ao salvar o grupo.", e);
        } finally {
            // Fechar recursos (métodos de utilidade devem ser implementados no ConnectionFactory)
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Lista todos os grupos de questões criados por um usuário.
     */
    public List<QuestionGroup> findAllByUserId(int userId) {
        String sql = "SELECT * FROM QuestionGroup WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<QuestionGroup> groups = new ArrayList<>();

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            while (rs.next()) {
                QuestionGroup group = new QuestionGroup();
                group.setGroupId(rs.getInt("group_id"));
                group.setUserId(rs.getInt("user_id"));
                group.setTitle(rs.getString("title"));
                group.setDescription(rs.getString("description"));
                group.setCreatedAt(rs.getTimestamp("created_at")); // Usar getTimestamp para DATETIME

                groups.add(group);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Grupos de Questões: " + e.getMessage());
            throw new RuntimeException("Falha ao listar grupos.", e);
        } finally {
            // Fechar recursos (ResultSet, PreparedStatement e Connection)
            ConnectionFactory.closeConnection(conn);
        }
        return groups;
    }

    /**
     * Busca um grupo de questões específico pelo ID.
     */
    public QuestionGroup findById(int groupId) {
        String sql = "SELECT * FROM QuestionGroup WHERE group_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        QuestionGroup group = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, groupId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                group = new QuestionGroup();
                group.setGroupId(rs.getInt("group_id"));
                group.setUserId(rs.getInt("user_id"));
                group.setTitle(rs.getString("title"));
                group.setDescription(rs.getString("description"));
                group.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Grupo de Questões por ID: " + e.getMessage());
            throw new RuntimeException("Falha ao buscar grupo.", e);
        } finally {
            // Fechar recursos (omiti o fechamento de stmt e rs para simplificar, 
            // mas é recomendado usar métodos de utilidade para fechar todos)
            ConnectionFactory.closeConnection(conn);
        }
        return group;
    }

    public void update(QuestionGroup group) {
        String sql = "UPDATE QuestionGroup SET title = ?, description = ? WHERE group_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);

            // Define os novos valores
            stmt.setString(1, group.getTitle());
            stmt.setString(2, group.getDescription());
            stmt.setInt(3, group.getGroupId()); // ID para a cláusula WHERE

            stmt.executeUpdate();

            System.out.println("✅ Grupo de Questões atualizado: " + group.getTitle());

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Grupo de Questões: " + e.getMessage());
            throw new RuntimeException("Falha ao atualizar o grupo.", e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Deleta um grupo de questões pelo ID.
     */
    public void delete(int groupId) {
        // Graças à cláusula ON DELETE CASCADE no banco, 
        // todas as questões e alternativas filhas serão deletadas automaticamente.
        String sql = "DELETE FROM QuestionGroup WHERE group_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, groupId);

            stmt.executeUpdate();

            System.out.println("✅ Grupo de Questões deletado (ID: " + groupId + ")");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar Grupo de Questões: " + e.getMessage());
            throw new RuntimeException("Falha ao deletar o grupo.", e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Lista todos os grupos de questões (para exibição pública/home).
     */
    public List<QuestionGroup> findAll() {
        String sql = "SELECT * FROM QuestionGroup ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<QuestionGroup> groups = new ArrayList<>();

        // (Lógica similar ao findAllByUserId, mas sem a cláusula WHERE user_id)
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                QuestionGroup group = new QuestionGroup();
                group.setGroupId(rs.getInt("group_id"));
                group.setUserId(rs.getInt("user_id"));
                group.setTitle(rs.getString("title"));
                group.setDescription(rs.getString("description"));
                group.setCreatedAt(rs.getTimestamp("created_at"));

                groups.add(group);
            }
        } catch (java.sql.SQLException e) {
            // ESSA É A LINHA CRÍTICA: Imprima a exceção para vê-la no console.
            System.err.println("ERRO SQL AO CARREGAR GRUPOS PÚBLICOS: " + e.getMessage());
            e.printStackTrace();

        } catch (RuntimeException e) {
            // Captura falhas de conexão vindas da ConnectionFactory
            System.err.println("ERRO DE CONEXÃO: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Não esquecer de fechar recursos
            ConnectionFactory.closeConnection(conn);
        }
        return groups;
    }
    // ... (próximos métodos)
}
