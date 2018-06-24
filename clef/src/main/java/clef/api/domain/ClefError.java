package clef.api.domain;

import java.util.List;
import java.util.stream.Collectors;

public class ClefError extends ClefItem {

	private String errorMsg;
	
	
	/**
	 * Create a ClefError instance with the given msg string.
	 * 
	 * @param msg
	 */
	public ClefError( String msg ) {
		this.errorMsg = msg;
	}
	
	
	/**
	 * Create a list of ClefError instances from a list of strings.
	 * 
	 * @param errs a list of String objects from which to create ClefErrors
	 * @return a list of ClefError instances
	 */
	public static List<ClefError> fromStrings ( List<String> errs ) {
		return errs.stream().map( s -> new ClefError(s) ).collect( Collectors.toList() );
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.errorMsg;
	}
}
