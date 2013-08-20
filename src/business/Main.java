package business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


import entities.Question;

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
	public static void create(EntityManager em)
	{   
	    em.getTransaction().begin();
	    
	    Question q1=new Question();
        q1.setText("What is you gender?");
        q1.setType("single");
        q1.setNumber(1);
        ArrayList<String> q1_opts = new ArrayList<String>();
        q1_opts.add("Male");
        q1_opts.add("Female");
        q1.setPoll_id(1);
        q1.setOptions(q1_opts);
        
        Question q2=new Question();
        q2.setText("What are your hobbies?");
        q2.setType("multiple");
        q2.setNumber(2);
        ArrayList<String> q2_opts = new ArrayList<String>();
        q2_opts.add("Cinema");
        q2_opts.add("Football");
        q2_opts.add("Basketball");
        q2.setPoll_id(1);
        q2.setOptions(q2_opts);
        
        Question q3=new Question();
        q3.setText("How important is education for you?");
        q3.setType("slide");
        q3.setNumber(3);
        ArrayList<String> q3_opts = new ArrayList<String>();
        q3_opts.add("1");
        q3_opts.add("10");
        q3.setPoll_id(1);
        q3.setOptions(q3_opts);
        
        Question q4=new Question();
        q4.setText("What is you opinion for the poll?");
        q4.setType("text");
        q4.setNumber(4);
        q4.setPoll_id(1);
	    
	    em.persist(q1);
	    em.persist(q2);
	    em.persist(q3);
	    em.persist(q4);
	    em.getTransaction().commit();

	    
	}
	public static Question load(EntityManager em)
	{
		System.out.println("Load started");
		Question q = em.find(entities.Question.class, (long)4);
		System.out.println("Question found");
		for(String opt: q.getOptions() )
			System.out.println(opt);
		return q;
	}
	public static List<Question> getQuestions(EntityManager em, long poll_id)
	{
		Query query = em.createQuery("select q from Question q where q.poll_id = :poll_id");
		query.setParameter("poll_id", (long)1);
		return query.getResultList();
		
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

}