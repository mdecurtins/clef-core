package clef.api.domain.response;

import java.util.List;
import java.util.ArrayList;

import clef.api.domain.ClefResult;

public class ResultResponse extends ClefResponse {

	private List<ClefResult> results;
	
	public ResultResponse() {
		super();
		this.responseType = ResponseType.RESULT.toString();
		this.results = new ArrayList<ClefResult>();
	}
	
	public List<ClefResult> getResults() {
		return this.results;
	}
	
	public <T> void setData( List<T> l ) {
		for ( T o : l ) {
			ClefResult cr = (ClefResult) o;
			if ( ! this.results.contains(cr) ) {
				this.results.add( cr );
			}
		}
	}
}
