package business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import entities.Answers20;
import entities.Question;

public class ResultsPageCreation {
	
	public String showAllStatistics(EntityManager em,long poll_id) {
		String page="";
		List<entities.Question> questionList;
		List<entities.Answers20> answers;
		questionList = getQuestionList(em, poll_id);
		answers = getPollData(em, poll_id);
		class resultCounter {
			int questionNo;
			String optionName;
			int total=-1;
			int count;
		}
		
		ArrayList<resultCounter> rc_list = new ArrayList<resultCounter>();
		for(Question q:questionList) {
			if(q.getType().equals("single") || q.getType().equals("multiple") ) {
				for(String opt:q.getOptions()){
					resultCounter rc = new resultCounter();
					rc.questionNo = q.getNumber();
					rc.optionName = opt;
					rc_list.add(rc);
				}
				for(Answers20 answer:answers){
					for(String ans:answer.getAnswer(q.getNumber())) {
						for(resultCounter r:rc_list) {
							if(r.questionNo == q.getNumber())
								if(r.optionName.equals(ans))
									r.count++;
						}
					}
				}
			}
			else if (q.getType().equals("slide")) {
				resultCounter rc = new resultCounter();
				rc.questionNo = q.getNumber();
				rc.total = 0;
				rc_list.add(rc);
				for(Answers20 answer:answers){
					for(String ans:answer.getAnswer(q.getNumber())) {
						for(resultCounter r:rc_list) {
							if(r.questionNo == q.getNumber())
								r.total += Integer.parseInt(ans);
						}
					}
				}
			}
			
		}
		// results will be listed with this string
		int questionNo = -1;
		for(resultCounter r:rc_list){
			if(questionNo != r.questionNo)
			{
				questionNo = r.questionNo;
				page += "<p><b>Question " + questionNo + "</b><br><hr>";
			}
			if(r.total == -1)
				page += r.optionName +"<i>"+ r.count +"</i><br>";
			else
				page += "Average <i>" +(double)r.total/answers.size()+"</i><br>";
		}
		
		return page;
	}
	private List<entities.Answers20> getPollData(EntityManager em,long poll_id) {
		//List<entities.Answers20> answers = new List<entities.Answers20>();
		Query query = em.createQuery("select a from Answers20 a where a.poll_id = :poll_id");
		query.setParameter("poll_id", poll_id);
	//	answers = (ArrayList<entities.Answers20>) query.getResultList();
		return query.getResultList();
	}
	private List<entities.Question> getQuestionList(EntityManager em,long poll_id){
	//	List<entities.Question> questionList = new List<entities.Question>();
		Query query = em.createQuery("select q from Question q where q.poll_id = :poll_id");
		query.setParameter("poll_id", poll_id);
	//	questionList = (List<entities.Question>) query.getResultList();
		return query.getResultList();
	}

}
