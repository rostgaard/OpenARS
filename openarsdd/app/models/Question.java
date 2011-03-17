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
    public String adminLink;
    public String studentLink; //pollID
    public String question;
    public boolean MultipleAllowed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    public List<Answer> Answers;

    public Question(String adminLink, String studentLink, String question, boolean MultipleAllowed) {
        this.adminLink = adminLink;
        this.studentLink = studentLink;
        this.question = question;
        this.MultipleAllowed = MultipleAllowed;
    }

    
}
