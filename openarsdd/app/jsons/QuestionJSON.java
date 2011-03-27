/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsons;

import models.Question;

/**
 * JSON that holds all information information about question
 * @author OpenARS Server API team
 */
public class QuestionJSON {

    private long pollID;
    private long questionID;
    private String question;
    private String[] answers;
    private boolean multipleAllowed;
    private String responderID;
    private int duration;
    private String email;


    /**
     * @param question Question object (model) to create JSON from
     */
    public QuestionJSON(Question question) {
        this.pollID = question.pollID;
        this.questionID = question.id;
        this.answers = getAnswersArray(question);
        this.question = question.question;
        this.duration = question.timeRemaining();
        this.multipleAllowed = question.multipleAllowed;
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

    public void setResponderID(String responderID) {
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

    public String getResponderID() {
        return responderID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets all answers from model to be inserted into QuestionJSON
     * @param question
     * @return
     */
    public final String[] getAnswersArray(Question question) {
        int size = question.answers.size();
        String[] answersArray = new String[size];
        for (int i = 0; i < size; i++) {
            answersArray[i] = question.answers.get(i).answer;
        }
        return answersArray;
    }

    /**
     * Makes Question object (model) from JSON object
     * @return
     */
    public Question makeModelFromJSON() {
        Question q = new Question(pollID, question, multipleAllowed, email);
        return q;
    }
}
