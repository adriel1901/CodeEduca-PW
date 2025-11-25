package Controle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // 1. Defina as constantes de conexão
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // Driver JDBC do MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/bancodequestoes?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "root"; // Usuário do seu MySQL (altere se for diferente)
    private static final String PASS = "meo0511"; // Sua senha do MySQL

    /**
     * Método que estabelece e retorna uma nova conexão com o Banco de Dados.
     * @return Objeto Connection pronto para ser usado.
     */
    public static Connection getConnection() {
        try {
            // 2. Carrega o driver na memória
            Class.forName(DRIVER); 
            
            // 3. Retorna a conexão
            return DriverManager.getConnection(URL, USER, PASS);
            
        } catch (ClassNotFoundException e) {
            // Ocorre se o JAR do driver não foi encontrado
            System.err.println("Erro ao carregar o driver JDBC: " + e.getMessage());
            throw new RuntimeException("Driver do MySQL não encontrado!", e);
        } catch (SQLException e) {
            // Ocorre em caso de problemas de conexão (URL, USER, PASS errados)
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException("Falha na conexão com o banco de dados!", e);
        }
    }

    /**
     * Método de conveniência para fechar a conexão.
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
