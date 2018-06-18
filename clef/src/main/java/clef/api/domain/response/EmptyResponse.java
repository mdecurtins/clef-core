package clef.api.domain.response;

import java.util.List;

public class EmptyResponse extends ClefResponse {

	public EmptyResponse() {
		super();
		this.responseType = ResponseType.EMPTY.toString();
	}
	
	/**
	 * 
	 * N.B. The concrete implementation of this method in {@link clef.api.domain.response.EmptyResponse} does not do anything.
	 * 
	 * @param l
	 */
	public <T> void setData( List<T> l ) {
		
	}
}
