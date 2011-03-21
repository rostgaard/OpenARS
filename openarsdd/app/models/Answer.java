package models;

import java.util.List;
import javax.persistence.*;
import play.db.jpa.*;

/**
 * Model class for a possible answer linked to a question.
 * @author OpenARS Server API team
 */
@Entity
public class Answer extends Model {

//    public String studentLink;
    @ManyToOne
    public Question question;
    public String answer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "answer")
    public List<Vote> votes;

    public Answer(Question question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
