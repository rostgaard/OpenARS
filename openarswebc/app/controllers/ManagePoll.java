package controllers;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.json.JSONObject;

import play.mvc.Controller;
import Utility.RestClient;

public class ManagePoll extends Controller {
	public static void index(String pollID, String adminkey) {
		validation.required(pollID);
		validation.required(adminkey);
		boolean redirect = true;
		if (validation.hasErrors()) {
			pollID = session.get("pollID");
			adminkey = session.get("adminkey");
			redirect = false;
		}
		
		try {
			boolean correct = RestClient.getInstance().checkAdminkey(pollID,
					adminkey);
			if (correct) {
				if (redirect) {
					session.put("pollID", pollID);
					session.put("adminkey", adminkey);
					ManagePoll.index(null, null);
				} else {
					render(pollID, adminkey);
				}
			}
		} catch (Exception e) {
		}
		Application.index();
	}

	public static void activate() {
		String pollID = session.get("pollID");
		String adminkey = session.get("adminkey");

		String duration = "";
		String durationString = "00:00";
		// Get the duration from the server
		try {
			String res = RestClient.getInstance().getQuestion(pollID);
			JSONObject questionJSON = new JSONObject(res);

			duration = questionJSON.getString("duration");

			// Parse the duration and turn it into minutes and seconds
			int dur = Integer.parseInt(duration);
			int m = (int) Math.floor(dur / 60);
			int s = dur - m * 60;

			// Add leading zeros and make the string.
			char[] zeros = new char[2];
			Arrays.fill(zeros, '0');
			DecimalFormat df = new DecimalFormat(String.valueOf(zeros));

			durationString = df.format(m) + ":" + df.format(s);
		} catch (Exception e) {
			e.printStackTrace();
		}

		render(pollID, adminkey, duration, durationString);
	}

	public static void activateSubmit(String minutes, String seconds) {
		String pollID = session.get("pollID");
		String adminkey = session.get("adminkey");
		
		int s = 0;
		int m = 0;

		validation.required(minutes);
		validation.required(seconds);
		if (!validation.hasErrors()) {
			try {
				s = Integer.parseInt(seconds);
				if (s < 0) {
					s = 0;
				}
			} catch (Exception e) {
			}

			try {
				m = Integer.parseInt(minutes);
				if (m < 0) {
					m = 0;
				}
			} catch (Exception e) {
			}

			int duration = s + m * 60;

			try {
				String res = RestClient.getInstance().activate(pollID,
						adminkey, duration);
			} catch (Exception e) {
			}
		}

		activate();
	}

	public static void edit() {
		String pollID = session.get("pollID");
		String adminkey = session.get("adminkey");
		render(pollID, adminkey);
	}

	public static void statistics() {
		String pollID = session.get("pollID");
		String adminkey = session.get("adminkey");
		render(pollID, adminkey);
	}
}
