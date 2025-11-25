package Modelo;

public class Alternative {
    
    private int alternativeId;
    private int questionId; // FK para a Questão
    private String alternativeText;
    private boolean isCorrect;
    private String identifier; // Ex: 'A', 'B', 'C'

    // Construtor e Getters/Setters

    public int getAlternativeId() { return alternativeId; }
    public void setAlternativeId(int alternativeId) { this.alternativeId = alternativeId; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public String getAlternativeText() { return alternativeText; }
    public void setAlternativeText(String alternativeText) { this.alternativeText = alternativeText; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean isCorrect) { this.isCorrect = isCorrect; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
}
