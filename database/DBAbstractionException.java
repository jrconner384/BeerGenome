package database;

public class DBAbstractionException extends Exception {	
	public DBAbstractionException(Exception e)
	{
		super(e);
	}
	
	public DBAbstractionException()
	{
		
	}

	public DBAbstractionException(String string)
	{
		super(string);
	}
}
