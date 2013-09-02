package business;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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

public class Main {

	private static EntityManagerFactory factory;
	private static final String PERSISTENCE_UNIT_NAME = "flashpoll";
	
	public static void main() {
		// TODO Auto-generated method stub
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    EntityManager em = factory.createEntityManager();
//	    create(em);
//	    load(em);
//	    
	    em.close();
	}
	public static List<Question> getQuestions(EntityManager em, long poll_id)
	{
		Query query = em.createQuery("select q from Question q where q.poll_id = :poll_id");
		query.setParameter("poll_id", poll_id);
		return query.getResultList();
		
	}
	public static Long getLastPollId (EntityManager em)
	{
		Query query = em.createQuery("select MAX(s.id) from Poll s");
		return (long)query.getResultList().get(0);
	}
	public static boolean checkEmail (EntityManager em, String email)
	{
		try{
			if(em.find(entities.User.class, email) != null)
			{
				System.out.println("user exist");
				return true;
			}
			else
			{
				System.out.println("user doesnt exist");
				return false;
			}
		}catch (IllegalArgumentException e) {
			System.out.println(e.toString());
			return false;
		}

	}
	public static boolean insertUser(EntityManager em, String email, String gender, String occupation, int age)
	{
		System.out.println("insertUser started");
		entities.User user = new entities.User();
		user.setEmail(email);
		user.setGender(gender);
		user.setOccupation(occupation);
		user.setAge(age);
		em.getTransaction().begin();
		System.out.println("transaction started");
		try {
			em.persist(user);
			em.getTransaction().commit();
		}catch (Exception e){
			System.out.println(e.toString());
			return false;
		}
		System.out.println("Successful user insert with "+email+" "+age);
		return true;
	}
	public static boolean insertAnswers(EntityManager em, String user_email, Long poll_id, String answers)
	{
		entities.Answers answ = new entities.Answers();
		answ.setPoll_id(poll_id);
		answ.setUser_email(user_email);
		answ.setAnswers(answers);
		
		em.getTransaction().begin();
		System.out.println("transaction started");
		try {
			em.persist(answ);
			em.getTransaction().commit();
		}catch (Exception e){
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
	public static long insertPoll(EntityManager em, String pollXml) throws URISyntaxException
	{
		if(validateXml(pollXml))
		{
			parseXml(em, pollXml);
		}
		else 
			return -1;
		return getLastPollId(em);
	}
	private static boolean validateXml(String pollXml) throws URISyntaxException {
		Source schemaFile = new StreamSource(new File("schema.xsd"));
        //Source xmlFile = new StreamSource(new File("../src/poll.xml"));
		//System.out.println(String.class.getResource("/schema.xsd").getPath());
		//Source schemaFile = new StreamSource(new File(String.class.getResource("/schema.xsd").getPath()));
		URL url = ClassLoader.getSystemResource("/schema.xsd");
		System.out.println("-------------------------------");
		if(url == null) {
			System.out.println("Url is null");
		}
		else {
		System.out.println(url.toString());
		System.out.println(url.getPath());
		System.out.println(url.toURI());
		}
		
		
        StringReader reader = new StringReader(pollXml);
        Source xmlFile = new javax.xml.transform.stream.StreamSource(reader);
        
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        

        try{
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            System.out.println(xmlFile.getSystemId() + " is valid");
        }
        catch (SAXException e) 
        {
            System.out.println(xmlFile.getSystemId() + " is NT valid");
            System.out.println("Reason: " + e.getLocalizedMessage());
            return false;
        }
        catch (Exception e) 
        {
            System.out.println(xmlFile.getSystemId() + " is NOT valid");
            System.out.println("Reason: " + e.getLocalizedMessage());
            return false;
        }
        return true;
	}
	private static void parseXml(EntityManager em, String pollXml)
	{
		try {

	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(new InputSource(new StringReader(pollXml)));
	     
	        doc.getDocumentElement().normalize();
	     
	        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	     
	        NodeList nList = doc.getElementsByTagName("question");

	        Poll poll = new Poll();
            poll.setStatus(true);
            em.getTransaction().begin();
    		System.out.println("transaction started");
    		try {
    			em.persist(poll);
    			em.getTransaction().commit();
    		}catch (Exception e){
    			System.out.println(e.toString());
    		}
	     
	        for (int temp = 0; temp < nList.getLength(); temp++) {
	     
	            Node nNode = nList.item(temp);
	     
	            System.out.println("\nCurrent Element :" + nNode.getNodeName());
	     
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	     
	                Element eElement = (Element) nNode;
	                
	        		Question question = new Question();
	                System.out.println("Text : " + eElement.getElementsByTagName("text").item(0).getTextContent());
	                question.setText(eElement.getElementsByTagName("text").item(0).getTextContent());
	                System.out.println("Type : " + eElement.getElementsByTagName("type").item(0).getTextContent());
	                question.setType(eElement.getElementsByTagName("type").item(0).getTextContent());
	                System.out.println("Number : " + eElement.getElementsByTagName("number").item(0).getTextContent());
	                question.setNumber(Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent()));
	                if(eElement.getElementsByTagName("options").getLength() > 0)
	                {
	                	ArrayList<String> options = new ArrayList<String>();
	                	for(int i=0; i< eElement.getElementsByTagName("options").getLength(); i++)
	                	{
	                		System.out.println("Options : " + eElement.getElementsByTagName("options").item(i).getTextContent());
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
	        			System.out.println(e.toString());
	        		}
	     
	            }
	        }
	        } catch (Exception e) {
	        e.printStackTrace();
	        }
	}
	public static List<entities.Poll> getActivePolls (EntityManager em)
	{
		Query query = em.createQuery("select p from Poll p where p.status = :status");
		query.setParameter("status", true);
		return query.getResultList();
	}
	public static List<entities.Answers> getPollAnswers (EntityManager em, Long pollId) 
	{
		Query query = em.createQuery("select a from Answers a where a.poll_id = :pollId");
		query.setParameter("pollId", pollId);
		return query.getResultList();
	}
	
	

}