/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsons;

/**
 *
 * @author veri
 */
public class RequestQuestionJSON {

    private long responderID;
    private long pollID;

    public RequestQuestionJSON(long responderID, long pollID) {
        this.responderID = responderID;
        this.pollID = pollID;
    }

    public RequestQuestionJSON() {
    }

    public long getPollID() {
        return pollID;
    }

    public void setPollID(long pollID) {
        this.pollID = pollID;
    }

    public long getResponderID() {
        return responderID;
    }

    public void setResponderID(long responderID) {
        this.responderID = responderID;
    }

    @Override
    public String toString() {
        return "[responderID = " + responderID + ", pollID: " + pollID + "]";
    }
}
