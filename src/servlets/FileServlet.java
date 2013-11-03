package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileItem;

import com.google.gson.*;

import business.Main;
import business.PollLogger;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileServlet() {
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
	    Long poll_id = Long.parseLong(request.getParameter("pollId"));
	    List<entities.Answers> answers = main.getPollAnswers(em, poll_id);
	    String results = "";
	    for(int i =0; i < answers.size() -1;i++) {
	    	results += answers.get(i).getUser_email() +";" +answers.get(i).getAnswers() +"\r\n"; 
    	}
	    results += answers.get(answers.size() -1).getUser_email() + ";" + answers.get(answers.size() -1).getAnswers();
	    response.getWriter().write(results);
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
		try {
			PollLogger.log("FileServlet post started.");
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                    String filename = item.getName();
                    PollLogger.log(filename + " selected");
                    String xml = item.getString();
                    response.setContentType("text");

                    long poll_id = main.insertPoll(em, xml);                    
                    if(poll_id > 0)
                    	response.getWriter().write(""+poll_id);
                    else
                    	response.getWriter().write("Error inserting poll");
                    PollLogger.log("New Poll id is:" + poll_id);
                
            }
        } catch (FileUploadException | URISyntaxException e) {
        	PollLogger.log("Parsing file upload failed." + e.toString());
            throw new ServletException("Parsing file upload failed.", e);
        }
		em.close();
	    factory.close();
	}

}
