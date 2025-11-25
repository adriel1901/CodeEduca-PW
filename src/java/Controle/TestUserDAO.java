package Controle;


import Modelo.User;

public class TestUserDAO {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        // 1. Teste de CADASTRO (SAVE)
        User newUser = new User();
        newUser.setUsername("teste_user1");
        newUser.setPassword("senha123"); 
        newUser.setFullName("Usuário Teste Um");
        newUser.setEmail("teste.um@gmail.com");

        System.out.println("--- Testando Cadastro ---");
        dao.save(newUser);
        System.out.println("-------------------------");

        // 2. Teste de AUTENTICAÇÃO (LOGIN)
        System.out.println("\n--- Testando Login ---");
        
        // Credenciais Corretas
        User loggedUser = dao.authenticate("teste_user1", "senha123"); 
        if (loggedUser != null) {
            System.out.println("✅ Login bem-sucedido!");
            System.out.println("Bem-vindo, " + loggedUser.getFullName() + " (ID: " + loggedUser.getUserId() + ")");
        } else {
            System.out.println("❌ Falha no login: Credenciais inválidas.");
        }
        
        // Credenciais Incorretas
        User failedUser = dao.authenticate("teste_user1", "senha_errada"); 
        if (failedUser == null) {
            System.out.println("✅ Login falhou corretamente com senha errada.");
        }
    }
}
