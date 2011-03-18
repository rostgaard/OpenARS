package models;

import play.db.jpa.Model;

public class Poll extends Model {
	public int pollId;
	public String question;
	public String[] answers;
	public boolean multipleAllowed;
	public String email;
}
