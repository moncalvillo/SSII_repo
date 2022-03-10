package src.main.java.pai1;

public class EmailAtributes {
	private String message;
	private String subject;
	private String recipient;
	private String from;
	private String password;
	
	public EmailAtributes(String message, String subject, String recipient, String from, String password) {
		super();
		this.message = message;
		this.subject = subject;
		this.recipient = recipient;
		this.from = from;
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
