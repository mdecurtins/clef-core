package clef.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class specifically for indicating functionality that is not yet implemented.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
public class ClefNotImplementedException extends ClefException {

	private static final long serialVersionUID = 2018_08_12_001L;
	
	public ClefNotImplementedException( String msg ) {
		super( msg );
	}

}
