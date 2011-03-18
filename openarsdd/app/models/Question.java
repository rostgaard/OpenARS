package models;

import java.util.List;
import java.util.Random;
import javax.persistence.*;
import play.data.validation.Required;
import play.db.jpa.*;

/**
 *
 * @author veri
 */
@Entity
public class Question extends Model {

    private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
    
    @Required
    public long studentLink; //pollID
    public String password;
    public String question;
    public boolean multipleAllowed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    public List<Answer> answers;
    public int duration;

    public Question(long studentLink, String question, boolean MultipleAllowed) {
        this.studentLink = studentLink;
        this.question = question;
        this.multipleAllowed = MultipleAllowed;
    }

    public void activateFor(int duration) {
        this.duration = duration;
    }

    public static String getRandomString(int length) {
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }
}
