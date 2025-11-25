package Modelo;

import java.util.List;

public class Question {

    private int questionId;
    private int groupId; // FK para o Grupo
    private String questionText;
    private String difficulty;
    private List<Alternative> alternatives;
    // Opcionalmente, pode-se incluir uma lista de alternativas aqui
    // private List<Alternative> alternatives;

    // Construtor e Getters/Setters
    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
