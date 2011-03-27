package models;

import javax.persistence.*;
import play.db.jpa.*;

/**
 * Model class that represents vote for an answer. Is connected with one
 * voting round and there is a counter which increments by voting for a question.
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
     * @param answer Represents answer that this vote should belong to
     * @param count Count of votes for answer provided
     * @param votingRound Voting round this vote should be in
     */
    public Vote(Answer answer, int count, VotingRound votingRound) {
        this.answer = answer;
        this.count = count;
        this.votingRound = votingRound;
    }

    /**
     * @param answer Represents answer that this vote should belong to
     * @param votingRound Voting round this vote should be in
     */
    public Vote(Answer answer, VotingRound votingRound) {
        this(answer, 1, votingRound);
    }

}
