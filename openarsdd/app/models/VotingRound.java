package models;

import java.util.List;
import javax.persistence.*;
import play.db.jpa.*;

/**
 *
 * @author veri
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
