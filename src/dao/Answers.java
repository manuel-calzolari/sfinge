package dao;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import components.Bit;

import models.Answer;
import models.AssociationRule;
import models.Itemset;
import models.ItemsetCollection;
import models.Question;

/**
 * DAO class for answers management.
 * 
 * @author Manuel Calzolari
 */
public class Answers {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public Answers(ServletContext context) {
		try {
			Class.forName(context.getInitParameter("jdbc.driver"));
			con = DriverManager.getConnection(context.getInitParameter("jdbc.url"), context.getInitParameter("jdbc.user"), context.getInitParameter("jdbc.password"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Returns a list of data of answers with the given form id.
	 */
	public Map<Question, List<Answer>> queryAll(int form_id) {
		ResultSet rs2 = null;
		Map<Question, List<Answer>> answers_map = new LinkedHashMap<Question, List<Answer>>();
		try {
			ps = con.prepareStatement("SELECT * FROM questions WHERE form_id=? ORDER BY order_no");
			ps.setInt(1, form_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Question question = new Question();
				ArrayList<Answer> answers = new ArrayList<Answer>();

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
				
				ps = con.prepareStatement("SELECT * FROM answers WHERE question_id=?");
				ps.setInt(1, rs.getInt("id"));
				rs2 = ps.executeQuery();
				while (rs2.next()) {
					Answer answer = new Answer();
					answer.setId(rs2.getInt("id"));
					answer.setQuestion_id(rs2.getInt("question_id"));
					
					Gson gson = new Gson();
					Type listType = new TypeToken<ArrayList<String>>(){}.getType();
					List<String> list = gson.fromJson(rs2.getString("answer"), listType);
					answer.setAnswer(list);
					
					answer.setAnswerer_id(rs2.getLong("answerer_id"));

					// Strips milliseconds
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					Date cdatetime = sdf.parse(rs2.getString("cdatetime"));
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					answer.setCdatetime(sdf.format(cdatetime));

					answers.add(answer);
				}
				answers_map.put(question, answers);
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
		return answers_map;
	}
	
	/**
	 * Returns a list of data of answers with the given form id and filters.
	 */
	public Map<Question, List<Answer>> queryAll(int form_id, List<String> filters_id) {
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		Map<Question, List<Answer>> answers_map = new LinkedHashMap<Question, List<Answer>>();
		try {
			ps = con.prepareStatement("SELECT * FROM questions WHERE form_id=? ORDER BY order_no");
			ps.setInt(1, form_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Question question = new Question();
				ArrayList<Answer> answers = new ArrayList<Answer>();

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
				
				ps = con.prepareStatement("SELECT * FROM answers WHERE answer=(SELECT answer FROM answers WHERE id IN (?))"); // GROUP BY answer
				//ps.setInt(1, rs.getInt("id"));
				// Converting List to comma-separated
				ps.setString(1, filters_id.toString().replace("[", "").replace("]", "").replace(", ", ","));
				rs2 = ps.executeQuery();
				while (rs2.next()) {
					ps = con.prepareStatement("SELECT * FROM answers WHERE question_id=? AND answerer_id=? AND cdatetime=?");
					ps.setInt(1, rs.getInt("id"));
					ps.setLong(2, rs2.getLong("answerer_id"));
					ps.setString(3, rs2.getString("cdatetime"));
					rs3 = ps.executeQuery();
					while (rs3.next()) {
						Answer answer = new Answer();
						answer.setId(rs3.getInt("id"));
						answer.setQuestion_id(rs3.getInt("question_id"));

						Gson gson = new Gson();
						Type listType = new TypeToken<ArrayList<String>>() {}.getType();
						List<String> list = gson.fromJson(rs3.getString("answer"), listType);
						answer.setAnswer(list);

						answer.setAnswerer_id(rs3.getLong("answerer_id"));

						// Strips milliseconds
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						Date cdatetime = sdf.parse(rs3.getString("cdatetime"));
						sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						answer.setCdatetime(sdf.format(cdatetime));

						answers.add(answer);
					}
				}
				answers_map.put(question, answers);
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
		return answers_map;
	}

	/**
	 * Creates an answer.
	 */
	public void create(Answer a) {
		try {
			ps = con.prepareStatement("INSERT INTO answers VALUES (?, ?, ?, ?, ?)");
			ps.setInt(1, 0); // Placeholder
			ps.setInt(2, a.getQuestion_id());
			ps.setString(3, a.getAnswer());
			ps.setLong(4, a.getAnswerer_id());
			ps.setString(5, a.getCdatetime());
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
	 * Deletes the answer with the given id.
	 */
	public void delete(int id) {
		try {
			ps = con.prepareStatement("DELETE FROM answers WHERE id=?");
			ps.setInt(1, id);
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
	 * Use to check if the answer belongs to a required question.
	 */
	public Boolean isRequired(int question_id) {
		Boolean result = false;
		try {
			ps = con.prepareStatement("SELECT answer_required FROM questions WHERE id=?");
			ps.setInt(1, question_id);
			rs = ps.executeQuery();
			while (rs.next())
				if (rs.getInt("answer_required") == 1)
					result = true;
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
		return result;
	}

	/**
	 * Returns a map with the answers frequency with a given question id.
	 */
	public Map<String, Integer> frequency(int question_id) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			ps = con.prepareStatement("SELECT answer, COUNT(*) as total FROM answers WHERE question_id=? GROUP BY answer ORDER BY total DESC");
			ps.setInt(1, question_id);
			rs = ps.executeQuery();
			while (rs.next())
				result.put(rs.getString("answer"), rs.getInt("total"));
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
		return result;
	}

	/**
	 * Returns the average of the answers with a given question id.
	 */
	public double average(int question_id) {
		double result = 0;
		try {
			int count = 0;
			ps = con.prepareStatement("SELECT answer FROM answers WHERE question_id=?");
			ps.setInt(1, question_id);
			rs = ps.executeQuery();
			while (rs.next())
				try {
					Gson gson = new Gson();
					Type listType = new TypeToken<ArrayList<String>>(){}.getType();
					List<String> list = gson.fromJson(rs.getString("answer"), listType);

					Iterator<String> iterator = list.iterator();
					while (iterator.hasNext()) {
						result += Double.parseDouble(iterator.next());
						count++;
					}
				} catch (NumberFormatException nfe) {
				}
			result /= count;
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
		return result;
	}

	/**
	 * Returns the variance of the answers with a given question id.
	 */
	public double variance(int question_id, double average) {
		double result = 0;
		try {
			int count = 0;
			ps = con.prepareStatement("SELECT answer FROM answers WHERE question_id=?");
			ps.setInt(1, question_id);
			rs = ps.executeQuery();
			while (rs.next())
				try {
					Gson gson = new Gson();
					Type listType = new TypeToken<ArrayList<String>>(){}.getType();
					List<String> list = gson.fromJson(rs.getString("answer"), listType);

					Iterator<String> iterator = list.iterator();
					while (iterator.hasNext()) {
						result += Math.pow(Double.parseDouble(iterator.next()) - average, 2);
						count++;
					}
				} catch (NumberFormatException nfe) {
				}
			result /= count;
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
		return result;
	}
	
	/**
	 * Returns the support (by answerer) of the answers with a given x itemset and y itemset.
	 */
	public double support(List<Integer> x, List<Integer> y) { // TODO: Considerare form?
		double result = 0;
		try {
			int count = 0;
			Map<Long, List<Integer>> map = new HashMap<Long, List<Integer>>();
			ps = con.prepareStatement("SELECT id, answerer_id FROM answers");
			rs = ps.executeQuery();
			while (rs.next())
				if (map.containsKey(rs.getLong("answerer_id"))) {
					map.get(rs.getLong("answerer_id")).add(rs.getInt("id"));
				} else {
					List<Integer> list = new LinkedList<Integer>();
					list.add(rs.getInt("id"));
					map.put(rs.getLong("answerer_id"), list);
				}
			for (Map.Entry<Long, List<Integer>> entry : map.entrySet()) {
			    if (entry.getValue().containsAll(x) && entry.getValue().containsAll(y))
			    	count++;
			}
			result = count / map.size();
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
		return result;
	}
	
	/**
	 * Returns the confidence (by answerer) of the answers with a given x itemset and y itemset.
	 */
	public double confidence(List<Integer> x, List<Integer> y) { // TODO: Considerare form?
		double result = 0;
		try {
			int count_x = 0;
			int count = 0;
			Map<Long, List<Integer>> map = new HashMap<Long, List<Integer>>();
			ps = con.prepareStatement("SELECT id, answerer_id FROM answers");
			rs = ps.executeQuery();
			while (rs.next())
				if (map.containsKey(rs.getLong("answerer_id"))) {
					map.get(rs.getLong("answerer_id")).add(rs.getInt("id"));
				} else {
					List<Integer> list = new LinkedList<Integer>();
					list.add(rs.getInt("id"));
					map.put(rs.getLong("answerer_id"), list);
				}
			for (Map.Entry<Long, List<Integer>> entry : map.entrySet()) {
			    if (entry.getValue().containsAll(x)) {
			    	count_x++;
			    	if (entry.getValue().containsAll(y))
			    		count++;
			    }
			}
			result = count / count_x;
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
		return result;
	}
	
	/**
	 * 
	 */
	public ItemsetCollection frequent(Integer min_sup) {
		ItemsetCollection db =  new ItemsetCollection();
		ItemsetCollection L =  new ItemsetCollection(); // resultant large itemsets
		ItemsetCollection Li =  new ItemsetCollection(); // large itemset in each iteration
		ItemsetCollection Ci =  new ItemsetCollection(); // candidate itemset in each iteration
		try {
			ps = con.prepareStatement("SELECT * FROM answers ORDER BY answerer_id"); // TODO: form = answerer+cdatetime?
			rs = ps.executeQuery();
			
			long prev_ai = 0;
			Itemset temp = new Itemset();
			while (rs.next()) {
				if ((rs.getLong("answerer_id") == prev_ai) || (prev_ai == 0)) {
					temp.add(rs.getString("question_id") + rs.getString("answer"));
					prev_ai = rs.getLong("answerer_id");
				} else {
					db.add(temp);
					temp = new Itemset();
					temp.add(rs.getString("question_id") + rs.getString("answer"));
					prev_ai = rs.getLong("answerer_id");			
				}
			}
			db.add(temp); // add the last itemset
			
			Itemset I = db.GetUniqueItems();
			
			// first iteration (1-item itemsets)
			for (String item : I) {
				temp = new Itemset();
				temp.add(item);
				Ci.add(temp);
			}
			
		    // next iterations
		    int k = 2;
		    while (Ci.size() != 0)
		    {
		        // set Li from Ci (pruning)
		        Li.clear();
		        for (Itemset itemset : Ci)
		        {
		            itemset.setSupport(db.FindSupport(itemset));
		            if (itemset.getSupport() >= min_sup)
		            {
		                Li.add(itemset);
		                L.add(itemset);
		            }
		        }

		        // set Ci for next iteration (find supersets of Li)
		        Ci.clear();
		        Ci.addAll(Bit.FindSubsets(Li.GetUniqueItems(), k)); // get k-item subsets
		        k += 1;
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
		return L;
	}
	
	public List<AssociationRule> mine(ItemsetCollection L, double confidenceThreshold)
	{
		ItemsetCollection db =  new ItemsetCollection();
		List<AssociationRule> allRules = new ArrayList<AssociationRule>();
		try {
			ps = con.prepareStatement("SELECT * FROM answers ORDER BY answerer_id"); // TODO: form = answerer+cdatetime?
			rs = ps.executeQuery();
			
			long prev_ai = 0;
			Itemset temp = new Itemset();
			Itemset temp2 = new Itemset();
			while (rs.next()) {
				if ((rs.getLong("answerer_id") == prev_ai) || (prev_ai == 0)) {
					temp.add(rs.getString("question_id") + rs.getString("answer"));
					prev_ai = rs.getLong("answerer_id");
				} else {
					db.add(temp);
					temp = new Itemset();
					temp.add(rs.getString("question_id") + rs.getString("answer"));
					prev_ai = rs.getLong("answerer_id");			
				}
			}
			db.add(temp); // add the last itemset
			
		    for (Itemset itemset : L)
		    {
		        ItemsetCollection subsets = Bit.FindSubsets(itemset, 0); // get all subsets
		        for (Itemset subset : subsets)
		        {
		            double confidence = (db.FindSupport(itemset) / db.FindSupport(subset)) * 100.0;
		            if (confidence >= confidenceThreshold)
		            {
		                AssociationRule rule = new AssociationRule();
		                temp = (Itemset)rule.getX().clone();
		                temp.addAll(subset);
		                rule.setX(temp);
		                temp = (Itemset)rule.getY().clone();
		                temp2 = (Itemset)itemset.clone();
		                temp2.removeAll(subset);
		                temp.addAll(temp2);
		                rule.setY(temp);
		                rule.setSupport(db.FindSupport(itemset));
		                rule.setConfidence(confidence);
		                if (rule.getX().size() > 0 && rule.getY().size() > 0)
		                    allRules.add(rule);
		            }
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
		return allRules;
	}
}
