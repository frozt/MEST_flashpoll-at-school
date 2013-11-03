package business;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PollLogger {

	public static void log(String message) {
		Logger logger = Logger.getLogger("logger");
		FileHandler fh;
		try {  
            
            // This block configure the logger with handler and formatter  
            fh = new FileHandler("/home/ahmet/mest_log/poll_logfile.log");  
            logger.addHandler(fh);  
            //logger.setLevel(Level.ALL);  
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  
              
            // the following statement is used to log any messages  
            logger.info(message);  
              
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	  
}
