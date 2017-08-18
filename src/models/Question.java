package models;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Model of the questions table.
 * 
 * @author Manuel Calzolari
 */
public class Question {
	private int id;
	private int form_id;
	private String question;
	private String question_type;
	private int order_no;
	private String instructions;
	private List<String> options;
	private int answer_required = 0;
	private String default_value;
	private int max_length;
	private String cdatetime; // DATETIME
	private String mdatetime; // DATETIME
	private int rows;
	private String size;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getForm_id() {
		return form_id;
	}

	public void setForm_id(int form_id) {
		this.form_id = form_id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}

	public int getOrder_no() {
		return order_no;
	}

	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(String options) {
		Gson gson = new Gson();
		Type listType = new TypeToken<List<String>>(){}.getType();
		List<String> list = gson.fromJson(options, listType);
		this.options = list;
	}

	public int getAnswer_required() {
		return answer_required;
	}

	public void setAnswer_required(int answer_required) {
		this.answer_required = answer_required;
	}

	public String getDefault_value() {
		return default_value;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}

	public int getMax_length() {
		return max_length;
	}

	public void setMax_length(int max_length) {
		this.max_length = max_length;
	}

	public String getCdatetime() {
		return cdatetime;
	}

	public void setCdatetime(String cdatetime) {
		this.cdatetime = cdatetime;
	}

	public String getMdatetime() {
		return mdatetime;
	}

	public void setMdatetime(String mdatetime) {
		this.mdatetime = mdatetime;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
