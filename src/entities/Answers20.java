package entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Answers20
 *
 */
@Entity

public class Answers20 implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String user_email;
	private Long poll_id;
	@Version
	private Timestamp create_date;
	private ArrayList<String> answer1;
	private ArrayList<String> answer2;
	private ArrayList<String> answer3;
	private ArrayList<String> answer4;
	private ArrayList<String> answer5;
	private ArrayList<String> answer6;
	private ArrayList<String> answer7;
	private ArrayList<String> answer8;
	private ArrayList<String> answer9;
	private ArrayList<String> answer10;
	private ArrayList<String> answer11;
	private ArrayList<String> answer12;
	private ArrayList<String> answer13;
	private ArrayList<String> answer14;
	private ArrayList<String> answer15;
	public void setAnswer(ArrayList<String> answer, int questionNo) {
		switch(questionNo) {
		case 1:
			this.answer1 = answer;
			break;
		case 2:
			this.answer2 = answer;
			break;
		case 3:
			this.answer3 = answer;
			break;
		case 4:
			this.answer4 = answer;
			break;
		case 5:
			this.answer5 = answer;
			break;
		case 6:
			this.answer6 = answer;
			break;
		case 7:
			this.answer7 = answer;
			break;
		case 8:
			this.answer8 = answer;
			break;
		case 9:
			this.answer9 = answer;
			break;
		case 10:
			this.answer10 = answer;
			break;
		case 11:
			this.answer11 = answer;
			break;
		case 12:
			this.answer12 = answer;
			break;
		case 13:
			this.answer13 = answer;
			break;
		case 14:
			this.answer14 = answer;
			break;
		case 15:
			this.answer15 = answer;
			break;
		}
	}
	public ArrayList<String> getAnswer(int questionNo) {
		switch(questionNo) {
		case 1:
			return answer1;
		case 2:
			return answer2; 
		case 3:
			return answer3;
		case 4:
			return answer4;
		case 5:
			return answer5;
		case 6:
			return answer6;
		case 7:
			return answer7;
		case 8:
			return answer8;
		case 9:
			return answer9;
		case 10:
			return answer10;
		case 11:
			return answer11;
		case 12:
			return answer12;
		case 13:
			return answer13;
		case 14:
			return answer14;
		case 15:
			return answer15;
			default:
				return null;
		}
	}
	public ArrayList<String> getAnswer1() {
		return answer1;
	}
	public void setAnswer1(ArrayList<String> answer1) {
		this.answer1 = answer1;
	}
	public ArrayList<String> getAnswer2() {
		return answer2;
	}
	public void setAnswer2(ArrayList<String> answer2) {
		this.answer2 = answer2;
	}
	public ArrayList<String> getAnswer3() {
		return answer3;
	}
	public void setAnswer3(ArrayList<String> answer3) {
		this.answer3 = answer3;
	}
	public ArrayList<String> getAnswer4() {
		return answer4;
	}
	public void setAnswer4(ArrayList<String> answer4) {
		this.answer4 = answer4;
	}
	public ArrayList<String> getAnswer5() {
		return answer5;
	}
	public void setAnswer5(ArrayList<String> answer5) {
		this.answer5 = answer5;
	}
	public ArrayList<String> getAnswer6() {
		return answer6;
	}
	public void setAnswer6(ArrayList<String> answer6) {
		this.answer6 = answer6;
	}
	public ArrayList<String> getAnswer7() {
		return answer7;
	}
	public void setAnswer7(ArrayList<String> answer7) {
		this.answer7 = answer7;
	}
	public ArrayList<String> getAnswer8() {
		return answer8;
	}
	public void setAnswer8(ArrayList<String> answer8) {
		this.answer8 = answer8;
	}
	public ArrayList<String> getAnswer9() {
		return answer9;
	}
	public void setAnswer9(ArrayList<String> answer9) {
		this.answer9 = answer9;
	}
	public ArrayList<String> getAnswer10() {
		return answer10;
	}
	public void setAnswer10(ArrayList<String> answer10) {
		this.answer10 = answer10;
	}
	public ArrayList<String> getAnswer11() {
		return answer11;
	}
	public void setAnswer11(ArrayList<String> answer11) {
		this.answer11 = answer11;
	}
	public ArrayList<String> getAnswer12() {
		return answer12;
	}
	public void setAnswer12(ArrayList<String> answer12) {
		this.answer12 = answer12;
	}
	public ArrayList<String> getAnswer13() {
		return answer13;
	}
	public void setAnswer13(ArrayList<String> answer13) {
		this.answer13 = answer13;
	}
	public ArrayList<String> getAnswer14() {
		return answer14;
	}
	public void setAnswer14(ArrayList<String> answer14) {
		this.answer14 = answer14;
	}
	public ArrayList<String> getAnswer15() {
		return answer15;
	}
	public void setAnswer15(ArrayList<String> answer15) {
		this.answer15 = answer15;
	}
	public Long getPoll_id() {
		return poll_id;
	}
	public void setPoll_id(Long poll_id) {
		this.poll_id = poll_id;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	
}
