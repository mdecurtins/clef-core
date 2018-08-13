package clef.api.domain.response;

import java.util.List;
import java.util.ArrayList;

import clef.mir.AlgorithmAttributes;

/**
 * Class representing an API response containing information about MIR algorithms.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class AlgorithmProfileResponse extends ClefResponse {

	private List<AlgorithmAttributes> algorithms;
	
	public AlgorithmProfileResponse() {
		super();
		this.responseType = ResponseType.ALGORITHM_PROFILE.toString();
		this.algorithms = new ArrayList<AlgorithmAttributes>();
	}
	
	public List<AlgorithmAttributes> getAlgorithms() {
		return this.algorithms;
	}
	
	public <T> void setData( List<T> l ) {
		for ( T o : l ) {
			AlgorithmAttributes a = (AlgorithmAttributes) o;
			if ( ! this.algorithms.contains(a) ) {
				this.algorithms.add( a );
			}
		}
	}
	
}
