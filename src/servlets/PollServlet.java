package servlets;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.*;

import business.Main;

/**
 * Servlet implementation class DbServlet
 */
@WebServlet("/PollServlet")
public class PollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PollServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("flashpoll");
	    EntityManager em = factory.createEntityManager();
	    
	    if(request.getParameter("requestType").equals("activePolls")) {
	    	List<entities.Poll> activePolls = Main.getActivePolls(em);
	    	response.setContentType("text");
	    	response.setCharacterEncoding("UTF-8");
	    	String resp = "" ;
	    	for(int i =0; i < activePolls.size() -1;i++) {
	    		resp += activePolls.get(i).getId().toString() + ";"; 
	    	}
	    	resp += activePolls.get(activePolls.size() -1).getId().toString();
	    	response.getWriter().write(resp);
	    	System.out.println("Response is :" +resp);
	    }
	    else if (request.getParameter("requestType").equals("createPoll")){
	    	System.out.println("Create poll started");
	    	System.out.println(request.getParameter("poll"));
			business.PageCreation pc = new business.PageCreation();
		    long poll_id =Long.parseLong(request.getParameter("poll"));
		    
		    response.setContentType("text/html");  
		    response.setCharacterEncoding("UTF-8"); 
		    response.getWriter().write(pc.create(Main.getQuestions(em, poll_id))); 
		    System.out.println("Poll id is "+poll_id);
	    }
	    else {
	    	System.out.println("Unknown request type");
	    }
	    
	    em.close();
	    factory.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("flashpoll");
	    EntityManager em = factory.createEntityManager();
	    
	    String email = request.getParameter("email");
		Long pollId = Long.parseLong((request.getParameter("pollId")));
		String answers = request.getParameter("answers");
		if(Main.insertAnswers(em, email, pollId, answers))
			response.getWriter().write("success");
		else
			response.getWriter().write("fail");
		
		em.close();
	    factory.close();
	}

}