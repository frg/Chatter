package gravy.v2;

import java.io.Serializable;
import java.util.Calendar;

public class Message implements Serializable{
	private String message;
	private String timeStamp;
	
	private String recipient;
	
	public Message(String message){
		this.message = message;
		this.timeStamp = Calendar.getInstance().getTime().toString();
	}

	public String getMessage() {
		return message;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getRecipient() {
		return recipient;
	}
	
	public String toString(){
		return timeStamp + "\n" + message;
	}
}
