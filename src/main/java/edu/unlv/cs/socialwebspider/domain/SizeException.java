package edu.unlv.cs.socialwebspider.domain;

public class SizeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errorMessage;
	
	public SizeException()
	{
		super();
		errorMessage = "Size is too large";
	}
	
	public SizeException(String message)
	{
		super(message);
		errorMessage = message;
	}
	
	public String getError()
	{
		return errorMessage;
	}
	
}

