/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package notifiers;

import play.mvc.*;
import models.*;

/**
 *
 * @author UserX
 */
public class Mail extends Mailer {

    public static void questionCreated(Question createdQuestion) {

        long id = 1;
        setSubject("Poll created in openars");
        addRecipient(createdQuestion.email);
        setFrom("noreply@openars.edu");
        if (createdQuestion != null) {
            id = createdQuestion.pollID;
            send(id);
        }
    }
}
