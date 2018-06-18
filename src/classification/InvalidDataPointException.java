package classification;

/**
 * A special exception made for signaling when a datapoint is invalid, in order to terminate the calculation in an orderly manner.
 * @author Henrik Ronnholm
 *
 */
public class InvalidDataPointException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidDataPointException(String message, Exception cause) {
		super(message, cause);			
	}

	
}
