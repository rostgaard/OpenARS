package jsons;

/**
 * @author veri
 */
public class VoteResponseJSON {

    private boolean voteSuccessful;

    /**
     * @param voteSuccessful
     */
    public VoteResponseJSON(boolean voteSuccessful) {
        this.voteSuccessful = voteSuccessful;
    }

    public boolean getStatus() {
        return voteSuccessful;
    }

    public void setStatus(boolean status) {
        this.voteSuccessful = status;
    }
}
