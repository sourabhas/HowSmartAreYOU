package com.soushetty.howsmartareyou.model;

/*object class to store and calculate score of the quiz
* when correctly answered: +1
* when answered wrongly: -1 */
public class Score {

    private int score;

    public Score() {
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
