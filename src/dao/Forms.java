package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import models.Form;

/**
 * DAO class for forms management.
 * 
 * @author Manuel Calzolari
 */
public class Forms {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public Forms(ServletContext context) {
		try {
			Class.forName(context.getInitParameter("jdbc.driver"));
			con = DriverManager.getConnection(context.getInitParameter("jdbc.url"), context.getInitParameter("jdbc.user"), context.getInitParameter("jdbc.password"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Returns data of the form with the given id.
	 */
	public Form query(int id) {
		Form form = new Form();
		try {
			ps = con.prepareStatement("SELECT * FROM forms WHERE id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				form.setId(rs.getInt("id"));
				form.setTitle(rs.getString("title"));
				form.setDesc(rs.getString("desc"));

				// Strips milliseconds
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date cdatetime = sdf.parse(rs.getString("cdatetime"));
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				form.setCdatetime(sdf.format(cdatetime));

				// Strips milliseconds
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date mdatetime = sdf.parse(rs.getString("mdatetime"));
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				form.setMdatetime(sdf.format(mdatetime));

				form.setCreator_id(rs.getInt("creator_id"));
				form.setPassphrase(rs.getString("passphrase"));
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
		return form;
	}
	
	/**
	 * Returns data of the form with the given passphrase.
	 */
	public Form query(String p) {
		Form form = new Form();
		try {
			ps = con.prepareStatement("SELECT * FROM forms WHERE passphrase=?");
			ps.setString(1, p);
			rs = ps.executeQuery();
			while (rs.next()) {
				form.setId(rs.getInt("id"));
				form.setTitle(rs.getString("title"));
				form.setDesc(rs.getString("desc"));

				// Strips milliseconds
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date cdatetime = sdf.parse(rs.getString("cdatetime"));
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				form.setCdatetime(sdf.format(cdatetime));

				// Strips milliseconds
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date mdatetime = sdf.parse(rs.getString("mdatetime"));
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				form.setMdatetime(sdf.format(mdatetime));

				form.setCreator_id(rs.getInt("creator_id"));
				form.setPassphrase(rs.getString("passphrase"));
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
		return form;
	}

	/**
	 * Returns a list of data of all forms.
	 */
	public List<Form> queryAll() {
		ArrayList<Form> forms = new ArrayList<Form>();
		try {
			ps = con.prepareStatement("SELECT * FROM forms");
			rs = ps.executeQuery();
			while (rs.next()) {
				Form form = new Form();
				form.setId(rs.getInt("id"));
				form.setTitle(rs.getString("title"));
				form.setDesc(rs.getString("desc"));

				// Strips milliseconds
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date cdatetime = sdf.parse(rs.getString("cdatetime"));
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				form.setCdatetime(sdf.format(cdatetime));

				// Strips milliseconds
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date mdatetime = sdf.parse(rs.getString("mdatetime"));
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				form.setMdatetime(sdf.format(mdatetime));

				form.setCreator_id(rs.getInt("creator_id"));
				form.setPassphrase(rs.getString("passphrase"));
				forms.add(form);
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
		return forms;
	}

	/**
	 * Create a form.
	 */
	public void create(Form f) {
		try {
			ps = con.prepareStatement("INSERT INTO forms VALUES (?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, 0); // Placeholder
			ps.setString(2, f.getTitle());
			ps.setString(3, f.getDesc());
			ps.setString(4, f.getCdatetime());
			ps.setString(5, f.getMdatetime());
			ps.setInt(6, f.getCreator_id());
			ps.setString(7, f.getPassphrase());
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
	 * Updates the form with the given id.
	 */
	public void update(Form f, int id) {
		try {
			ps = con.prepareStatement("UPDATE forms SET title=?, `desc`=?, mdatetime=?, creator_id=?, passphrase=? WHERE id=?");
			ps.setString(1, f.getTitle());
			ps.setString(2, f.getDesc());
			ps.setString(3, f.getMdatetime());
			ps.setInt(4, f.getCreator_id());
			ps.setString(5, f.getPassphrase());
			ps.setInt(6, id);
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
	 * Deletes the form with the given id and all its questions and answers.
	 */
	public void delete(int id) {
		try {
			ps = con.prepareStatement("DELETE FROM forms WHERE id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
			ps.clearParameters();

			ps = con.prepareStatement("SELECT id FROM questions WHERE form_id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			ps.clearParameters();
			while (rs.next()) {
				ps = con.prepareStatement("DELETE FROM answers WHERE question_id =?");
				ps.setInt(1, rs.getInt("id"));
				ps.executeUpdate();
				ps.clearParameters();
			}

			ps = con.prepareStatement("DELETE FROM questions WHERE form_id =?");
			ps.setInt(1, id);
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
	 * Checks if a passphrase already exists other than for the given id.
	 */
	public Boolean passphraseExists(int id, String passphrase) {
		try {
			ps = con.prepareStatement("SELECT * FROM forms WHERE id!=? AND passphrase=?");
			ps.setInt(1, id);
			ps.setString(2, passphrase);
			rs = ps.executeQuery();
			while (rs.next())
				return true;
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
		return false;
	}
}
