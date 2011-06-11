package ynn.network.util;

public class SerializationException extends Exception
{

	private static final long serialVersionUID = 1L;
	
	public SerializationException()
	{
		super();
	}
	
	public SerializationException(String message)
	{
		super(message);
	}
	
	public SerializationException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
