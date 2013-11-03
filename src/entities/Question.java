package entities;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.persistence.*;

@Entity	
public class Question implements Serializable{
	private static final long serialVersionUID = 1L;
	 
    // Persistent Fields:
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private String type;
    private int number;
    private long poll_id;
    private ArrayList<String> options;
    @Version
	private Timestamp create_date;
    
	public Long getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public long getPoll_id() {
		return poll_id;
	}
	public void setPoll_id(long poll_id) {
		this.poll_id = poll_id;
	}
	public ArrayList<String> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}
	/*public String toHtml ()
	{
		String content;
		content =" <div data-role=\"header\"><h1>FlashPoll</h1></div>" + " <div data-role=\"content\"> ";
		content += " <fieldset data-role=\"controlgroup\"> <legend>"+this.getText()+" </legend> ";
		for(int i=0;i<this.getOptions().size();i++)
        {
            content += " <input type=\"radio\" name=\"singleSelection\" id=choice"+i+" \" value=\"off\"><label for=\"choice"+i+" \">"
                    + this.getOptions().get(i) +"</label> ";
        }
		content += " </fieldset></div>";
		return content;
	}*/

}