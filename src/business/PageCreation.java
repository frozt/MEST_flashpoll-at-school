package business;

import java.util.List;
import entities.*;

public class PageCreation {
	public String create(List<Question> questions)
    {
        String page="";
        for(Question q : questions)
            page += question(q,questions.size());
        page += finalPage();
        return page;
    }

	 public String question (entities.Question q, int totalQuestion)
	    {
		 String question="<div data-role=\"page\" id=\"question"+q.getNumber()+"\"> "
	                +" <div data-role=\"header\"><h1>Flashpoll@Schools</h1></div>"
	                + " <div data-role=\"content\"> ";
	        
	        switch(q.getType())
	        {
	            case "single":
	                question += single(q,totalQuestion);               
	                break;
	            case "multiple":
	                question += multiple(q,totalQuestion);
	                break;
	            case "slide":
	                question += slide(q,totalQuestion);
	                break;
	            case "text":
	                question += text(q,totalQuestion);
	                break;
	            default:
	                question += "Question type did not received.</div></div>";
	                break;
	        }
	        
	        return question;
	    }
	 private String single(entities.Question q, int totalQuestion)
	    {
	        String content = " <fieldset data-role=\"controlgroup\" data-type=\"horizontal\"> <legend>"+q.getText()+" </legend> ";
	        for(int i=0;i<q.getOptions().size();i++)
	        {
	            content += " <input type=\"radio\" class=\"answers\" name=\"singleSelection\" id=\"choice"+i+"\"><label for=\"choice"+i+"\">"
	                    + q.getOptions().get(i) +"</label> ";
	        }
	        content += " </fieldset> ";
	        content += bottomButtons(q.getNumber(), totalQuestion);
	        content += progress(q.getNumber(),totalQuestion);
	        content += "</div></div>";
	        
	        return content;
	    }
	    private String multiple(entities.Question q, int totalQuestion)
	    {
	        String content = " <fieldset data-role=\"controlgroup\"> <legend>"+q.getText()+"<br> (You can select more than one answer) </legend> ";
	        for(int i=0;i<q.getOptions().size();i++)
	        {
	            content += " <input type=\"checkbox\" class=\"answers\" name=\"multiSelection"+i+"\" id=\"multiSelection"+i+" \" ><label for=\"multiSelection"+i+" \">"
	                    + q.getOptions().get(i) +"</label> ";
	        }
	        content += " </fieldset> ";
	        content += bottomButtons(q.getNumber(), totalQuestion);
	        content += progress(q.getNumber(),totalQuestion);
	        content += "</div></div>";
	        return content;
	    }
	    private String slide(entities.Question q, int totalQuestion)
	    {
	        String content = " <form><label for=\"slider\">"+q.getText()+"</label>";
	        content += "<input type=\"range\" class=\"ranger\" name=\"slider\" id=\"slider\" data-highlight=\"true\" "
	                + "min=\""+ q.getOptions().get(0)+ "\" "
	                + "max=\"" +q.getOptions().get(1) +"\">";
	        content += " <label class=\"slider_min\" for=\"slider\">"+ q.getOptions().get(2)+"</label> "
	        		+ " <label class=\"slider_max\" for=\"slider\">"+ q.getOptions().get(3)+"</label><br><br> ";
	        content += " </form> ";
	        content += bottomButtons(q.getNumber(), totalQuestion);
	        content += progress(q.getNumber(),totalQuestion);
	        content += "</div></div>";
	        return content;
	    }
	    private String text(entities.Question q, int totalQuestion)
	    {
	        String content = "<label for=\"textarea\">"+q.getText()+"</label>";
	        content += "<textarea rows=\"4\" class=\"answers\" name=\"textarea\" id=\"textarea\"></textarea>";
	        content += bottomButtons(q.getNumber(), totalQuestion);
	        content += progress(q.getNumber(),totalQuestion);
	        content += "</div></div>";
	        return content;
	    }
	    
	    private String progress(int value, int totalNo)
	    {
	        return "<input type=\"range\" name=\"progress\" id=\"progress\" data-track-theme=\"d\" data-theme=\"b\" min=\"0\" max=\""+totalNo + "\" value=\""
	                +value+"\" data-mini=\"true\" disabled=\"disabled\" data-highlight=\"true\">";
	    }
	    private String bottomButtons(int questionNo, int totalNo)
	    {
	        String prev="",next="";
	        if(questionNo == 1)
	        {
	           
	        	next="onclick=\"nextPage(false);\"";
	        }
	        else if (questionNo == totalNo)
	        {
	            prev="onclick=\"prevPage()\"";
	            next="onclick=\"nextPage(true);\"";
	        }
	        else
	        {
	            prev="onclick=\"prevPage()\"";
	            next="onclick=\"nextPage(false);\"";
	 
	        }

	        String buttons="";
	        if(questionNo == 1)
	        {
	        	buttons=" <fieldset class=\"ui-grid-a\">"
	        			+"<div class=\"ui-block-a\"></div>"
	                    +" <div class=\"ui-block-b\"><input type=\"button\" value=\"Next\" data-theme=\"b\" "
	                    +next+ "></div></fieldset>";
	        }
	        else
	        {
	        	buttons=" <fieldset class=\"ui-grid-a\"><div class=\"ui-block-a\"><input type=\"button\" value=\"Previous\" data-theme=\"b\""
                +prev+" ></div> "
                +" <div class=\"ui-block-b\"><input type=\"button\" value=\"Next\" data-theme=\"b\" "
                +next+ "></div></fieldset>";
	        }
	        	
	        return buttons;
	    }
	    private String finalPage ()
	    {
	        String content=" <div data-role=\"page\" id=\"final\"><div data-role=\"header\"><h1>MEST</h1></div> "
	                +" <div data-role=\"content\"><h1>Thank you for participation</h1>This is the end of survey."
	                + " <br>Your results are submitted.<div id=\"feedback\"></div>"
	                +"<fieldset class=\"ui-grid-a\"><div class=\"ui-block-a\"><input type=\"button\" value=\"Homepage\" data-theme=\"b\" "
	                +" onclick=\"window.location = 'poll.html'; \"></div></fieldset></div></div>";
	        return content;
	    }
}
