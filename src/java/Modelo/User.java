package Modelo;

public class User {
    // Atributos baseados nas colunas do banco
    private int userId;
    private String username;
    private String password; // Usado para input/cadastro
    private String fullName;
    private String email;
    // private Date createdAt; // Opcional

    // Construtor padrão
    public User() {}
    
    // --- Getters e Setters ---

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Opcional: método toString para debug
    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", username=" + username + ", fullName=" + fullName + ", email=" + email + '}';
    }
}
