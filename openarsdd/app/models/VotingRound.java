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
public class VotingRound extends Model {

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
}
