package business;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import entities.Poll;

public class UserFeedback {

	public String addUserFeedback(EntityManager em, Long poll_id) {
		String feedback="";
		Query query = em.createQuery("select p from Poll p where p.id = :pollId");
		query.setParameter("pollId", poll_id);
		LanguageBundle language = new LanguageBundle();
		Poll poll = (Poll) query.getSingleResult();
		for(String info: poll.getFeedback_info()) {
			switch (info) {
			case "total respondent":
				feedback +=language.getMessage("TotalRespondent")+": " + totalRespondent(em, poll_id);
				break;

			default:
				break;
			}
		}
		feedback += "<p><a href=\"statistics.html?id="+ poll_id + "\" target=”_blank”>Klicka för statistik</a></p>";
		return feedback;
	}

	private String totalRespondent(EntityManager em, Long poll_id) {
		Query query = em.createQuery("select count(a) from Answers a where a.poll_id = :pollId");
		query.setParameter("pollId", poll_id);
		return query.getSingleResult().toString();
	}
}
