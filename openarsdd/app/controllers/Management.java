/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import jsons.ActivationJSON;
import jsons.CreateResponseJSON;
import jsons.QuestionJSON;
import models.Answer;
import models.Question;
import notifiers.MailNotifier;
import play.mvc.Controller;

/**
 * Controller which takes care of functions that poll administrator uses
 * @author OpenARS Server API team
 */
public class Management extends Controller {

    private static Gson gson = new Gson();

    /**
     * Method for creating a question
     * URL: <server>/newPoll
     * Method: POST
     * Request body: QuestionJSON with these fields set: question, answers, multipleAllowed, email
     */
    public static void createQuestion() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
        try {
            String json = reader.readLine();
            QuestionJSON questionMsg = gson.fromJson(json, QuestionJSON.class);

            Question question = questionMsg.makeModelFromJSON();

            // generate data and save question, try until we have unique poll ID
            do {
                question.pollID = new Random(System.currentTimeMillis()).nextInt(999999);
            } while (!Question.find("byPollID", question.pollID).fetch().isEmpty());

            question.generateAdminKey(8);
            question.save();

            // send mail to the creator of question 
            MailNotifier.sendPollIDLink(question);
            MailNotifier.sendAdminLink(question);

            // retrieve answers from JSON and save them into database
            for (String a : questionMsg.getAnswers()) {
                new Answer(question, a).save();
            }

            renderJSON(new CreateResponseJSON(question.pollID, question.adminKey));

        } catch (IOException ex) {
            ex.printStackTrace();
            renderJSON(new String());
        }

    }

    /**
     * Method for activating the question. It responds with "activated" when
     * adminKey is correct, "not activated" when it is not correct or 
     * "The question does not exist!" if the poll does not exist. <br/>
     * URL: <server>/activation/{id}/{adminKey} <br/>
     * Method: POST <br/>
     * Parameter {id} - poll ID <br/>
     * Parameter {adminKey} - randomly generated string at question creation <br/>
     * Request body: ActivationJSON with duration set
     */
    public static void activation() {
        long urlID = params.get("id", Long.class).longValue();
        String adminKey = params.get("adminKey");

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
        try {
            // deserialize json from request body
            String json = reader.readLine();
            ActivationJSON activationMsg = gson.fromJson(json, ActivationJSON.class);

            // retrieve and activate the question
            Question question = Question.find("byPollID", urlID).first();

            if (question == null) {
                renderJSON("The question does not exist!");
            }

            // only when provided adminKey is correct
            if (question.adminKey.equals(adminKey)) {
                question.activateFor(activationMsg.getDuration());
                question.save();
                renderJSON("activated");
            } else {
                renderJSON("not activated");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            renderJSON(new String());
        }
    }

    /**
     * Method used to check if admin link for the question is correct.
     * It responds with true if it is correct or false otherwise. <br/>
     * URL: <server>/checkAdminKey/{id}/{adminKey} <br/>
     * Method: GET
     * Parameter {id} - poll ID <br/>
     * Parameter {adminKey} - randomly generated string at question creation <br/>
     */
    public static void checkAdminLink() {
        long urlID = params.get("id", Long.class).longValue();
        String adminKey = params.get("adminKey");

        // retrieve and activate the question
        Question question = Question.find("byPollID", urlID).first();

        if (question == null) {
            renderJSON(false);
        }

        if (adminKey != null && question.adminKey.equals(adminKey)) {
            renderJSON(true);
        } else {
            renderJSON(false);
        }
    }
}
