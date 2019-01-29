package arrayscript.parser.util;

public class ParsingException extends Exception {

	private static final long serialVersionUID = -6896415651652453816L;
	
	public ParsingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ParsingException(String message) {
		super(message);
	}
}