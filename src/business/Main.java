package business;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import java.lang.*;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import entities.Question;
import entities.Poll;
import entities.User;

public class Main {

	private static EntityManagerFactory factory;
	private static final String PERSISTENCE_UNIT_NAME = "flashpoll";
	
	public Main() {
		
	}
	public boolean checkPollId (EntityManager em, long poll_id) {
		Query query = em.createQuery("select p from Poll p where p.id = :poll_id and p.status =:status");
		query.setParameter("poll_id", poll_id);
		query.setParameter("status", true);
		try {
			Poll poll = (Poll) query.getSingleResult();
			return true;
		}
		catch(NoResultException e) {
			return false;
		}
		catch(NullPointerException n) {
			return false;
		}
	}
	public  List<Question> getQuestions(EntityManager em, long poll_id)
	{
		Query query = em.createQuery("select q from Question q where q.poll_id = :poll_id");
		query.setParameter("poll_id", poll_id);
		return query.getResultList();
		
	}
	public String getPollTitle (EntityManager em, long poll_id) {
		Query query = em.createQuery("select p.title from Poll p where p.id = :poll_id");
		query.setParameter("poll_id", poll_id);
		return query.getSingleResult().toString();
	}
	public  Long getLastPollId (EntityManager em)
	{
		Query query = em.createQuery("select MAX(s.id) from Poll s");
		return (long)query.getResultList().get(0);
	}
	public  boolean checkEmail (EntityManager em, String email)
	{
		try{
			if(em.find(entities.User.class, email) != null)
			{
				PollLogger.log("user exist");
				return true;
			}
			else
			{
				PollLogger.log("user doesnt exist");
				return false;
			}
		}catch (IllegalArgumentException e) {
			PollLogger.log("checkmail function exception "+ e.toString());
			return false;
		}

	}
	public String checkLogin (EntityManager em, String username, String password) {
	//	Query query = em.createQuery("select u from User u where u.username = :username and u.password=:password");
		Query query = em.createQuery("select u from User u where u.username = :username");
		query.setParameter("username", username);
	//	query.setParameter("password", password);
		
		try {
			User user = (User) query.getSingleResult();
			if(user.getGender() == null)
				return "login new";
			else
				return "login exist";
		}
		catch(NoResultException e) {
			return "login new";
		}
	}
	public boolean insertUser(EntityManager em, String email, String gender, int age)
	{
		
		PollLogger.log("insertUser started");
		entities.User user = new entities.User();
		user.setEmail(email);
		user.setGender(gender);
		user.setAge(age);
		em.getTransaction().begin();
		try {
			em.persist(user);
			em.getTransaction().commit();
		}catch (Exception e){
			PollLogger.log("insertUser function exception "+e.toString());
			return false;
		}		
		
		PollLogger.log("Successful user insert with "+email+" "+age);
		return true;
	}
	public boolean insertUserWithUsername(EntityManager em, String username, String gender, int age)
	{
		
		PollLogger.log("insertUser started");
		entities.User user = new entities.User();
		user.setUsername(username);
		user.setGender(gender);
		user.setAge(age);
		em.getTransaction().begin();
		try {
			em.persist(user);
			em.getTransaction().commit();
		}catch (Exception e){
			PollLogger.log("insertUser function exception "+e.toString());
			return false;
		}		
		
		PollLogger.log("Successful user insert with "+username+" "+age);
		return true;
	}
	public boolean updateUser (EntityManager em, String username, String gender, int age) {
		PollLogger.log("updateUser started");
		
		Query query = em.createQuery("select u from User u where u.username = :username");
		query.setParameter("username", username);
		entities.User user;
		try {
			user = (User) query.getSingleResult();
		}
		catch(NoResultException e) {
			return false;
		}
		
		user.setAge(age);
		user.setGender(gender);
		em.getTransaction().begin();
		try {
			em.merge(user);
			em.getTransaction().commit();
		}catch (Exception e){
			PollLogger.log("updateUser function "+ e.toString());
			return false;
		}
		return true;
	}
	public boolean insertAnswers(EntityManager em, String user_email, Long poll_id, String answers)
	{
		PollLogger.log("insertAnswers started");
		entities.Answers answ = new entities.Answers();
		answ.setPoll_id(poll_id);
		answ.setUser_email(user_email);
		answ.setAnswers(answers);
		
		em.getTransaction().begin();
		try {
			em.persist(answ);
			em.getTransaction().commit();
		}catch (Exception e){
			PollLogger.log("insertAnswers exception "+e.toString());
			return false;
		}
		return true;
	}
	public long insertPoll(EntityManager em, String pollXml) throws URISyntaxException
	{
		PollLogger.log("insertPoll started");
		if(validateXml(pollXml))
		{
			parseXml(em, pollXml);
		}
		else 
			return -1;
		return getLastPollId(em);
	}
	public void setPollStatus (EntityManager em, long poll_id,boolean status) {
		Poll poll = em.find(entities.Poll.class, poll_id);
		em.getTransaction().begin();
		poll.setStatus(status);
		em.getTransaction().commit();
	}
	public void deletePoll (EntityManager em, long poll_id) {
		Poll poll = em.find(entities.Poll.class, poll_id);
		em.getTransaction().begin();
		em.remove(poll);
		em.getTransaction().commit();
	}
	private boolean validateXml(String pollXml) throws URISyntaxException {
		Source schemaFile = new StreamSource(new File("schema.xsd"));
        //Source xmlFile = new StreamSource(new File("../src/poll.xml"));
		//PollLogger.log(String.class.getResource("/schema.xsd").getPath());
		//Source schemaFile = new StreamSource(new File(String.class.getResource("/schema.xsd").getPath()));

        StringReader reader = new StringReader(pollXml);
        Source xmlFile = new javax.xml.transform.stream.StreamSource(reader);
        
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        
        try{
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            PollLogger.log(xmlFile.getSystemId() + " is valid");
        }
        catch (SAXException e) 
        {
            PollLogger.log(xmlFile.getSystemId() + " is NT valid");
            PollLogger.log("Reason: " + e.getLocalizedMessage());
            return false;
        }
        catch (Exception e) 
        {
            PollLogger.log(xmlFile.getSystemId() + " is NOT valid");
            PollLogger.log("Reason: " + e.getLocalizedMessage());
            return false;
        }
        return true;
	}
	private void parseXml(EntityManager em, String pollXml)
	{
		try {
			PollLogger.log("parseXml started");

	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(new InputSource(new StringReader(pollXml)));
	     
	        doc.getDocumentElement().normalize();
	     
	        
	        // parse feedback info elements
	        NodeList feedback = doc.getElementsByTagName("user_feedback");
	        ArrayList<String> feedback_list = new ArrayList<>();

	        for (int temp = 0; temp < feedback.getLength(); temp++) {
	        	Node info = feedback.item(temp);
	        	Element eElement = (Element) info;
	        	feedback_list.add(eElement.getElementsByTagName("info").item(temp).getTextContent());	        	
	        }
	        // parse poll title
	        NodeList title = doc.getElementsByTagName("title");
	        Element titleElement = (Element) title.item(0);
	        String titleStr = titleElement.getTextContent();
	        
	        // create and commit poll object into database
	        Poll poll = new Poll();
            poll.setStatus(true);
            poll.setFeedback_info(feedback_list);
            poll.setTitle(titleStr);
            
            em.getTransaction().begin();
    		try {
    			em.persist(poll);
    			em.getTransaction().commit();
    		}catch (Exception e){
    			PollLogger.log("ParseXml poll insert exception "+e.toString());
    		}
    		// parse question elements
    		NodeList nList = doc.getElementsByTagName("question");
    		
	        for (int temp = 0; temp < nList.getLength(); temp++) {
	     
	            Node nNode = nList.item(temp);
	     
	            PollLogger.log("\nCurrent Element :" + nNode.getNodeName());
	     
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	     
	                Element eElement = (Element) nNode;
	                
	        		Question question = new Question();
	                PollLogger.log("Text : " + eElement.getElementsByTagName("text").item(0).getTextContent());
	                question.setText(eElement.getElementsByTagName("text").item(0).getTextContent());
	                PollLogger.log("Type : " + eElement.getElementsByTagName("type").item(0).getTextContent());
	                question.setType(eElement.getElementsByTagName("type").item(0).getTextContent());
	                PollLogger.log("Number : " + eElement.getElementsByTagName("number").item(0).getTextContent());
	                question.setNumber(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent()));
	                if(eElement.getElementsByTagName("options").getLength() > 0)
	                {
	                	ArrayList<String> options = new ArrayList<String>();
	                	for(int i=0; i< eElement.getElementsByTagName("options").getLength(); i++)
	                	{
	                		PollLogger.log("Options : " + eElement.getElementsByTagName("options").item(i).getTextContent());
	                		options.add(eElement.getElementsByTagName("options").item(i).getTextContent());
	                	}
	                	question.setOptions(options);
	                }
	                question.setPoll_id(getLastPollId(em));
	                
	                if(!em.getTransaction().isActive())
	                    em.getTransaction().begin(); 
	        		try {
	        			em.persist(question);
	        			em.getTransaction().commit();
	        		}catch (Exception e){
	        			PollLogger.log("xml parsing exception "+e.toString());
	        		}
	     
	            }
	        }
	        } catch (Exception e) {
	        	PollLogger.log("parseXml function exception "+e.toString());
	        }
	}
	public List<entities.Poll> getActivePolls (EntityManager em)
	{
		Query query = em.createQuery("select p from Poll p where p.status = :status");
		query.setParameter("status", true);
		return query.getResultList();
	}
	public List<entities.Poll> getDeactivePolls (EntityManager em)
	{
		Query query = em.createQuery("select p from Poll p where p.status = :status order by p.id desc");
		query.setParameter("status", false);
		return query.getResultList();
	}
	public List<entities.Answers> getPollAnswers (EntityManager em, Long pollId) 
	{
		Query query = em.createQuery("select a from Answers a where a.poll_id = :pollId");
		query.setParameter("pollId", pollId);
		return query.getResultList();
	}
	
	public String getLoginType (EntityManager em) {
		Query query = em.createQuery("select l.login_type from Login l where l.id = :id");
		query.setParameter("id", (long)1);
		return query.getSingleResult().toString();
	}

}