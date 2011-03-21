/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package notifiers;

import play.mvc.*;
import models.*;

/**
 *
 * @author OpenARS Server API team
 */
public class MailNotifier extends Mailer {

    public static void sendAdminLink(Question createdQuestion) {

        long id = 1;
        setSubject("Your admin link");
        addRecipient(createdQuestion.email);
        setFrom("no-reply@mailer.openars.dk");
        if (createdQuestion != null) {
            id = createdQuestion.pollID;
            send(id);
        }
    }
}
