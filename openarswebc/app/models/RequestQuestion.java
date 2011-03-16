package models;

import play.db.jpa.Model;

public class RequestQuestion extends Model {
	protected int pollId = -1;
	protected int responderId = -1;
}
