package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Run
 * 
 * @author Manuel Calzolari
 */
@WebServlet("/Run/")
public class Run extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Forwards the GET request to the user authentication controller.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/Users").forward(request, response);
	}

	/**
	 * Forwards the POST request to the user authentication controller.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/Users").forward(request, response);
	}
}
