package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
    	String name = "Droids";
        render(name);
    }
    
    public static void makenewpoll() {
        render();
    }
    
    public static void joinpoll(String id) {
        render(id);
    }
    
    public static void managepoll() {
        render();
    }

}