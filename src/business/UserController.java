package business;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import entities.Admin;

public class UserController {

	public boolean checkAdmin (EntityManager em, String username, String password) {
		try{
			Query query = em.createQuery("select a from Admin a where a.username = :username and a.password = :password");
			query.setParameter("username", username);
			query.setParameter("password", password);
			if(!query.getResultList().isEmpty())
			{
				entities.Admin ad = (Admin) query.getSingleResult();
				if(ad.isStatus()) {
					PollLogger.log("user exist");
					return true;
				}
				else {
					PollLogger.log("admin is not active");
					return false;
				}
			}
			else
			{
				PollLogger.log("admin not found");
				return false;
			}
		}catch (IllegalArgumentException e) {
			PollLogger.log("checkAdmin function exception "+e.toString());
			return false;
		}
	}
	// returns true if user did not attend the poll, false if user already attended the poll
	public boolean checkUserAnswers (EntityManager em, String user_email, Long poll_id) {
		Query query = em.createQuery("select a from Answers a where a.user_email = :user_email and a.poll_id = :poll_id");
		query.setParameter("user_email", user_email);
		query.setParameter("poll_id", poll_id);
		if(query.getResultList().isEmpty())
			return true;
		else
			return false;
	}

}
