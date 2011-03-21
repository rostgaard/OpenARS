package controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.mvc.Controller;
import Utility.RestClient;

public class JoinPoll extends Controller {
	public static void index(String id) throws JSONException {
		if(request.url.contains("joinpoll")) {
			redirect("/" + id);
		}
		JSONObject questionJSON = RestClient.getInstance().getQuestion();
		String pollID = questionJSON.getString("pollID");
		String questionID = questionJSON.getString("questionID");
		String question = questionJSON.getString("question");
		JSONArray answersArray = questionJSON.getJSONArray("answers");
		// String multipleAllowed = questionJSON.getString("multipleAllowed");
		String duration = questionJSON.getString("duration");

		render(id, pollID, questionID, question, answersArray, duration);
	}

	 public static void submit() {

	}
}
