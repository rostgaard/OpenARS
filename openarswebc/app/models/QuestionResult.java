package models;

import play.db.jpa.Model;

public class QuestionResult extends Model {
	protected int pollId = -1;
	protected int questionId = -1;
	protected String question = null;
	protected String[] answers;
	protected Integer[] results;
}
