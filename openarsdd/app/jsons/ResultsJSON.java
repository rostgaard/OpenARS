/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsons;

/**
 *
 * @author veri
 */
public class ResultsJSON {

    private long pollID;
    private long questionID;
    private String question;
    private String[] answers;
    private int[] votes;

    /**
     * @param pollID
     * @param questionID
     * @param question
     * @param answers
     * @param votes
     */
    public ResultsJSON(long pollID, long questionID, String question, String[] answers, int[] votes) {
        this.pollID = pollID;
        this.questionID = questionID;
        this.question = question;
        this.answers = answers;
        this.votes = votes;
    }

    public long getPollID() {
        return pollID;
    }

    public void setPollID(long pollID) {
        this.pollID = pollID;
    }

    public long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int[] getVotes() {
        return votes;
    }

    public void setVotes(int[] votes) {
        this.votes = votes;
    }
}
