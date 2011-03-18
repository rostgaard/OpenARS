package controllers;

import models.QuestionResult;
import play.mvc.Controller;

public class Json extends Controller {

    public static void json() {
    	renderJSON(new QuestionResult());
    }
}