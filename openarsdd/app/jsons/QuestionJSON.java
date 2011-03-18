/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Answer;
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
    private boolean[] correctAnswers;
    private boolean multipleAllowed;
    private long responderID;
    private int duration;

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

    /**
     * @param question
     * @param responderID
     */
    public QuestionJSON(Question question, long responderID) {
        this.pollID = question.studentLink;
        this.questionID = question.id;
        this.answers = getAnswersArray(question);
        this.responderID = responderID;
        this.question = question.question;
        this.duration = question.duration;
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

    public boolean[] getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(boolean[] correctAnswers) {
        this.correctAnswers = correctAnswers;
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
     * Gets all answers from JSON to be inserted into Question (model)
     * @param question
     * @return
     */
    public final List<Answer> getAnswersList(Question question) {
        List<Answer> list = new ArrayList<Answer>();
        for (int i = 0; i < answers.length; i++) {
            Answer a = new Answer(question, answers[i], correctAnswers[i]);
            list.add(a);
        }
        return list;
    }

    /**
     * Makes Question object (model) from JSON object
     * @return
     */
    public Question makeModelFromJSON() {
        Question q = new Question(pollID, question, multipleAllowed);
        return q;
    }


}
