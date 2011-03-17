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
public class Answer extends Model {

    public String studentLink;
    @ManyToOne
    public Question question;
    public String answer;
    public boolean isCorrect;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "answer")
    public List<Vote> votes;

    public Answer(Question question, String answer, boolean isCorrect) {
        this.question = question;
        this.answer = answer;
        this.isCorrect = isCorrect;
    }


}
