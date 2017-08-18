package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.google.gson.Gson;

import models.Question;

/**
 * DAO class for questions management.
 * 
 * @author Manuel Calzolari
 */
public class Questions {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public Questions(ServletContext context) {
		try {
			Class.forName(context.getInitParameter("jdbc.driver"));
			con = DriverManager.getConnection(context.getInitParameter("jdbc.url"), context.getInitParameter("jdbc.user"), context.getInitParameter("jdbc.password"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Returns data of the question with the given id.
	 */
	public Question query(int id) {
		Question question = new Question();
		try {
			ps = con.prepareStatement("SELECT * FROM questions WHERE id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				question.setId(rs.getInt("id"));
				question.setForm_id(rs.getInt("form_id"));
				question.setQuestion(rs.getString("question"));
				question.setQuestion_type(rs.getString("question_type"));
				question.setOrder_no(rs.getInt("order_no"));
				question.setInstructions(rs.getString("instructions"));
				question.setOptions(rs.getString("options"));
				question.setAnswer_required(rs.getInt("answer_required"));
				question.setDefault_value(rs.getString("default_value"));
				question.setMax_length(rs.getInt("max_length"));
				question.setCdatetime(rs.getString("cdatetime"));
				question.setMdatetime(rs.getString("mdatetime"));
				question.setRows(rs.getInt("rows"));
				question.setSize(rs.getString("size"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
		return question;
	}

	/**
	 * Returns a list of data of questions with the given form id.
	 */
	public List<Question> queryAll(int form_id) {
		List<Question> questions = new ArrayList<Question>();
		try {
			ps = con.prepareStatement("SELECT * FROM questions WHERE form_id=? ORDER BY order_no");
			ps.setInt(1, form_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Question question = new Question();

				question.setId(rs.getInt("id"));
				question.setForm_id(rs.getInt("form_id"));
				question.setQuestion(rs.getString("question"));
				question.setQuestion_type(rs.getString("question_type"));
				question.setOrder_no(rs.getInt("order_no"));
				question.setInstructions(rs.getString("instructions"));
				question.setOptions(rs.getString("options"));
				question.setAnswer_required(rs.getInt("answer_required"));
				question.setDefault_value(rs.getString("default_value"));
				question.setMax_length(rs.getInt("max_length"));
				question.setCdatetime(rs.getString("cdatetime"));
				question.setMdatetime(rs.getString("mdatetime"));
				question.setRows(rs.getInt("rows"));
				question.setSize(rs.getString("size"));

				questions.add(question);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
		return questions;
	}

	/**
	 * Returns a list of data of questions with the given passphrase.
	 */
	public List<Question> queryAll(String passphrase) {
		List<Question> questions = new ArrayList<Question>();
		try {
			ps = con.prepareStatement("SELECT id FROM forms WHERE passphrase=?");
			ps.setString(1, passphrase);
			rs = ps.executeQuery();
			int formId = 0;
			while (rs.next())
				formId = rs.getInt("id");
			ps.clearParameters();

			if (formId != 0) {
				ps = con.prepareStatement("SELECT * FROM questions WHERE form_id=? ORDER BY order_no");
				ps.setInt(1, formId);
				rs = ps.executeQuery();
				while (rs.next()) {
					Question question = new Question();

					question.setId(rs.getInt("id"));
					question.setForm_id(rs.getInt("form_id"));
					question.setQuestion(rs.getString("question"));
					question.setQuestion_type(rs.getString("question_type"));
					question.setOrder_no(rs.getInt("order_no"));
					question.setInstructions(rs.getString("instructions"));
					question.setOptions(rs.getString("options"));
					question.setAnswer_required(rs.getInt("answer_required"));
					question.setDefault_value(rs.getString("default_value"));
					question.setMax_length(rs.getInt("max_length"));
					question.setCdatetime(rs.getString("cdatetime"));
					question.setMdatetime(rs.getString("mdatetime"));
					question.setRows(rs.getInt("rows"));
					question.setSize(rs.getString("size"));

					questions.add(question);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
		return questions;
	}

	/**
	 * Creates a question.
	 */
	public void create(Question q) {
		try {
			ps = con.prepareStatement("INSERT INTO questions VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, 0); // Placeholder
			ps.setInt(2, q.getForm_id());
			ps.setString(3, q.getQuestion());
			ps.setString(4, q.getQuestion_type());
			ps.setInt(5, q.getOrder_no());
			ps.setString(6, q.getInstructions());

			Gson gson = new Gson();
			String json = gson.toJson(q.getOptions());
			ps.setString(7, json);

			ps.setInt(8, q.getAnswer_required());
			ps.setString(9, q.getDefault_value());
			ps.setInt(10, q.getMax_length());
			ps.setString(11, q.getCdatetime());
			ps.setString(12, q.getMdatetime());
			ps.setInt(13, q.getRows());
			ps.setString(14, q.getSize());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Updates the question with given id.
	 */
	public void update(Question q, int id) {
		try {
			ps = con.prepareStatement("UPDATE questions SET question=?, question_type=?, instructions=?, options=?, answer_required=?, default_value=?, max_length=?, mdatetime=?, rows=?, size=? WHERE id=?");
			ps.setString(1, q.getQuestion());
			ps.setString(2, q.getQuestion_type());
			ps.setString(3, q.getInstructions());

			Gson gson = new Gson();
			String json = gson.toJson(q.getOptions());
			ps.setString(4, json);

			ps.setInt(5, q.getAnswer_required());
			ps.setString(6, q.getDefault_value());
			ps.setInt(7, q.getMax_length());
			ps.setString(8, q.getMdatetime());
			ps.setInt(9, q.getRows());
			ps.setString(10, q.getSize());
			ps.setInt(11, id);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Deletes the question with the given id and all its answers. Fixes the questions order.
	 */
	public void delete(int id) {
		try {
			ps = con.prepareStatement("SELECT order_no FROM questions WHERE id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			int order = 0;
			while (rs.next())
				order = rs.getInt("order_no");
			ps.clearParameters();

			ps = con.prepareStatement("DELETE FROM answers WHERE question_id =?");
			ps.setInt(1, id);
			ps.executeUpdate();
			ps.clearParameters();

			ps = con.prepareStatement("DELETE FROM questions WHERE id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
			ps.clearParameters();

			ps = con.prepareStatement("UPDATE questions SET order_no=order_no-1 WHERE order_no>?");
			ps.setInt(1, order);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Returns the highest order number between the questions of a given form id.
	 */
	public int maxOrder_no(int form_id) {
		int max = 0;
		try {
			ps = con.prepareStatement("SELECT MAX(order_no) AS order_no FROM questions WHERE form_id=?");
			ps.setInt(1, form_id);
			rs = ps.executeQuery();
			while (rs.next())
				max = rs.getInt("order_no");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
		return max;
	}

	/**
	 * Increases the position of the question with the given id.
	 */
	public void upOrder_no(int id) {
		try {
			ps = con.prepareStatement("SELECT order_no FROM questions WHERE id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			int order = 0;
			while (rs.next())
				order = rs.getInt("order_no");
			ps.clearParameters();

			if (order != 1) {
				ps = con.prepareStatement("UPDATE questions SET order_no=order_no+1 WHERE order_no=?");
				ps.setInt(1, order - 1);
				ps.executeUpdate();
				ps.clearParameters();

				ps = con.prepareStatement("UPDATE questions SET order_no=order_no-1 WHERE id=?");
				ps.setInt(1, id);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Decreases the position of the question with the given id.
	 */
	public void downOrder_no(int id) {
		try {
			ps = con.prepareStatement("SELECT order_no, form_id FROM questions WHERE id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			int order = 0;
			int formId = 0;
			while (rs.next()) {
				order = rs.getInt("order_no");
				formId = rs.getInt("form_id");
			}
			ps.clearParameters();

			if (order != maxOrder_no(formId)) {
				ps = con.prepareStatement("UPDATE questions SET order_no=order_no-1 WHERE order_no=?");
				ps.setInt(1, order + 1);
				ps.executeUpdate();
				ps.clearParameters();

				ps = con.prepareStatement("UPDATE questions SET order_no=order_no+1 WHERE id=?");
				ps.setInt(1, id);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
	}
}
