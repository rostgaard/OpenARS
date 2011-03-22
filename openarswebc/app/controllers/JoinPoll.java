package controllers;

import java.text.DecimalFormat;
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
			JSONObject questionJSON = new JSONObject(RestClient.getInstance()
					.getQuestion(id));

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
		String question = null;
		JSONArray answersArray = null;
		String duration = null;

		try {
			JSONObject questionJSON = new JSONObject(RestClient.getInstance()
					.getQuestion(pollID));

			question = questionJSON.getString("question");
			answersArray = questionJSON.getJSONArray("answers");
			duration = questionJSON.getString("duration");
		} catch (Exception e) {
			try {
				// Most like the poll has been inactivated, so we need the
				// results instead
				JSONObject resultJSON = new JSONObject(RestClient.getInstance()
						.getResults(pollID, null));

				question = resultJSON.getString("question");
				answersArray = resultJSON.getJSONArray("answers");
				duration = "0";
			} catch (Exception e2) {
			}
		}
		
		String durationString = "00:00";
		// Parse the duration and turn it into minutes and seconds
		int dur = Integer.parseInt(duration);
		int m = (int) Math.floor(dur / 60);
		int s = dur - m * 60;

		// Add leading zeros and make the string.
		char[] zeros = new char[2];
		Arrays.fill(zeros, '0');
		DecimalFormat df = new DecimalFormat(String.valueOf(zeros));

		durationString = df.format(m) + ":" + df.format(s);

		render(pollID, question, answersArray, duration, durationString);
	}

	public static void nopoll(String pollID) {
		render(pollID);
	}
}