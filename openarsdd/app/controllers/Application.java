package controllers;

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
        Question question = Question.find("byStudentLink", id).first();

        // if there is no question render blank string
        if (question == null) {
            renderJSON(new String());
        }

        // otherwise render json of Question message
        QuestionJSON testQ = new QuestionJSON(question, -1);
        testQ.setDuration(5);
        renderJSON(testQ);
    }
}
