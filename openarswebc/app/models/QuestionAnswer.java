package models;

import play.db.jpa.Model;

public class QuestionAnswer extends Model {
	protected int pollId = -1;
	protected int questionId = -1;
	protected String[] answers;
	protected int responderId = -1;
}
