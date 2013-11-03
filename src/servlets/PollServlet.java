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
import business.PollLogger;
import business.UserController;
import business.UserFeedback;

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
	    Main main = new Main();
	    response.setContentType("text/html");  
	    response.setCharacterEncoding("UTF-8");
	    switch (request.getParameter("requestType").toString()) {
	    case "activePolls":
	    	PollLogger.log("Active Poll servlet started");
	    	List<entities.Poll> activePolls = main.getActivePolls(em);
	    	String resp = "" ;
	    	for(int i =0; i < activePolls.size() -1;i++) {
	    		resp += activePolls.get(i).getId().toString() +" - "+ activePolls.get(i).getTitle()+ ";"; 
	    	}
	    	resp += activePolls.get(activePolls.size() -1).getId().toString() +" - "+ activePolls.get(activePolls.size() -1).getTitle();
	    	response.getWriter().write(resp);
	    	System.out.println("Response is :" +resp);
	    	break;
	    case "createPoll":
	    	PollLogger.log("Create poll started");
			business.PageCreation pc = new business.PageCreation();
		    long poll_id =Long.parseLong(request.getParameter("poll"));
		    String user_email = request.getParameter("email");
		    
		    UserController userCont = new UserController();
	    	if(!userCont.checkUserAnswers(em, user_email, poll_id)){
	    		response.getWriter().write("exist");
	    	}
	    	else {
	    		response.getWriter().write(pc.create(main.getQuestions(em, poll_id),main.getPollTitle(em, poll_id))); 
	    		PollLogger.log("Poll id is "+poll_id);
	    	}
	    	break;
	    case "loginType":
	    	PollLogger.log("Login Type servlet started");
		    String login_type = main.getLoginType(em);
		    if(!login_type.equals(null)) {
		    	response.getWriter().write(login_type);
		    }
		    else
		    	response.getWriter().write("email");
		    break;
	    case "deactivatePoll":
	    	PollLogger.log("Deactivate Poll Servlet started.");
		    poll_id = Long.parseLong(request.getParameter("pollId").toString().split("-")[0].trim());
		    main.setPollStatus(em, poll_id, false);
	    	break;
	    case "deletePoll":
	    	PollLogger.log("Delete Poll Servlet started.");
		    poll_id = Long.parseLong(request.getParameter("pollId").toString().split("-")[0].trim());
		    main.deletePoll(em, poll_id);
	    	break;
	    	default :
	    		System.out.println("Unknown request type");
	    		break;
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
	    Main main = new Main();
	    
	    response.setContentType("text/html");  
	    response.setCharacterEncoding("UTF-8");
	    String email = request.getParameter("email");
		Long pollId = Long.parseLong((request.getParameter("pollId")));
		String answers = request.getParameter("answers");
		PollLogger.log("Insert answer servlet started.");
		if(main.insertAnswers(em, email, pollId, answers)) {
			UserFeedback ufeed = new UserFeedback();
			response.getWriter().write(ufeed.addUserFeedback(em, pollId));
		}
			
		else
			response.getWriter().write("fail");
		
		em.close();
	    factory.close();
	}

}