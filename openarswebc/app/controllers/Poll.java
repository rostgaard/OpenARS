package controllers;

import org.json.JSONObject;

import Utility.RestClient;
import play.mvc.Controller;

public class Poll extends Controller {
	public static void getResults(String id, String adminkey) {
		String res = RestClient.getInstance().getResults(id, adminkey);
		System.out.println(res);
		if (res != null && !res.isEmpty())
			renderJSON(res);
		renderJSON(false);
	}
}
