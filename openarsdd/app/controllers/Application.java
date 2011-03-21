package controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import jsons.CreateResponseJSON;
import jsons.QuestionJSON;
import jsons.ResultsJSON;
import jsons.VoteJSON;
import jsons.VoteResponseJSON;
import models.Answer;
import models.Question;
import models.Vote;
import models.VotingRound;
import play.libs.Mail;
import play.mvc.*;
import notifiers.*;

//import models.*;
public class Application extends Controller {

    /**
     * This is the default page. It should redirect to web service frontpage
     */
    public static void index() {
//        Question q = new Question(23456, "What is the capital of France?", false);
//        Answer a1 = new Answer(q, "Paris");
//        Answer a2 = new Answer(q, "London");
//
//        q.save();
//        a1.save();
//        a2.save();

        //Question question = Question.find("byStudentLink", "2345").first();
        //QuestionJSON testQ = new QuestionJSON(question, 9254);
        render();
    }

    public static void sayHello(String myName) {
        render(myName);
    }

    public static void requestQuestion() {
        long urlID = params.get("id", Long.class).longValue();

        // RequestNew message to test:  {"responderID":6547,"pollID":2345}

//        BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
//        try {
//            String json = reader.readLine();
//            Gson gson = new Gson();
//            RequestQuestionJSON requestNewMsg = gson.fromJson(json, RequestQuestionJSON.class);

        Question question = Question.find("byPollID", urlID).first();

        // if there is no question or URL is not corresponding to JSON body, render blank string
        if (question == null
                || question.pollID != urlID
                || question.duration <= 0) {
            System.out.println("Question is null: " + (question == null));
//                System.out.println("StudentLink != ID: " + (question.pollID != urlID));
            System.out.println("Inactive: " + (question.duration <= 0));
            renderJSON(new String());
        }

        // otherwise render json of Question message
        QuestionJSON testQ = new QuestionJSON(question);
        renderJSON(testQ);

//        } catch (IOException ex) {
//            ex.printStackTrace();
        renderJSON(new String());
//        }

    }

    public static void createQuestion() {

        // RequestNew message to test:  {"responderID":6547,"pollID":2345}

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
        try {
            String json = reader.readLine();
            Gson gson = new Gson();
            QuestionJSON questionMsg = gson.fromJson(json, QuestionJSON.class);

            Question question = questionMsg.makeModelFromJSON();

            // generate data and save question
            question.pollID = new Random(System.currentTimeMillis()).nextInt(999999);
            question.adminKey = Question.getRandomString(8);
            question.save();

            // retrieve answers from JSON and save them into database
            for (String a : questionMsg.getAnswers()) {
                System.out.println("Answer: " + a);
                new Answer(question, a).save();
            }

//            MailNotifier.sendAdminLink(question);

            renderJSON(new CreateResponseJSON(question.pollID, question.adminKey));

        } catch (IOException ex) {
            ex.printStackTrace();
            renderJSON(new String());
        }

    }

    public static void vote() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
            String json = reader.readLine();
            Gson gson = new Gson();
            VoteJSON vote = gson.fromJson(json, VoteJSON.class);

            String responderID = vote.getResponderID();



            // we need to store votes for all provided answers in array
            for (String answer : vote.getAnswers()) {
                Question question = Question.find("id = ? AND pollID = ?", vote.getQuestionID(), vote.getPollID()).first();

                // if there is no such a question return false
                if (question == null) {
                    renderJSON(new VoteResponseJSON(false));
                }

                // find a question to vote for iteration variable answer
                Answer selectedAnswer = null;
                for (Answer a : question.answers) {
                    if (a.answer.equals(answer)) {
                        selectedAnswer = a;
                    }
                }

                // if there are no votes for this answer in DB, create one
                if (selectedAnswer.votes.isEmpty()) {
                    //@TODO: more voting rounds!
                    VotingRound votingRound = new VotingRound(30, question).save();
                    new Vote(selectedAnswer, 1, votingRound).save();
                    renderJSON(new VoteResponseJSON(true));
                } // otherwise just increment the count value
                else {
                    //@TODO: more voting rounds!
                    selectedAnswer.votes.get(0).count++;
                    selectedAnswer.save();

                    renderJSON(new VoteResponseJSON(true));
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void getResults() {
        long urlID = params.get("id", Long.class).longValue();
        Question question = Question.find("byPollID", urlID).first();

        if (question == null) {
            renderJSON("");
        }

        int[] votes = new int[question.answers.size()];
        for (int i = 0; i < question.answers.size(); i++) {
            //@TODO: implement more voting rounds
            // get count for each answer
            Answer answer = question.answers.get(i);
            if (answer.votes.isEmpty()) {
                votes[i] = 0;
            } else {
                votes[i] = answer.votes.get(0).count;
            }

        }
        ResultsJSON result = new ResultsJSON(urlID, question.id, question.question, question.getAnswersArray(), votes);

        renderJSON(result);
    }
//    void sendSumEmail(Question madeQuestion) {
//        Mail.questionCreated(madeQuestion);
//        render();
//    }
}
