package models;

import java.util.Collections;
import java.util.Date;
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

    private static final String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
    @Required
    public long pollID; //pollID
    public String adminKey;
    public String email;
    public String question;
    public boolean multipleAllowed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    public List<Answer> answers;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    public List<VotingRound> votingRound;

    /**
     * @param pollID
     * @param question Text of the question
     * @param MultipleAllowed whether there are multiple options allowed or not
     * @param email e-mail address of the poll creator
     */
    public Question(long pollID, String question, boolean MultipleAllowed, String email) {
        this.pollID = pollID;
        this.question = question;
        this.multipleAllowed = MultipleAllowed;
        this.email = email;
    }

    /**
     * Activates the question for provided number of seconds.
     * If the question is in active state, it changes the activation duration,
     * otherwise it creates new voting round.
     * @param duration number of seconds to activate the question for
     * @return activated Question object - does not have to be used
     */
    public Question activateFor(int duration) {
        if (isActive()) {
            VotingRound lastRound = getLastVotingRound();
            lastRound.startDateTime = new Date(System.currentTimeMillis());
            lastRound.EndDateTime = new Date(lastRound.startDateTime.getTime() + duration * 1000);
            lastRound.save();
        } else {
            new VotingRound(duration, this).save();
        }
        return this;
    }

    /**
     * Gets latest voting round if it exists or null otherwise.
     * @return
     */
    public VotingRound getLastVotingRound() {
        if (votingRound.isEmpty()) {
            return null;
        }
        Collections.sort(votingRound);
        int lastIndex = votingRound.size() - 1;
        return votingRound.get(lastIndex);
    }

    /**
     * Generates random string of alphanumerical characters.
     * @param length int length of generated string
     * @return String generated string
     */
    public void generateAdminKey(int length) {
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        adminKey = sb.toString();
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

    /**
     * Can be used to determine if the question is activated or not
     * @return boolean activation status
     */
    public boolean isActive() {
        return timeRemaining() > 0;
    }

    /**
     * Returns remaining time for which the question is activated. This value
     * should be sent to the clients so that they can set the countdown. It is
     * also used by bethod isActive() to determine the activation state.
     * @return
     */
    public int timeRemaining() {
        VotingRound lastRound = getLastVotingRound();
        if (lastRound == null) {
            return 0;
        }

        Date endTime = lastRound.EndDateTime;
        Date currentTime = new Date(System.currentTimeMillis());

        int difference = (int) Math.ceil((endTime.getTime() - currentTime.getTime()) / 1000);
        return (difference > 0) ? difference : 0;
    }

    /**
     * Returns true when there has not been any voting done yet
     * @return true when there is no voting round
     */
    public boolean isFresh() {
        return getLastVotingRound() == null;
    }

    /**
     * Gets vote counts as an array of integers. Used for statistics.
     * @return int[] array of vote counts / results
     */
    public int[] getVoteCounts() {
        int index = 0;
        int[] votes = new int[answers.size()];

        if (isFresh()) {
            return votes;
        }

        for (Answer answer : answers) {
            List<Vote> votesList = answer.latestVotes();
            if (votesList.isEmpty()) {
                votes[index] = 0;
            } else {
                votes[index] = votesList.get(0).count;
            }
            index++;
        }
        return votes;
    }

    @Override
    public String toString() {
        return "AdminKey: " + this.adminKey + " PollID: " + this.pollID;
    }
}
