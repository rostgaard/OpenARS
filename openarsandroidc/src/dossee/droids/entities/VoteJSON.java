package dossee.droids.entities;

/**
 * @author Tomas Verescak
 * 
 * Vote is encapsulated in this object
 */
public class VoteJSON {

    private long pollID;
    private long questionID;
    private String[] answers;
    private String responderID;

    public VoteJSON(long pollID, long questionID, String[] answers, String responderID) {
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

    public String getResponderID() {
        return responderID;
    }

    public void setResponderID(String responderID) {
        this.responderID = responderID;
    }
}