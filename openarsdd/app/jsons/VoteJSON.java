/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsons;

/**
 *
 * @author veri
 */
public class VoteJSON {

    private long pollID;
    private long questionID;
    private String[] answers;
    private long responderID;

    public VoteJSON(long pollID, long questionID, String[] answers, long responderID) {
        this.pollID = pollID;
        this.questionID = questionID;
        this.answers = answers;
        this.responderID = responderID;
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

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public long getResponderID() {
        return responderID;
    }

    public void setResponderID(long responderID) {
        this.responderID = responderID;
    }
}
