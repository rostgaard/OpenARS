package controllers;

import play.i18n.Lang;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void changelang(String lang) {
    	Lang.change(lang);
    }
}

