package servlets;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.*;

import entities.Answers;
import business.Main;
import business.PollLogger;
import business.ResultsPageCreation;
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
	    case "deactivePolls":
	    	PollLogger.log("Deactive Poll servlet started");
	    	List<entities.Poll> deactivePolls = main.getDeactivePolls(em);
	    	resp = "" ;
	    	for(int i =0; i < deactivePolls.size() -1;i++) {
	    		resp += deactivePolls.get(i).getId().toString() +" - "+ deactivePolls.get(i).getTitle()+ ";"; 
	    	}
	    	resp += deactivePolls.get(deactivePolls.size() -1).getId().toString() +" - "+ deactivePolls.get(deactivePolls.size() -1).getTitle();
	    	response.getWriter().write(resp);
	    	System.out.println("Response is :" +resp);
	    	break;
	    case "createPoll":
	    	
	    	PollLogger.log("Create poll started");
			business.PageCreation pc = new business.PageCreation();
		    long poll_id =Long.parseLong(request.getParameter("poll"));
	    	
		    String user_email = request.getParameter("email");
		    
		    UserController userCont = new UserController();
		    if(main.checkPollId(em, poll_id)) {
		    	if(!userCont.checkUserAnswers(em, user_email, poll_id)){
		    		response.getWriter().write("exist");
		    	}
		    	else {
		    		response.getWriter().write(pc.create(main.getQuestions(em, poll_id),main.getPollTitle(em, poll_id))); 
		    		PollLogger.log("Poll id is "+poll_id);
		    	}
		    }
		    else 
		    	response.getWriter().write("invalid poll");
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
	    case "transferPoll":
	    	System.out.println("Transfer pll servlet started");
	    	poll_id = Long.parseLong(request.getParameter("pollId").toString().split("-")[0].trim());
	    	Query query = em.createQuery("select a from Answers a where a.poll_id = :poll_id");
			query.setParameter("poll_id", poll_id);
			for(int i=0;i<query.getResultList().size();i++) {
				
				entities.Answers ans = (Answers) query.getResultList().get(i);
				System.out.println("Loop started" + ans.getId());
				main.insertAnswers20(em, ans.getUser_email() , poll_id, ans.getAnswers());
			}
	    	
	    	break;
	    case "statistics":
	    	System.out.println("Statistics servlet started");
	    	poll_id = Long.parseLong(request.getParameter("pollId").toString().split("-")[0].trim());
	    	ResultsPageCreation rpc = new ResultsPageCreation();
	    	response.getWriter().write(rpc.showAllStatistics(em, poll_id));
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