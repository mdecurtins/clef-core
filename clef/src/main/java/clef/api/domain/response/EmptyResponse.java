package clef.api.domain.response;

import java.util.List;

/**
 * Class to return an empty API response.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class EmptyResponse extends ClefResponse {

	public EmptyResponse() {
		super();
		this.responseType = ResponseType.EMPTY.toString();
	}
	
	
	public <T> void setData( List<T> l ) {
		
	}
}
