package br.com.fabianoss.security.exception;

import java.util.ArrayList;
import java.util.List;

public class LoginException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -695011851587361456L;

	private List<String> messages = new ArrayList<>();

	public LoginException() {
		super();
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

	public LoginException(List<String> messages) {
		super();
		this.messages = messages;
	}

	public LoginException(List<String> messages,Throwable cause) {
		super(cause);
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}

}
