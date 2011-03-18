package controllers;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jsons.QuestionJSON;
import jsons.RequestNewJSON;
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

    public static void processRequest(RequestNewJSON request) {
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
        Long id = params.get("id", Long.class);

        // RequestNew message to test:  {"responderID":6547,"pollID":2345}

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.body));
        try {
            String json = reader.readLine();
            Gson gson = new Gson();
            RequestNewJSON requestNewMsg = gson.fromJson(json, RequestNewJSON.class);

            Question question = Question.find("byStudentLink", requestNewMsg.getPollID()).first();

            // if there is no question or URL is not corresponding to JSON body, render blank string
            if (question == null || question.studentLink != id) {
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
}
