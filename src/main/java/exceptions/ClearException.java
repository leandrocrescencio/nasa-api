package exceptions;


public class ClearException extends RuntimeException {

	private static final long serialVersionUID = -5141246164442856257L;
	
	public ClearException(String message) {
		super(message);
	}
}