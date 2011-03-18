package models;

import java.util.List;
import javax.persistence.*;
import play.data.validation.Required;
import play.db.jpa.*;

/**
 *
 * @author veri
 */
@Entity
public class Question extends Model {

    @Required
    public long studentLink; //pollID
    public long adminLink;
    public String question;
    public boolean MultipleAllowed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    public List<Answer> Answers;
    public int duration;

    public Question(long studentLink, String question, boolean MultipleAllowed) {
        this.studentLink = studentLink;
        this.question = question;
        this.MultipleAllowed = MultipleAllowed;
    }

    public void activateFor(int duration) {
        this.duration = duration;
    }

}
