package controllers;

import play.mvc.Controller;

public class ManagePoll extends Controller {
	public static void index(String pollID, String adminkey) {
		System.out.println(pollID);
		System.out.println(adminkey);
		render(pollID, adminkey);
	}
	
	public static void activate(String pollID, String adminkey, String minutes, String seconds) {
		render();
	}
	
	public static void edit(String pollID, String adminkey) {
		render(pollID, adminkey);
	}
	
	public static void statistics(String pollID, String adminkey) {
		render(pollID, adminkey);
	}
}
