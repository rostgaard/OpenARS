package dossee.droids.entities;

/**
 * @author Tomas Verescak
 * 
 * VoteResponse retrieved from server is encapsulated in this object
 */
public class VoteResponseJSON {

	private boolean voteSuccessful;
	
    public boolean isVoteSuccessful() {
		return voteSuccessful;
	}

	public void setVoteSuccessful(boolean voteSuccessful) {
		this.voteSuccessful = voteSuccessful;
	}
}