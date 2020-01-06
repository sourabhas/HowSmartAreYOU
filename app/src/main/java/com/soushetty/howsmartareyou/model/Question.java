package com.soushetty.howsmartareyou.model;

/* Class defined to use the question and it's answer accordingly.Constructors ,getter and setter methods generated*/
public class Question {
    private String answer;
    private boolean answerTrue;

    public Question() {
    }
    //Constructor using the two declared variables :question and its answer(true or false)
    public Question(String answer, boolean answerTrue) {
        this.answer = answer;
        this.answerTrue = answerTrue;
    }

    //getters and setters method
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }

    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", answerTrue=" + answerTrue +
                '}';
    }
}
