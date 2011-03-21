package models;

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
    public String startDateTime;
    public String EndDateTime;

//    public VotingRound(List<Vote> votes, String startDateTime, String EndDateTime) {
//        this.votes = votes;
//        this.startDateTime = startDateTime;
//        this.EndDateTime = EndDateTime;
//    }
}
