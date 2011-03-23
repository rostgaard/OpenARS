package dossee.droids.entities;

/**
 * @author Tomas Verescak
 * 
 * Question retrieved from server is encapsulated in this object
 */
public class QuestionJSON {

    private long pollID;
    private long questionID;
    private String question;
    private String[] answers;
    private boolean multipleAllowed;
    private long responderID;
    private int duration;

    public QuestionJSON() {
    }

    public boolean isMultipleAllowed() {
        return multipleAllowed;
    }

    public void setMultipleAllowed(boolean multipleAllowed) {
        this.multipleAllowed = multipleAllowed;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }    

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public void setPollID(long pollID) {
        this.pollID = pollID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public void setResponderID(long responderID) {
        this.responderID = responderID;
    }

    public String[] getAnswers() {
        return answers;
    }

    public long getPollID() {
        return pollID;
    }

    public long getQuestionID() {
        return questionID;
    }

    public long getResponderID() {
        return responderID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}