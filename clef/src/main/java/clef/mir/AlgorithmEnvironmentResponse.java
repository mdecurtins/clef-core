package clef.mir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class AlgorithmEnvironmentResponse {

	private List<String> errors;
	private int itemsSearched;
	private List<Result> results;
	private String status;
	
	private static final Set<String> STATUSES = new HashSet<String>( Arrays.asList( "success", "fail", "error" ) );
	
	public AlgorithmEnvironmentResponse() {
		this.errors = new ArrayList<String>();
		this.itemsSearched = 0;
		this.results = new ArrayList<Result>();
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	public List<Result> getResults() {
		return results;
	}
	
	public String getStatus() {
		return status;
	}
	
	public int numItemsSearched() {
		return itemsSearched;
	}
	
	public void setErrors( List<String> errs ) {
		this.errors = errs;
	}
	
	public void setItemsSearched( int num ) {
		this.itemsSearched = num;
	}
	
	public void setResults( List<Result> r ) {
		this.results = r;
	}
	
	public void setStatus( String s ) {
		// Default to "fail" status if an invalid status string is passed.
		if ( ! STATUSES.contains( s.toLowerCase() ) ) {
			this.status = "fail";
		} else {
			this.status = s.toLowerCase();
		}
	}
	
	
	
}
