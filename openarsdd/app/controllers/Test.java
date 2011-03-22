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
        // Only for testing
        Question testQuestion = new Question(2525, "Test Question?", false,"krc@retrospekt.dk");
        testQuestion.adminKey = "123jhd";
        testQuestion.pollID = 665698;   

        System.out.println();

        MailNotifier.sendPollIDLink(testQuestion);
        MailNotifier.sendAdminLink(testQuestion);
        render();
    }

    public static void index() {
        renderJSON("no such page");
    }

}
