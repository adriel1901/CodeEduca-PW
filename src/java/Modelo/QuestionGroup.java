package Modelo;

import java.util.Date;

public class QuestionGroup {
    
    private int groupId;
    private int userId; // FK para o criador do grupo
    private String title;
    private String description;
    private Date createdAt; // Tipo Date ou Timestamp para corresponder ao DATETIME do MySQL

    // Construtor padrão
    public QuestionGroup() {}

    // --- Getters e Setters ---
    
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
