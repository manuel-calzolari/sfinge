package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.User;

import components.Utils;

import dao.Users;

/**
 * Servlet implementation class UsersController
 * 
 * @author Manuel Calzolari
 */
@WebServlet("/Users")
public class UsersController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ServletConfig config;
	private ServletContext context;

	private String infoLogin;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		appLogin(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		appLogin(request, response);
	}

	/**
	 * Authenticates as Facebook application.
	 * 
	 * @see <a href="https://developers.facebook.com/docs/howtos/login/server-side-login/">Login for Server-side Apps</a>
	 */
	private void appLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> administrators = Arrays.asList(context.getInitParameter("app.administrators").split(","));

		String app_id = context.getInitParameter("app.id");
		String app_secret = context.getInitParameter("app.secret");
		String my_url = context.getInitParameter("app.canvas_page");

		HttpSession session = request.getSession(true);

		String code = request.getParameter("code");

		if (request.getAttribute("info_login") != null)
			infoLogin = (String) request.getAttribute("info_login");
		request.setAttribute("info_login", infoLogin);

		if (code == null) {
			SecureRandom random = new SecureRandom();
			String state = new BigInteger(130, random).toString(32);
			session.setAttribute("state", state);
			//response.sendRedirect("https://www.facebook.com/dialog/oauth?client_id=" + app_id + "&redirect_uri=" + my_url + "&state=" + state);
			// sendRedirect() doesn't work inside frames. Workaround: use JavaScript redirect.
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<script>");
			out.println("top.location.href='https://www.facebook.com/dialog/oauth?client_id=" + app_id + "&redirect_uri=" + URLEncoder.encode(my_url, "UTF-8") + "&state=" + state + "';");
			out.println("</script>");
			out.println("</head>");
			out.println("</html>");
		}

		if ((session.getAttribute("state") != null) && (session.getAttribute("state").equals(request.getParameter("state")))) {
			HashMap<String, String> params = Utils.ParseStr(Utils.UrlGetContents("https://graph.facebook.com/oauth/access_token?client_id=" + app_id + "&redirect_uri=" + URLEncoder.encode(my_url, "UTF-8")
					+ "&client_secret=" + app_secret + "&code=" + code));

			session.setAttribute("access_token", params.get("access_token"));

			Users u = new Users(session.getAttribute("access_token").toString());
			User user = u.query("me");
			session.setAttribute("user", user);

			if (administrators.contains(user.getUsername()))
			{
				session.setAttribute("admin", "true");
				//request.getRequestDispatcher("/Forms").forward(request, response);
				//session.setAttribute("info_login", "Hello, you just authenticated!");
				response.sendRedirect("Forms?r=admin");
			} else
				//request.getRequestDispatcher("users/index.jsp").forward(request, response);
				response.sendRedirect("Forms");
		}
	}
}
