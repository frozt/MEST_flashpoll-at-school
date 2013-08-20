package servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import business.Main;

/**
 * Servlet implementation class EmailServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("flashpoll");
	    EntityManager em = factory.createEntityManager();
	    response.setContentType("text");
	    if(Main.checkEmail(em, request.getParameter("email")))
	    	response.getWriter().write("exist");
	    else
	    	response.getWriter().write("not exist");
	    em.close();
	    factory.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("flashpoll");
	    EntityManager em = factory.createEntityManager();
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String occupation = request.getParameter("occupation");
		int age = Integer.parseInt(request.getParameter("age"));
		if(Main.insertUser(em, email, gender, occupation, age))
			response.getWriter().write("success");
		else
			response.getWriter().write("fail");
		em.close();
	    factory.close();
	}

}
