package ynn.network.util;

public class DeserializationException extends Exception
{

	private static final long serialVersionUID = 1L;
	
	public DeserializationException()
	{
		super();
	}
	
	public DeserializationException(String message)
	{
		super(message);
	}
	
	public DeserializationException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
