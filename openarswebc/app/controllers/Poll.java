package controllers;

import org.json.JSONObject;

import Utility.RestClient;
import play.mvc.Controller;

public class Poll extends Controller {
	public static void getResults(String id) {
		try {
			JSONObject res = RestClient.getInstance().getResults(id);
			res.get("pollID");
			renderJSON(res);
		} catch (Exception e) {
		}
		renderJSON(false);
	}
}
