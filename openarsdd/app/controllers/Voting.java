package controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jsons.QuestionJSON;
import jsons.ResultsJSON;
import jsons.VoteJSON;
import jsons.VoteResponseJSON;
import models.Answer;
import models.Question;
import models.Vote;
import models.VotingRound;
import play.cache.Cache;
import play.mvc.*;

/**
 * Controller which takes care of functions that are mainly for poll users
 * @author OpenARS Server API team
 */
public class Voting extends Controller {

    private static Gson gson = new Gson();

    /**
     * Returns question with answers as QuestionJSON object.
     * If there is no question in DB, it returns blank string and when
     * the question is not activated it returns string "inactive". <br/>
     * URL: <server>/{id} <br/>
     */
    public static void getQuestion() {
        long urlID = params.get("id", Long.class).longValue();
        Question question = Question.find("byPollID", urlID).first();

        if (question == null) {
            renderJSON("");
        }

        if (!question.isActive()) {
            renderJSON("inactive");
        }

        // render Question JSON only when question is active
        renderJSON(new QuestionJSON(question));

    }

    /**
     * Retrieves votes from the request body and stores them in the database.
     * One client can vote only once during one voting round. His/her responder
     * ID is stored in the cache for duration of the poll. <br/>
     * It responds with "No such question!" if the question does not exist,
     * "inactive" if the question is not in the active state, "already voted"
     * if the responder has already voted in this voting round. It stores the vote
     * only if nothing from above is true and it responds with VoteResponseJSON which
     * has voteSuccessful set to true;
     * URL: <server>/vote/{id} <br/>
     * Method: POST <br/>
     * Parameter {id} - poll ID <br/>
     * Request body: VoteJSON
     */
    public static void vote() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
            String json = reader.readLine();
            VoteJSON vote = gson.fromJson(json, VoteJSON.class);

            Question question = Question.find("id = ? AND pollID = ?", vote.getQuestionID(), vote.getPollID()).first();
            if (question == null) {
                renderJSON("No such question!");
            } else if (!question.isActive()) {
                renderJSON("inactive"); // question not activated yet
            }

            String responderID = vote.getResponderID();

            if (Cache.get(responderID, VotingRound.class) == null) {
                Cache.add(responderID, question.getLastVotingRound(), question.timeRemaining() + "s");
            } else {
                renderJSON("already voted");
            }

            // we need to store votes for all provided answers in array
            for (String answer : vote.getAnswers()) {
                // find a question to vote for iteration variable answer
                Answer selectedAnswer = null;
                for (Answer a : question.answers) {
                    if (a.answer.equals(answer)) {
                        selectedAnswer = a;
                    }
                }
                // if there are no votes for this answer in DB, create one
                if (!selectedAnswer.alreadyInLatestRound()) {
                    new Vote(selectedAnswer, question.getLastVotingRound()).save();
                } else {
                    // otherwise just increment the count value
                    for (Vote v : selectedAnswer.votes) {
                        if (v.votingRound.equals(question.getLastVotingRound())) {
                            v.count++;
                            v.save();
                        }
                    }
                }
            }
            renderJSON(new VoteResponseJSON(true));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns results of the answer. It returns results when the question is not
     * in active state, or when there is a correct adminLink provided as a second
     * parameter<br/>
     * URL: <server>/getResults/{id} <br/>
     * Method: GET <br/>
     * Parameter {id} - poll ID <br/>
     * Parameter {adminKey} - randomly generated string at question creation <br/>
     */
    public static void getResults() {
        long urlID = params.get("id", Long.class).longValue();
        String adminKey = params.get("adminKey");
        // retrieve the question from DB
        Question question = Question.find("byPollID", urlID).first();
        if (question == null) {
            renderJSON("");
        }
        // return results when correct adminKey is provided or when question is not active
        if ((adminKey != null && question.adminKey.equals(adminKey)) || !question.isActive()) {
            int[] votes = question.getVoteCounts();
            ResultsJSON result = new ResultsJSON(urlID, question.id, question.question, question.getAnswersArray(), votes);
            renderJSON(result);
        }

        // otherwise do not respond with results
        if (question.isActive()) {
            renderJSON("voting in progress");
        }

    }
}
