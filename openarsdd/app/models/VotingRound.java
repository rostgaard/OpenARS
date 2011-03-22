package models;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import play.db.jpa.*;

/**
 * Voting round model. Having this enables us to have several voting rounds of a 
 * question. The result returned to the clients should always be the latest.
 * @author OpenARS Server API team
 */
@Entity
public class VotingRound extends Model implements Comparable<VotingRound> {

    @OneToMany(mappedBy = "votingRound")
    public List<Vote> votes;
    public Date startDateTime;
    public Date EndDateTime;
    @ManyToOne
    public Question question;

    public VotingRound(int duration, Question question) {
        this.question = question;
        this.startDateTime = new Date(System.currentTimeMillis());
        this.EndDateTime = new Date(startDateTime.getTime() + duration * 1000);
    }

    public int compareTo(VotingRound other) {
        return this.EndDateTime.compareTo(other.EndDateTime);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof VotingRound)) {
            return false;
        }
        VotingRound vr = (VotingRound) other;
        return this.EndDateTime.equals(vr.EndDateTime);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.votes != null ? this.votes.hashCode() : 0);
        hash = 71 * hash + (this.EndDateTime != null ? this.EndDateTime.hashCode() : 0);
        hash = 71 * hash + (this.question != null ? this.question.hashCode() : 0);
        return hash;
    }
}
