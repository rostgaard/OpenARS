/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;
import models.Question;
import play.mvc.*;
import notifiers.*;
/**
 *
 * @author UserX
 */
public class Testing extends Controller {

    public static void sendSumEmail(){
        Question madeQuestion = new Question(2525L, "Who is your daddy and what does he do?", false,"juha.hakala@metropolia.fi");
        Mail.questionCreated(madeQuestion);
        render();
    }

}
