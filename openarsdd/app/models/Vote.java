package models;

import javax.persistence.*;
import play.db.jpa.*;

/**
 * Vote model
 * @author OpenARS Server API team
 */
@Entity
public class Vote extends Model {

    @ManyToOne
    public Answer answer;
    public int count;
    @ManyToOne
    public VotingRound votingRound;

    /**
     * @param answer
     * @param count
     * @param votingRound
     */
    public Vote(Answer answer, int count, VotingRound votingRound) {
        this.answer = answer;
        this.count = count;
        this.votingRound = votingRound;
    }

    public Vote(Answer answer, VotingRound votingRound) {
        this(answer, 1, votingRound);
    }

}
