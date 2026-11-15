package com.oasis.exam;

public class Question {
    private final String prompt;
    private final String[] options;
    private final int correctIndex;
    private final String explanation;

    public Question(String prompt, String[] options, int correctIndex, String explanation) {
        if (options == null || options.length != 4 || correctIndex < 0 || correctIndex > 3)
            throw new IllegalArgumentException("Each question must have four options and a valid answer.");
        this.prompt = prompt;
        this.options = options.clone();
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }
    public String getPrompt() { return prompt; }
    public String[] getOptions() { return options.clone(); }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }
}
