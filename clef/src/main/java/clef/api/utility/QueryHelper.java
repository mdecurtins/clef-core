package clef.api.utility;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import clef.common.ClefException;

/**
 * A utility class to help with checking and extracting parameters received in a request URL.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class QueryHelper {

	
	/**
	 * Checks that all required parameters have been specified in the request.
	 * 
	 * This method checks only for the existence of the required parameter keynames; it does not check values.
	 * 
	 * @since 1.0.0
	 * @param requestParams
	 * @param required
	 * @return true if all required parameters exist in requestParams, false otherwise.
	 */
	public static boolean checkRequiredParameters( Map<String, String> requestParams, Set<String> required ) {
		return requestParams.keySet().containsAll( required );
	}
	
	
	/**
	 * Gets parameters and their values for a specific algorithm.
	 * 
	 * Algorithm-specific parameters are expected to be passed in the URL in the form: algorithm-name$parameter-name=value with no spaces.
	 * 
	 * @since 1.0.0
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
	
	
	/**
	 * Gets the values from a URL parameter key whose value is a CSV string.
	 * 
	 * Values are expected to be passed as a comma-separated list in the URL in the form: parameterName=value1[,...valueN] with no spaces.
	 * 
	 * @since 1.0.0
	 * @param requestParams a map of KV pairs generated from the request URL
	 * @return the names of the algorithms that the user has requested to be run.
	 */
	public static List<String> getCSVParameterValues( String parameterName, Map<String, String> requestParams ) throws ClefException {
		List<String> vals = null;
		String[] splitVals = null;
		String parameterValue = null;
		if ( ( parameterValue = requestParams.get( parameterName ) ) != null ) {
			splitVals = parameterValue.split( "," );
			vals = Arrays.asList( splitVals );
		}
		
		if ( vals.isEmpty() ) {
			throw new ClefException( "Could not retrieve CSV values for parameter: " + parameterName );
		}
		
		return vals;
	}
}
