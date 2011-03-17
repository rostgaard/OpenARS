package controllers;

import models.Answer;
import models.Question;
import play.mvc.*;


//import models.*;
public class Application extends Controller {

    public static void index() {
        Question q = new Question("1234", "2345", "What is the capital of Spain?", false);
        Answer a1 = new Answer(q, "Madrid", true);
        Answer a2 = new Answer(q, "Oslo", false);
        
        q.save();
        a1.save();
        a2.save();

        Question question = Question.find("byStudentLink", "2345").first();
        
        render(question);
    }

    public static void sayHello(String myName) {
        render(myName);
    }
}
