package clef.api.utility;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import clef.common.ClefException;

public class QueryHelper {

	
	/**
	 * Checks that all required parameters have been specified in the request.
	 * 
	 * This method checks only for the existence of the required parameter keynames; it does not check values.
	 * 
	 * @param requestParams
	 * @param required
	 * @return true if all required parameters exist in requestParams, false otherwise.
	 */
	public static boolean checkRequiredParameters( Map<String, String> requestParams, Set<String> required ) {
		return requestParams.keySet().containsAll( required );
	}
	
	
	/**
	 * Gets the names of algorithms that the user has requested to be run.
	 * 
	 * Algorithm names are expected to be passed as a comma-separated list in the URL in the form: algorithms=name1[,...nameN] with no spaces.
	 * 
	 * @param requestParams
	 * @return the names of the algorithms that the user has requested to be run.
	 */
	public static List<String> getRequestedAlgorithms( Map<String, String> requestParams ) throws ClefException {
		List<String> algorithms = null;
		String[] algorithmNames = null;
		String parameterValue = null;
		if ( ( parameterValue = requestParams.get( "algorithms" ) ) != null ) {
			algorithmNames = parameterValue.split( "," );
			algorithms = Arrays.asList( algorithmNames );
		}
		
		if ( algorithms.isEmpty() ) {
			throw new ClefException( "You must specify at least one algorithm to run." );
		}
		
		return algorithms;
	}
	
	
	/**
	 * Gets parameters and their values for a specific algorithm.
	 * 
	 * Algorithm-specific parameters are expected to be passed in the URL in the form: algorithm-name$parameter-name=value with no spaces.
	 * 
	 * @param algorithmName
	 * @param requestParams
	 * @return parameter-value pairs for parameters specific to this algorithm
	 */
	public static Map<String, String> getAlgorithmParameters( String algorithmName, Map<String, String> requestParams ) {
		
		Map<String, String> params = new HashMap<String, String>();
		
		for ( Map.Entry<String, String> p : requestParams.entrySet() ) {
			if ( p.getKey().startsWith( algorithmName + "$" ) ) {
				String[] parts = p.getKey().split( "$" );
				params.put( parts[1], p.getValue() );
			}
		}
		return params;
	}
}
