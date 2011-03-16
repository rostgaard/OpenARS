package controllers;

import models.Question;
import play.mvc.*;

public class Json extends Controller {

    public static void json() {
    	renderJSON(new Question());
    }
}