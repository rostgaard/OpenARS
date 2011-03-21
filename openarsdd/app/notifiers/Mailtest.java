package notifiers;

import play.*;
import play.mvc.*;
import java.util.*;

public class Mailtest extends Mailer {

   public static void welcome() {
      setSubject("Welcome %s", "Mr. Cheese");
      addRecipient("krc@retrospekt.dk");
      setFrom("OpenARS mailer daemon <no-reply@mailer.openars.dk>");

      send();
   }
/*
   public static void lostPassword(User user) {
      String newpassword = user.password;
      setFrom("Robot <robot@thecompany.com>");
      setSubject("Your password has been reset");
      addRecipient(user.email);
      send(user, newpassword);
   }
*/
}
