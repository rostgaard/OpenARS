/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsons;

import models.Question;

/**
 *
 * @author veri
 */
public class QuestionJSON {

    private long pollID;
    private long questionID;
    private String question;
    private String[] answers;
    private long responderID;
    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 
     * @param question
     * @param responderID
     */
    public QuestionJSON(Question question, long responderID) {
        this.pollID = question.studentLink;
        this.questionID = question.id;
        this.answers = getAnswersFromQuestion(question);
        this.responderID = responderID;
        this.question = question.question;
        this.duration = question.duration;

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

    public final String[] getAnswersFromQuestion(Question question) {
        int size = question.Answers.size();
        String[] answersArray = new String[size];
        for (int i = 0; i < size; i++) {
            answersArray[i] = question.Answers.get(i).answer;
        }
        return answersArray;
    }
}
