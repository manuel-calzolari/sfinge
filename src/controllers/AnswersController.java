package controllers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Answer;
import models.AssociationRule;
import models.ItemsetCollection;
import models.Question;
import models.User;
import dao.Answers;
import dao.Users;

/**
 * Servlet implementation class AnswersControllers
 */
@WebServlet("/Answers")
public class AnswersController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ServletConfig config;
	private ServletContext context;

	private int formId;
	private int Id;

	/**
	 * Saves the servlet config and context.
	 * 
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		this.config = config;
		this.context = this.config.getServletContext();
	}

	/**
	 * Dispatches GET requests.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String r = request.getParameter("r");
		String fid = request.getParameter("fid");
		String qid = request.getParameter("qid");
		String id = request.getParameter("id");

		if (fid != null)
			formId = Integer.parseInt(fid);
		if (qid != null)
			Integer.parseInt(qid);
		if (id != null)
			Id = Integer.parseInt(id);

		HttpSession session = request.getSession();

		if (session.getAttribute("access_token") == null)
			request.getRequestDispatcher("/Users").forward(request, response);

		if ((session.getAttribute("admin") != null) && session.getAttribute("admin").equals("true")) {
			if (r == null) {
				Answers a = new Answers(context);
				
				String answer_id = null;
				List<Integer> association_x_list = new LinkedList<Integer>();
				List<Integer> association_y_list = new LinkedList<Integer>();
				List<String> filters_list = new LinkedList<String>();
				Map<String, String[]> filters_map = request.getParameterMap();
				for (Map.Entry<String, String[]> entry : filters_map.entrySet()) {
					if (!entry.getKey().equals("referer") && !entry.getKey().equals("fid") && !entry.getKey().equals("p")) {
						if (entry.getKey().endsWith("_x")) {
							answer_id = entry.getKey().substring(0, entry.getKey().length() - 2); // answer_id is items name
							association_x_list.add(Integer.parseInt(answer_id));
							request.setAttribute("association_x_list", association_x_list);
						}
						if (entry.getKey().endsWith("_y")) {
							answer_id = entry.getKey().substring(0, entry.getKey().length() - 2); // answer_id is items name
							association_y_list.add(Integer.parseInt(answer_id));
							request.setAttribute("association_y_list", association_y_list);
						}
						
						if (entry.getKey().endsWith("_answer")) {
							answer_id = entry.getKey().substring(0, entry.getKey().length() - 7); // answer_id is items name
							filters_list.add(answer_id);
							request.setAttribute("filters_list", filters_list);
						}
					}
				}
				
				Map<Question, List<Answer>> answers_map = null;
				if (filters_list.isEmpty())
					answers_map = a.queryAll(formId);
				else
					answers_map = a.queryAll(formId, filters_list);
				request.setAttribute("answers_map", answers_map);

				Map<Long, User> users = new HashMap<Long, User>();
				Users u = new Users((String) session.getAttribute("access_token"));
				for (Map.Entry<Question, List<Answer>> entry : answers_map.entrySet())
					for (Answer item : entry.getValue())
						if (!users.containsKey(item.getAnswerer_id()))
							users.put(item.getAnswerer_id(), u.query(Long.toString(item.getAnswerer_id())));
				request.setAttribute("users", users);

				List<Map<String, Integer>> frequencies = new LinkedList<Map<String, Integer>>();
				for (Map.Entry<Question, List<Answer>> entry : answers_map.entrySet())
					frequencies.add(a.frequency(entry.getKey().getId()));
				request.setAttribute("frequencies", frequencies);
				
				DecimalFormat format = new DecimalFormat();
		        format.setDecimalSeparatorAlwaysShown(false);

				List<String> averages = new LinkedList<String>();
				for (Map.Entry<Question, List<Answer>> entry : answers_map.entrySet()) {
					double average = a.average(entry.getKey().getId());
					averages.add(format.format(average));
				}
				request.setAttribute("averages", averages);

				List<String> variances = new LinkedList<String>();
				for (Map.Entry<Question, List<Answer>> entry : answers_map.entrySet()) {
					double variance = a.variance(entry.getKey().getId(), a.average(entry.getKey().getId()));
					variances.add(format.format(variance));
				}
				request.setAttribute("variances", variances);
				
				double support = a.support(association_x_list, association_y_list);
				request.setAttribute("support", format.format(support * 100));
				
				double confidence = a.confidence(association_x_list, association_y_list);
				request.setAttribute("confidence", format.format(confidence * 100));
				
				try {
				// TEST
				//System.out.println(a.frequent(1).toString());
				//System.out.println(a.Mine(a.frequent(1), 1).toString());
	            ItemsetCollection L = a.frequent(Integer.parseInt(request.getParameter("minsup")));
	            //System.out.println(L);
	            List<AssociationRule> allRules = a.mine(L, Integer.parseInt(request.getParameter("minconf")));
	            List<String> rules = new LinkedList<String>();
	            for (AssociationRule rule : allRules)
	            	rules.add(rule.ToString());
	            request.setAttribute("minsup", request.getParameter("minsup"));
	            request.setAttribute("minconf", request.getParameter("minconf"));
	            request.setAttribute("rules", rules);
				} catch (NullPointerException e1) {
					// minsup or minconf null!
				} catch (NumberFormatException e2) {
					// minsup and minconf not integers!
			}

				request.getRequestDispatcher("answers/admin.jsp").forward(request, response);
			} else if (r.equals("delete")) {
				Answers a = new Answers(context);
				a.delete(Id);
				response.sendRedirect("Answers?fid=" + formId);
			}
		}
	}

	/**
	 * Dispatches POST requests.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		if (session.getAttribute("access_token") == null)
			request.getRequestDispatcher("/Users").forward(request, response);

		Map<String, String[]> map = null;
		Answers a = null;
		int question_id = 0;

		Boolean empty = true;
		List<Boolean> missing = new LinkedList<Boolean>();
		map = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			if (!entry.getKey().equals("referer") && !entry.getKey().equals("fid") && !entry.getKey().equals("p")) {
				a = new Answers(context);
				question_id = Integer.parseInt(entry.getKey()); // question_id is items name
				empty = true;
				for (String item : entry.getValue())
					if (!item.isEmpty())
						empty = false;
				if (a.isRequired(question_id) && empty)
					missing.add(true);
			}
		}

		if (!missing.isEmpty()) {
			request.setAttribute("answer_missing", "true");
			request.getRequestDispatcher("answers/error.jsp").forward(request, response);
		} else {
			map = request.getParameterMap();
			List<String> list = new LinkedList<String>();
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				if (!entry.getKey().equals("referer") && !entry.getKey().equals("fid") && !entry.getKey().equals("p")) {
					a = new Answers(context);
					question_id = Integer.parseInt(entry.getKey()); // question_id is items name
					list = new LinkedList<String>();
					for (String item : entry.getValue())
						if (!item.isEmpty())
							list.add(item);

					Answer ta = new Answer();

					// question_id
					ta.setQuestion_id(question_id);

					// answer
					ta.setAnswer(list);

					// answerer_id
					ta.setAnswerer_id(Long.parseLong(((User) session.getAttribute("user")).getId()));

					// cdatetime
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					ta.setCdatetime(sdf.format(new Date()));

					a.create(ta);
				}
			}
			request.getRequestDispatcher("answers/success.jsp").forward(request, response);
		}
	}
}
