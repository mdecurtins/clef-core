package clef.api.domain.response;

import java.util.List;
import java.util.ArrayList;

import clef.api.domain.ClefError;

public class ErrorResponse extends ClefResponse {

	private List<ClefError> errors;
	
	public ErrorResponse() {
		super();
		this.responseType = ResponseType.ERROR.toString();
		this.errors = new ArrayList<ClefError>();
	}
	
	public List<ClefError> getErrors() {
		return this.errors;
	}
	
	public <T> void setData( List<T> l ) {
		for ( T o : l ) {
			ClefError err = (ClefError) o;
			if ( ! this.errors.contains(err) ) {
				this.errors.add( err );
			}
		}
	}
}