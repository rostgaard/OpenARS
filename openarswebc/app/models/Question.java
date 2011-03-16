package models;

import play.db.jpa.Model;

public class Question extends Model {
	protected int pollId = 12;
	protected int questionId = 1214;
	protected String question = "When cheese gets its picture taken, what does it say?";
	protected String[] answers = {"Orangeeee", "Humaaaaan", "Meeee"};
	protected boolean multipleAllowed = false;
}
