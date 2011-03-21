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
 * @author OpenARS Server API team
 */
public class Test extends Controller {

    public static void createPoll(){
        Question madeQuestion = new Question(2525, "Test Question?", false,"krc@retrospekt.dk");

        MailNotifier.sendAdminLink(madeQuestion);
        render();
    }

}
