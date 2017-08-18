package models;

import java.util.List;

import com.google.gson.Gson;

/**
 * Model of the answers table.
 * 
 * @author Manuel Calzolari
 */
public class Answer {
	private int id;
	private int question_id;
	private List<String> answer;
	private long answerer_id;
	private String cdatetime; // DATETIME

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getAnswer() {
		Gson gson = new Gson();
		String json = gson.toJson(answer);
		return json;
	}

	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}

	public long getAnswerer_id() {
		return answerer_id;
	}

	public void setAnswerer_id(long answerer_id) {
		this.answerer_id = answerer_id;
	}

	public String getCdatetime() {
		return cdatetime;
	}

	public void setCdatetime(String cdatetime) {
		this.cdatetime = cdatetime;
	}
}
