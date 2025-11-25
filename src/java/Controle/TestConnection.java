package Controle;


import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            System.out.println("✅ Conexão com o banco de dados estabelecida com sucesso!");
        } catch (RuntimeException e) {
            System.out.println("❌ Falha na conexão: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection(con);
            System.out.println("Conexão fechada.");
        }
    }
}
