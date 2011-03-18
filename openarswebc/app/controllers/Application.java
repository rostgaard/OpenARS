package controllers;

import org.json.JSONArray;
import org.json.JSONObject;

import play.mvc.Controller;
import Utility.RestClient;

public class Application extends Controller {

    public static void index() {
    	String name = "Droids";
        render(name);
    }
    
    public static void makenewpoll() {
        render();
    }
    
    public static void joinpoll(String id) {
    	JSONObject questionJSON = RestClient.getInstance().getQuestion();
    	try {
        //Log.i("openarsActivity.java", "question JSON.toString()" + questionJSON.toString());
        
        	String pollID = questionJSON.getString("pollId");
			String questionID = questionJSON.getString("questionId");
			String question = questionJSON.getString("question");
			JSONArray answersArray = questionJSON.getJSONArray("answers");

			String multipleAllowed = questionJSON.getString("multipleAllowed");
			
			render(pollID, questionID, question, answersArray, multipleAllowed);
    	}
    	catch(Exception e) {
    		
    	}
	}
    
    public static void managepoll() {
        render();
    }
}