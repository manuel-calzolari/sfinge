package controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import models.Form;
import models.User;
import dao.Forms;
import dao.Users;

/**
 * Servlet implementation class FormsController
 * 
 * @author Manuel Calzolari
 */
@WebServlet("/Forms")
public class FormsController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ServletConfig config;
	private ServletContext context;

	private int formId;

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
		String id = request.getParameter("id");

		if (id != null)
			formId = Integer.parseInt(id);

		HttpSession session = request.getSession();

		if (session.getAttribute("access_token") == null)
			request.getRequestDispatcher("/Users").forward(request, response);

		if ((session.getAttribute("admin") != null) && session.getAttribute("admin").equals("true")) {
			if (r == null)
				r = "admin";

			if (r.equals("admin")) {
				Forms f = new Forms(context);
				List<Form> forms = f.queryAll();
				request.setAttribute("forms", forms);

				Map<Integer, User> users = new HashMap<Integer, User>();
				Users u = new Users((String) session.getAttribute("access_token"));
				for (Form item : forms)
					if (!users.containsKey(item.getCreator_id()))
						users.put(item.getCreator_id(), u.query(Integer.toString(item.getCreator_id())));
				request.setAttribute("users", users);

				request.getRequestDispatcher("forms/admin.jsp").forward(request, response);
			} else if (r.equals("view")) {
				Forms f = new Forms(context);
				Form form = f.query(formId);
				request.setAttribute("form", form);

				Users u = new Users((String) session.getAttribute("access_token"));
				User user = u.query(Integer.toString(form.getCreator_id()));
				request.setAttribute("user", user);

				request.getRequestDispatcher("forms/view.jsp").forward(request, response);
			} else if (r.equals("create")) {
				request.getRequestDispatcher("forms/create.jsp").forward(request, response);
			} else if (r.equals("update")) {
				Forms f = new Forms(context);
				Form form = f.query(formId);
				request.setAttribute("form", form);
				request.getRequestDispatcher("forms/update.jsp").forward(request, response);
			} else if (r.equals("delete")) {
				Forms f = new Forms(context);
				f.delete(formId);
				response.sendRedirect("Forms?r=admin");
			}
		} if ((session.getAttribute("admin") == null) || !session.getAttribute("admin").equals("true"))
			request.getRequestDispatcher("forms/index.jsp").forward(request, response);
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

		if ((session.getAttribute("admin") != null) && session.getAttribute("admin").equals("true")) {
			Forms f = new Forms(context);

			Boolean error = false;

			Form tf = new Form();

			// title
			if (request.getParameter("title").isEmpty()) {
				error = true;
				request.setAttribute("error_required", "true");
			} else
				tf.setTitle(request.getParameter("title"));

			// desc
			tf.setDesc(request.getParameter("desc"));

			// cdatetime
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tf.setCdatetime(sdf.format(new Date()));

			// mdatetime
			tf.setMdatetime(sdf.format(new Date()));

			// creator_id
			tf.setCreator_id(Integer.parseInt(((User) request.getSession().getAttribute("user")).getId()));

			// passphrase
			if (request.getParameter("passphrase").isEmpty()) {
				error = true;
				request.setAttribute("error_required", "true");
			} else if (f.passphraseExists(formId, request.getParameter("passphrase"))) {
				error = true;
				request.setAttribute("error_passphrase", "true");
			} else
				tf.setPassphrase(request.getParameter("passphrase"));

			request.setAttribute("form", tf);

			if (error) {
				if (((String) request.getParameter("referer")).equals("create_forms"))
					request.getRequestDispatcher("forms/create.jsp").forward(request, response);
				else if (((String) request.getParameter("referer")).equals("update_forms"))
					request.getRequestDispatcher("forms/update.jsp").forward(request, response);
			} else {
				if (((String) request.getParameter("referer")).equals("create_forms"))
					f.create(tf);
				else if (((String) request.getParameter("referer")).equals("update_forms"))
					f.update(tf, formId);
				response.sendRedirect("Forms?r=admin");
			}
		}
	}
}
