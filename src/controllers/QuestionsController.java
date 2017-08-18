package controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import components.Utils;

import dao.Forms;
import dao.Questions;

import models.Form;
import models.Question;

/**
 * Servlet implementation class QuestionsController
 */
@WebServlet("/Questions")
public class QuestionsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ServletConfig config;
	private ServletContext context;

	private int formId;
	private int questionId;

	/**
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
		String id = request.getParameter("id");
		String p = request.getParameter("p");

		if (fid != null)
			formId = Integer.parseInt(fid);
		if (id != null)
			questionId = Integer.parseInt(id);

		HttpSession session = request.getSession();

		if (session.getAttribute("access_token") == null)
			request.getRequestDispatcher("/Users").forward(request, response);

		if (r == null) {
			if ((fid != null) || (p != null)) {
				Forms f = new Forms(context);
				
				Form form = null;
				if (fid != null)
					form = f.query(formId);
				else if (p != null)
					form = f.query(p);
				
				request.setAttribute("form", form);

				Questions q = new Questions(context);
				
				List<Question> questions = null;
				if (fid != null)
					questions = q.queryAll(formId);
				else if (p != null)
					questions = q.queryAll(p);
				
				Boolean required = false;
				for (Question item : questions)
					if (item.getAnswer_required() == 1) {
						required = true;
						break;
					}
				if (required)
					request.setAttribute("required", "true");
				
				request.setAttribute("questions", questions);
				
				if (questions.isEmpty())
					request.getRequestDispatcher("questions/error.jsp").forward(request, response);
				else
					request.getRequestDispatcher("questions/index.jsp").forward(request, response);
			}
		} else if ((session.getAttribute("admin") != null) && session.getAttribute("admin").equals("true")) {
			if (r.equals("admin")) {
				Questions q = new Questions(context);
				List<Question> questions = q.queryAll(formId);
				request.setAttribute("questions", questions);
				request.getRequestDispatcher("questions/admin.jsp").forward(request, response);
			} else if (r.equals("view")) {
				Questions q = new Questions(context);
				Question question = q.query(questionId);
				request.setAttribute("question", question);
				request.getRequestDispatcher("questions/view.jsp").forward(request, response);
			} else if (r.equals("create")) {
				request.getRequestDispatcher("questions/create.jsp").forward(request, response);
			} else if (r.equals("update")) {
				Questions q = new Questions(context);
				Question question = q.query(questionId);
				request.setAttribute("question", question);
				if (question.getOptions() != null) {
					Gson gson = new Gson();
					request.setAttribute("options", Utils.JsonToSv(gson.toJson(question.getOptions())));
				}
				request.getRequestDispatcher("questions/update.jsp").forward(request, response);
			} else if (r.equals("delete")) {
				Questions q = new Questions(context);
				q.delete(questionId);
				response.sendRedirect("Questions?r=admin&fid=" + formId);
			} else if (r.equals("up")) {
				Questions q = new Questions(context);
				q.upOrder_no(questionId);
				response.sendRedirect("Questions?r=admin&fid=" + formId);
			} else if (r.equals("down")) {
				Questions q = new Questions(context);
				q.downOrder_no(questionId);
				response.sendRedirect("Questions?r=admin&fid=" + formId);
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

		Questions q = new Questions(context);

		if (((String) request.getParameter("referer")).equals("questions_index")) {
			request.getRequestDispatcher("/Answers").forward(request, response);
		} else if ((session.getAttribute("admin") != null) && session.getAttribute("admin").equals("true")) {
			if (((String) request.getParameter("referer")).equals("create_checkbox") || ((String) request.getParameter("referer")).equals("update_checkbox")) {
				Boolean error = false;

				Question tq = new Question();

				// question_type
				tq.setQuestion_type("checkbox");

				// form_id
				tq.setForm_id(formId);

				// question
				if (request.getParameter("question").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else
					tq.setQuestion(request.getParameter("question"));

				// order_no
				tq.setOrder_no(q.maxOrder_no(formId) + 1);

				// instructions
				tq.setInstructions(request.getParameter("instructions"));

				// options
				if (request.getParameter("options").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else {
					tq.setOptions(Utils.SvToJson(request.getParameter("options")));
					Gson gson = new Gson();
					request.setAttribute("options", Utils.JsonToSv(gson.toJson(tq.getOptions())));
				}

				// answer_required
				if ((request.getParameter("answer_required") != null) && request.getParameter("answer_required").equals("checked"))
					tq.setAnswer_required(1);

				// default_value
				tq.setDefault_value(request.getParameter("default_value"));

				// cdatetime
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				tq.setCdatetime(sdf.format(new Date()));

				// mdatetime
				tq.setMdatetime(sdf.format(new Date()));

				request.setAttribute("question", tq);

				if (error) {
					if (((String) request.getParameter("referer")).equals("create_checkbox"))
						request.getRequestDispatcher("questions/create.jsp").forward(request, response);
					else if (((String) request.getParameter("referer")).equals("update_checkbox"))
						request.getRequestDispatcher("questions/update.jsp").forward(request, response);
				} else {
					if (((String) request.getParameter("referer")).equals("create_checkbox"))
						q.create(tq);
					else if (((String) request.getParameter("referer")).equals("update_checkbox"))
						q.update(tq, questionId);
					response.sendRedirect("Questions?r=admin&fid=" + formId);
				}
			} else if (((String) request.getParameter("referer")).equals("create_password") || ((String) request.getParameter("referer")).equals("update_password")) {
				Boolean error = false;

				Question tq = new Question();

				// question_type
				tq.setQuestion_type("password");

				// form_id
				tq.setForm_id(formId);

				// question
				if (request.getParameter("question").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else
					tq.setQuestion(request.getParameter("question"));

				// order_no
				tq.setOrder_no(q.maxOrder_no(formId) + 1);

				// instructions
				tq.setInstructions(request.getParameter("instructions"));

				// answer_required
				if ((request.getParameter("answer_required") != null) && request.getParameter("answer_required").equals("checked"))
					tq.setAnswer_required(1);

				// default_value
				tq.setDefault_value(request.getParameter("default_value"));

				// max_length
				if (!request.getParameter("max_length").isEmpty())
					try {
						tq.setMax_length(Integer.parseInt(request.getParameter("max_length")));
					} catch (NumberFormatException nfe) {
						error = true;
						request.setAttribute("error_max_length", "true");
					}

				// cdatetime
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				tq.setCdatetime(sdf.format(new Date()));

				// mdatetime
				tq.setMdatetime(sdf.format(new Date()));

				// size
				tq.setSize(request.getParameter("size"));

				request.setAttribute("question", tq);

				if (error) {
					if (((String) request.getParameter("referer")).equals("create_password"))
						request.getRequestDispatcher("questions/create.jsp").forward(request, response);
					else if (((String) request.getParameter("referer")).equals("update_password"))
						request.getRequestDispatcher("questions/update.jsp").forward(request, response);
				} else {
					if (((String) request.getParameter("referer")).equals("create_password"))
						q.create(tq);
					else if (((String) request.getParameter("referer")).equals("update_password"))
						q.update(tq, questionId);
					response.sendRedirect("Questions?r=admin&fid=" + formId);
				}
			} else if (((String) request.getParameter("referer")).equals("create_radio") || ((String) request.getParameter("referer")).equals("update_radio")) {
				Boolean error = false;

				Question tq = new Question();

				// question_type
				tq.setQuestion_type("radio");

				// form_id
				tq.setForm_id(formId);

				// question
				if (request.getParameter("question").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else
					tq.setQuestion(request.getParameter("question"));

				// order_no
				tq.setOrder_no(q.maxOrder_no(formId) + 1);

				// instructions
				tq.setInstructions(request.getParameter("instructions"));

				// options
				if (request.getParameter("options").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else {
					tq.setOptions(Utils.SvToJson(request.getParameter("options")));
					Gson gson = new Gson();
					request.setAttribute("options", Utils.JsonToSv(gson.toJson(tq.getOptions())));
				}

				// answer_required
				if ((request.getParameter("answer_required") != null) && request.getParameter("answer_required").equals("checked"))
					tq.setAnswer_required(1);

				// default_value
				tq.setDefault_value(request.getParameter("default_value"));

				// cdatetime
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				tq.setCdatetime(sdf.format(new Date()));

				// mdatetime
				tq.setMdatetime(sdf.format(new Date()));

				request.setAttribute("question", tq);

				if (error) {
					if (((String) request.getParameter("referer")).equals("create_radio"))
						request.getRequestDispatcher("questions/create.jsp").forward(request, response);
					else if (((String) request.getParameter("referer")).equals("update_radio"))
						request.getRequestDispatcher("questions/update.jsp").forward(request, response);
				} else {
					if (((String) request.getParameter("referer")).equals("create_radio"))
						q.create(tq);
					else if (((String) request.getParameter("referer")).equals("update_radio"))
						q.update(tq, questionId);
					response.sendRedirect("Questions?r=admin&fid=" + formId);
				}
			} else if (((String) request.getParameter("referer")).equals("create_select") || ((String) request.getParameter("referer")).equals("update_select")) {
				Boolean error = false;

				Question tq = new Question();

				// question_type
				tq.setQuestion_type("select");

				// form_id
				tq.setForm_id(formId);

				// question
				if (request.getParameter("question").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else
					tq.setQuestion(request.getParameter("question"));

				// order_no
				tq.setOrder_no(q.maxOrder_no(formId) + 1);

				// instructions
				tq.setInstructions(request.getParameter("instructions"));

				// options
				if (request.getParameter("options").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else {
					tq.setOptions(Utils.SvToJson(request.getParameter("options")));
					Gson gson = new Gson();
					request.setAttribute("options", Utils.JsonToSv(gson.toJson(tq.getOptions())));
				}

				// answer_required
				if ((request.getParameter("answer_required") != null) && request.getParameter("answer_required").equals("checked"))
					tq.setAnswer_required(1);

				// default_value
				tq.setDefault_value(request.getParameter("default_value"));

				// cdatetime
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				tq.setCdatetime(sdf.format(new Date()));

				// mdatetime
				tq.setMdatetime(sdf.format(new Date()));

				request.setAttribute("question", tq);

				if (error) {
					if (((String) request.getParameter("referer")).equals("create_select"))
						request.getRequestDispatcher("questions/create.jsp").forward(request, response);
					else if (((String) request.getParameter("referer")).equals("update_select"))
						request.getRequestDispatcher("questions/update.jsp").forward(request, response);
				} else {
					if (((String) request.getParameter("referer")).equals("create_select"))
						q.create(tq);
					else if (((String) request.getParameter("referer")).equals("update_select"))
						q.update(tq, questionId);
					response.sendRedirect("Questions?r=admin&fid=" + formId);
				}
			} else if (((String) request.getParameter("referer")).equals("create_text") || ((String) request.getParameter("referer")).equals("update_text")) {
				Boolean error = false;

				Question tq = new Question();

				// question_type
				tq.setQuestion_type("text");

				// form_id
				tq.setForm_id(formId);

				// question
				if (request.getParameter("question").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else
					tq.setQuestion(request.getParameter("question"));

				// order_no
				tq.setOrder_no(q.maxOrder_no(formId) + 1);

				// instructions
				tq.setInstructions(request.getParameter("instructions"));

				// answer_required
				if ((request.getParameter("answer_required") != null) && request.getParameter("answer_required").equals("checked"))
					tq.setAnswer_required(1);

				// default_value
				tq.setDefault_value(request.getParameter("default_value"));

				// max_length
				if (!request.getParameter("max_length").isEmpty())
					try {
						tq.setMax_length(Integer.parseInt(request.getParameter("max_length")));
					} catch (NumberFormatException nfe) {
						error = true;
						request.setAttribute("error_max_length", "true");
					}

				// cdatetime
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				tq.setCdatetime(sdf.format(new Date()));

				// mdatetime
				tq.setMdatetime(sdf.format(new Date()));

				// size
				tq.setSize(request.getParameter("size"));

				request.setAttribute("question", tq);

				if (error) {
					if (((String) request.getParameter("referer")).equals("create_text"))
						request.getRequestDispatcher("questions/create.jsp").forward(request, response);
					else if (((String) request.getParameter("referer")).equals("update_text"))
						request.getRequestDispatcher("questions/update.jsp").forward(request, response);
				} else {
					if (((String) request.getParameter("referer")).equals("create_text"))
						q.create(tq);
					else if (((String) request.getParameter("referer")).equals("update_text"))
						q.update(tq, questionId);
					response.sendRedirect("Questions?r=admin&fid=" + formId);
				}
			} else if (((String) request.getParameter("referer")).equals("create_textarea") || ((String) request.getParameter("referer")).equals("update_textarea")) {
				Boolean error = false;

				Question tq = new Question();

				// question_type
				tq.setQuestion_type("textarea");

				// form_id
				tq.setForm_id(formId);

				// question
				if (request.getParameter("question").isEmpty()) {
					error = true;
					request.setAttribute("error_required", "true");
				} else
					tq.setQuestion(request.getParameter("question"));

				// order_no
				tq.setOrder_no(q.maxOrder_no(formId) + 1);

				// instructions
				tq.setInstructions(request.getParameter("instructions"));

				// answer_required
				if ((request.getParameter("answer_required") != null) && request.getParameter("answer_required").equals("checked"))
					tq.setAnswer_required(1);

				// default_value
				tq.setDefault_value(request.getParameter("default_value"));

				// cdatetime
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				tq.setCdatetime(sdf.format(new Date()));

				// mdatetime
				tq.setMdatetime(sdf.format(new Date()));

				// rows
				if (!request.getParameter("rows").isEmpty())
					try {
						tq.setRows(Integer.parseInt(request.getParameter("rows")));
					} catch (NumberFormatException nfe) {
						error = true;
						request.setAttribute("error_rows", "true");
					}

				// size
				tq.setSize(request.getParameter("size"));

				request.setAttribute("question", tq);

				if (error) {
					if (((String) request.getParameter("referer")).equals("create_textarea"))
						request.getRequestDispatcher("questions/create.jsp").forward(request, response);
					else if (((String) request.getParameter("referer")).equals("update_textarea"))
						request.getRequestDispatcher("questions/update.jsp").forward(request, response);
				} else {
					if (((String) request.getParameter("referer")).equals("create_textarea"))
						q.create(tq);
					else if (((String) request.getParameter("referer")).equals("update_textarea"))
						q.update(tq, questionId);
					response.sendRedirect("Questions?r=admin&fid=" + formId);
				}
			}
		}
	}
}
