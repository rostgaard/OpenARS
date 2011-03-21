package controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import jsons.CreateResponseJSON;
import jsons.QuestionJSON;
import jsons.RequestQuestionJSON;
import jsons.VoteJSON;
import models.Answer;
import models.Question;
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

    public static void processRequest(RequestQuestionJSON request) {
        Long id = params.get("id", Long.class);
        Question question = Question.find("byPollID", id).first();

        // if there is no question render blank string
        if (question == null) {
            renderJSON(new String());
        }

//        System.out.println("Poll ID: " + request.getPollID());
//        System.out.println("Responder ID: " + request.getResponderID());

        // otherwise render json of Question message
        QuestionJSON testQ = new QuestionJSON(question, 3249);
        renderJSON(testQ);
    }

    public static void requestQuestion() {
        long urlID = params.get("id", Long.class).longValue();

        // RequestNew message to test:  {"responderID":6547,"pollID":2345}

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
        try {
            String json = reader.readLine();
            Gson gson = new Gson();
            RequestQuestionJSON requestNewMsg = gson.fromJson(json, RequestQuestionJSON.class);

            Question question = Question.find("byPollID", requestNewMsg.getPollID()).first();

            // if there is no question or URL is not corresponding to JSON body, render blank string
            if (question == null
                    || question.pollID != urlID
                    || question.duration <= 0) {
                System.out.println("Question is null: " + (question == null));
                System.out.println("StudentLink != ID: " + (question.pollID != urlID));
                System.out.println("Inactive: " + (question.duration <= 0));
                renderJSON(new String());
            }

            // otherwise render json of Question message
            QuestionJSON testQ = new QuestionJSON(question, requestNewMsg.getResponderID());
            testQ.setDuration(5);
            renderJSON(testQ);

        } catch (IOException ex) {
            ex.printStackTrace();
            renderJSON(new String());
        }

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

//            question.refresh();
            // find modified data
//            System.out.println("question null?: " + (question == null));
//            System.out.println("answers null?: " + question.duration == null);
//            System.out.println("Number of questions: " + question.answers.size());

//            QuestionJSON responseJSON = new QuestionJSON(question);


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


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

     public static void sendSumEmail(Question madeQuestion){
        Mail.questionCreated(madeQuestion);
        render();
    }
}
