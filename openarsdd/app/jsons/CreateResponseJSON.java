/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsons;

/**
 *
 * @author veri
 */
public class CreateResponseJSON {

    private long pollID;
    private String adminKey;

    public CreateResponseJSON(long pollID, String adminKey) {
        this.pollID = pollID;
        this.adminKey = adminKey;
    }

    public long getPollID() {
        return pollID;
    }

    public void setPollID(long pollID) {
        this.pollID = pollID;
    }

    public String getAdminKey() {
        return adminKey;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }
}
