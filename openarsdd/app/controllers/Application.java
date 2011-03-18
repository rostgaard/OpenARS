package controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import jsons.QuestionJSON;
import jsons.RequestQuestionJSON;
import models.Answer;
import models.Question;
import play.mvc.*;

//import models.*;
public class Application extends Controller {

    public static void index() {
//        Question q = new Question("1234", "2345", "What is the capital of Spain?", false);
//        Answer a1 = new Answer(q, "Madrid", true);
//        Answer a2 = new Answer(q, "Oslo", false);

//        q.save();
//        a1.save();
//        a2.save();

        Question question = Question.find("byStudentLink", "2345").first();
        QuestionJSON testQ = new QuestionJSON(question, 9254);
        render(question, testQ);
    }

    public static void sayHello(String myName) {
        render(myName);
    }

    public static void processRequest(RequestQuestionJSON request) {
        Long id = params.get("id", Long.class);
        Question question = Question.find("byStudentLink", id).first();

        // if there is no question render blank string
        if (question == null) {
            renderJSON(new String());
        }

//        System.out.println("Poll ID: " + request.getPollID());
//        System.out.println("Responder ID: " + request.getResponderID());

        // otherwise render json of Question message
        QuestionJSON testQ = new QuestionJSON(question, 3248);
        renderJSON(testQ);
    }

    public static void test() {
        Long urlID = params.get("id", Long.class);

        // RequestNew message to test:  {"responderID":6547,"pollID":2345}

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
        try {
            String json = reader.readLine();
            Gson gson = new Gson();
            RequestQuestionJSON requestNewMsg = gson.fromJson(json, RequestQuestionJSON.class);

            Question question = Question.find("byStudentLink", requestNewMsg.getPollID()).first();

            // if there is no question or URL is not corresponding to JSON body, render blank string
            if (question == null
                    || question.studentLink != urlID
                    || question.duration <= 0) {
                
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

            // generate data
            long pollID = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
            String password = Question.getRandomString(8);

            // put generated data into model
            question.studentLink = pollID;
            question.password = password;

            question.save();
            for (Answer answer : questionMsg.getAnswersList(question)) {
                answer.save();
            }

            question = Question.find("byStudentLink", question.studentLink).first();

            renderJSON(question);


        } catch (IOException ex) {
            ex.printStackTrace();
            renderJSON(new String());
        }

    }
}
