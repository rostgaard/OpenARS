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

//import models.*;
public class Voting extends Controller {

    private static Gson gson = new Gson();

    /**
     * Returns question with answers as QuestionJSON object.
     * If there is no question in DB, it returns blank string and when
     * the question is not activated it returns string "inactive".
     */
    public static void getQuestion() {
        long urlID = params.get("id", Long.class).longValue();
        Question question = Question.find("byPollID", urlID).first();

        // if there is no question render blank string
        if (question == null) {
            renderJSON("");
        }

        if (!question.isActive()) {
            renderJSON("inactive");
        }

        // otherwise render json of Question message
        QuestionJSON questionJSON = new QuestionJSON(question);
        renderJSON(questionJSON);

    }

    public static void vote() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
            String json = reader.readLine();
            System.out.println("JSON:" + json);
            VoteJSON vote = gson.fromJson(json, VoteJSON.class);

            Question question = Question.find("id = ? AND pollID = ?", vote.getQuestionID(), vote.getPollID()).first();
            if (question == null) {
                renderJSON("No such question!");
            } else if (!question.isActive()) {
                renderJSON("inactive"); // question not activated yet
            }

            String responderID = vote.getResponderID();
            System.out.println("ResponderID: " + responderID);
            
            if (Cache.get(responderID, VotingRound.class) == null) {
                Cache.add(responderID, question.getLastVotingRound(), question.timeRemaining() + "s");
            } else {
                renderJSON("already voted");
            }


//            for (String string : vote.getAnswers()) {
//                System.out.println("Answer: " + string);
//            }

            // we need to store votes for all provided answers in array
            for (String answer : vote.getAnswers()) {
                System.out.println("answer: " + answer);
                // find a question to vote for iteration variable answer
                Answer selectedAnswer = null;
                for (Answer a : question.answers) {
                    if (a.answer.equals(answer)) {
                        selectedAnswer = a;
                    }
                }
                System.out.println("selectedAnswer:" + selectedAnswer.answer);
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

    public static void getResults() {
        long urlID = params.get("id", Long.class).longValue();
        String adminKey = params.get("adminKey");
        // retrieve the question from DB
        Question question = Question.find("byPollID", urlID).first();
        if (question == null) {
            renderJSON("");
        }

        if ((adminKey != null && question.adminKey.equals(adminKey)) || !question.isActive()) {

            // vote counts
            int[] votes = new int[question.answers.size()];

            // provide question counts only when there was some voting done
            if (!question.isFresh()) {
                votes = question.getVoteCounts();
            }

            ResultsJSON result = new ResultsJSON(urlID, question.id, question.question, question.getAnswersArray(), votes);
            renderJSON(result);

        }

        if (question.isActive()) {
            renderJSON("voting in progress");
        }

    }
}
