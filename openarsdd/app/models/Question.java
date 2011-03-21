package models;

import java.util.List;
import java.util.Random;
import javax.persistence.*;
import play.data.validation.Required;
import play.db.jpa.*;

/**
 * Model class for a poll question. This is related to answer one-to-many
 * @author OpenARS Server API team
 */
@Entity
public class Question extends Model {

    private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
    @Required
    public long pollID; //pollID
    public String adminKey;
    public String email;
    public String question;
    public boolean multipleAllowed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    public List<Answer> answers;
    public int duration;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    public List<VotingRound> votingRound;

    public Question(long pollID, String question, boolean MultipleAllowed, String email) {
        this.pollID = pollID;
        this.question = question;
        this.multipleAllowed = MultipleAllowed;
        this.email = email;
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

    /**
     * Gets array of answers as strings.
     * @return
     */
    public String[] getAnswersArray() {
        String[] array = new String[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            array[i] = answers.get(i).answer;
        }
        return array;
    }
}
