package Controle;

import Modelo.User; // Vamos criar este modelo em breve
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // UserDAO.java (Adicionar este método)
    /**
     * Atualiza apenas o hash da senha de um usuário.
     */
    public void updatePassword(int userId, String newPassword) {
        String sql = "UPDATE User SET password_hash = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);

            // AQUI NOVAMENTE: newPassword deve ser a senha HASHED em um sistema real!
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar senha: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Cadastra um novo usuário no banco de dados.
     */
    public void save(User user) {
        // SQL para inserção. user_id é AUTO_INCREMENT, então não o incluímos.
        String sql = "INSERT INTO User (username, password_hash, full_name, email) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);

            // 1. Define os valores para a query
            stmt.setString(1, user.getUsername());

            // ⚠️ Em um projeto real, aqui você usaria um algoritmo de hash seguro (ex: BCrypt)
            // Para simplificar agora, a hash é a própria senha.
            String passwordHash = user.getPassword();
            stmt.setString(2, passwordHash);

            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());

            // 2. Executa o comando SQL
            stmt.executeUpdate();

            System.out.println("✅ Usuário cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(conn);
            // closeStatement(stmt); // (opcional: criar um método para fechar o Statement também)
        }
    }

    /**
     * Busca um usuário pelo login e senha para fins de autenticação.
     *
     * @return O objeto User se as credenciais estiverem corretas, ou null.
     */
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM User WHERE username = ? AND password_hash = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);

            // 1. Define os parâmetros para a query
            stmt.setString(1, username);
            stmt.setString(2, password); // Comparando com o que foi salvo no banco

            // 2. Executa a busca
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Se um registro foi encontrado, preenche o objeto User
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                // Não é seguro retornar a senha, então a deixamos de fora
            }
        } catch (SQLException e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
        } finally {
            // Fechar recursos: ResultSet, Statement e Connection
            // ConnectionFactory.closeConnection(conn); // (Implementar fechamento de stmt e rs na factory para ser mais limpo)
        }
        return user;
    }

    // UserDAO.java (Método findById)
    public User findById(int userId) {
        String sql = "SELECT user_id, username, full_name, email FROM User WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                // Não carregamos a senha
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
            throw new RuntimeException("Falha ao buscar usuário.", e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return user;

    }

    // UserDAO.java (Método updateProfile)
    /**
     * Atualiza o nome completo e o email do usuário.
     */
    public void updateProfile(User user) {
        // A chave WHERE user_id garante que apenas o usuário correto seja atualizado.
        // O UNIQUE constraint no email fará o MySQL lançar uma exceção se o novo email já existir.
        String sql = "UPDATE User SET full_name = ?, email = ? WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getUserId());

            stmt.executeUpdate();
            System.out.println("✅ Perfil do usuário ID " + user.getUserId() + " atualizado.");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar perfil (SQL): " + e.getMessage());
            // Lançamos a RuntimeException para o Servlet tratar a falha (ex: email duplicado)
            throw new RuntimeException("Erro ao atualizar perfil: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

}
