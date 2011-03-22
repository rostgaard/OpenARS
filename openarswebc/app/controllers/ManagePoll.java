package controllers;

import Utility.RestClient;
import play.mvc.Controller;

public class ManagePoll extends Controller {
	public static void index(String pollID, String adminkey) {
		render(pollID, adminkey);
	}

	public static void activate(String pollID, String adminkey, String minutes,
			String seconds) {
		int s = 0;
		int m = 0;
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
			RestClient.getInstance().activate(pollID, adminkey, duration);
		} catch (Exception e) {
		}

		render(pollID, duration);
	}

	public static void edit(String pollID, String adminkey) {
		render(pollID, adminkey);
	}

	public static void statistics(String pollID, String adminkey) {
		render(pollID, adminkey);
	}
}
