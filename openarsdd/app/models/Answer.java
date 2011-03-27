package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import play.db.jpa.*;

/**
 * Model class for a possible answer linked to a question.
 * @author OpenARS Server API team
 */
@Entity
public class Answer extends Model {

    @ManyToOne
    public Question question;
    public String answer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "answer")
    public List<Vote> votes;

    public Answer(Question question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * Used to determine whether this answer was voted for in the last/current voting round
     * @return boolean true when it was voted for already or false otherwise
     */
    public boolean alreadyInLatestRound() {
        VotingRound vr = question.getLastVotingRound();
        List<Vote> latestVotes = vr.votes;
        for (Vote vote : latestVotes) {
            if (vote.answer.equals(this)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns list of votes that are associated with the last/current voting round.
     * @return List<Vote> list of latest votes
     */
    public List<Vote> latestVotes() {
        VotingRound lastRound = question.getLastVotingRound();
        List<Vote> latestVotes = new ArrayList<Vote>();

        if (!votes.isEmpty()) {
            for (Vote vote : votes) {
                if (vote.votingRound.equals(lastRound)) {
                    latestVotes.add(vote);
                }
            }
        }
        return latestVotes;
    }
}
