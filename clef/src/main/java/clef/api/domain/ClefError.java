package clef.api.domain;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing an error generated by the Clef system.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class ClefError extends ClefItem {

	private String errorMsg;
	
	
	/**
	 * Create a ClefError instance with the given msg string.
	 * 
	 * @param msg
	 * @since 1.0.0
	 */
	public ClefError( String msg ) {
		this.errorMsg = msg;
	}
	
	
	/**
	 * Create a list of ClefError instances from a list of strings.
	 * 
	 * @param errs a list of String objects from which to create ClefErrors
	 * @return a list of ClefError instances
	 * @since 1.0.0
	 */
	public static List<ClefError> fromStrings ( List<String> errs ) {
		return errs.stream().map( s -> new ClefError(s) ).collect( Collectors.toList() );
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public String getMessage() {
		return this.errorMsg;
	}
}
