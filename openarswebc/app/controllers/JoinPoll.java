package controllers;

import java.util.Arrays;

import models.Vote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.mvc.Controller;
import Utility.RestClient;

public class JoinPoll extends Controller {
	public static void index(String id) throws JSONException {
		if (request.url.contains("joinpoll")) {
			redirect("/" + id);
		}
		try {
			JSONObject questionJSON = RestClient.getInstance().getQuestion(id);

			String pollID = questionJSON.getString("pollID");

			String questionID = questionJSON.getString("questionID");
			String question = questionJSON.getString("question");
			JSONArray answersArray = questionJSON.getJSONArray("answers");
			// String multipleAllowed =
			// questionJSON.getString("multipleAllowed");
			String duration = questionJSON.getString("duration");

			render(id, pollID, questionID, question, answersArray, duration);
		} catch (JSONException e) {
			nopoll(id);
		}
	}

	public static void submit(String pollID, String questionID, String answer)
			throws JSONException {

		validation.required(pollID);
		validation.match(pollID, "^\\d+$");
		validation.required(questionID);
		validation.match(questionID, "^\\d+$");
		validation.required(answer);

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			index(pollID);
			return;
		}

		Vote v = new Vote();
		v.pollID = Integer.parseInt(pollID);
		v.questionID = Integer.parseInt(questionID);
		v.answers = new String[] { answer };
		v.rensponderID = request.remoteAddress + session.getId();

		RestClient.getInstance().vote(v);

		success(pollID);
	}

	public static void success(String pollID) {
		try {
			JSONObject questionJSON = RestClient.getInstance().getQuestion(
					pollID);

			String question = questionJSON.getString("question");
			JSONArray answersArray = questionJSON.getJSONArray("answers");

			render(pollID, question, answersArray);
		} catch (Exception e) {
		}
	}

	public static void nopoll(String pollID) {
		render(pollID);
	}
}