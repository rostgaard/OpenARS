package models;

import play.db.jpa.Model;

public class QuestionResult extends Model {
	protected int pollId = 12;
	protected int questionId = 1214;
	protected int votes = 103;
	protected String question = "When cheese gets its picture taken, what does it say?";
	protected String[] answers = {"Orangeeee", "Humaaaaan", "Meeee"};
	protected int[] results = {75, 80, 100};
}
