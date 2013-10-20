package business;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import entities.Admin;

public class UserController {

	public static boolean checkAdmin (EntityManager em, String username, String password) {
		try{
			Query query = em.createQuery("select a from Admin a where a.username = :username and a.password = :password");
			query.setParameter("username", username);
			query.setParameter("password", password);
			if(!query.getResultList().isEmpty())
			{
				entities.Admin ad = (Admin) query.getSingleResult();
				if(ad.isStatus()) {
					System.out.println("user exist");
					return true;
				}
				else {
					System.out.println("admin is not active");
					return false;
				}
			}
			else
			{
				System.out.println("admin not found");
				return false;
			}
		}catch (IllegalArgumentException e) {
			System.out.println(e.toString());
			return false;
		}
	}

}
