package clef.mir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import clef.common.ClefException;
import clef.mir.clefinfo.Parameter;
import clef.utility.FileHandler;


public class AlgorithmEnvironmentServiceImpl implements AlgorithmEnvironmentService {
	

	/**
	 * This is the in-memory store of algorithm execution environments, keyed by algorithm name.
	 * 
	 * This store will be populated upon initialization of this service. This is the ONLY time that the put() method should be called on 
	 * this map. After initialization, methods should only get() items from this map; doing otherwise will corrupt the algorithm 
	 * execution environment objects.
	 */
	private Map<String, AlgorithmEnvironment> algorithms = null;
	
	private static AlgorithmEnvironmentService instance = null;
	
	private static final String ENV_ALGORITHMS_DIR = "ALGORITHMS_DIR";
	
	private AlgorithmEnvironmentServiceImpl() throws ClefException, IOException {
		this.algorithms = new HashMap<String, AlgorithmEnvironment>();
		
		String algorithmsPath = System.getenv( ENV_ALGORITHMS_DIR );
		
		List<AlgorithmEnvironment> instances = new ArrayList<AlgorithmEnvironment>();
		if ( algorithmsPath != null && ! algorithmsPath.equals( "" ) ) {
			instances = FileHandler.traversePath( algorithmsPath, AlgorithmEnvironment.getPredicate(), AlgorithmEnvironment.getCreatorFunction() );
		}
				
		
		if ( instances.isEmpty() ) {
			throw new ClefException( "AlgorithmEnvironmentService: No clefinfo.json files found within the algorithms directory tree." );
		}
		
		for ( AlgorithmEnvironment ae : instances ) {
			if ( ae instanceof AlgorithmEnvironment ) {
				System.out.println( "AESImpl: Putting with name: " + ae.getAlgorithmAttributes().getName() );
				this.algorithms.put( ae.getAlgorithmAttributes().getName(), ae );
			}
		}
		
	}
	
	
	/**
	 * Gets an algorithm execution environment for making a request.
	 * 
	 * @param algorithmName
	 * @return
	 * @throws ClefException
	 */
	public AlgorithmEnvironment getAlgorithmEnvironment( String algorithmName ) throws ClefException {
		AlgorithmEnvironment ae = this.algorithms.get( algorithmName );
		if ( ae == null ) {
			throw new ClefException( "AlgorithmEnvironmentService did not find an algorithm with name: " + algorithmName );
		}
		return ae;
	}
	

	
	
	/**
	 * 
	 * @return
	 */
	public static AlgorithmEnvironmentService getInstance() throws ClefException, IOException {
		if ( instance == null ) {
			instance = new AlgorithmEnvironmentServiceImpl();
		}
		return instance;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<String> listAlgorithmNames() {
		List<String> names = new ArrayList<String>();
		for ( String name : this.algorithms.keySet() ) {
			names.add( name );
		}
		return names;
	}
	

	/**
	 * Sets parameter values for a given request to an AlgorithmEnvironment.
	 * 
	 * @param ae
	 * @param params
	 * @return the AlgorithmEnvironment with parameter values set for the current request.
	 */
	public AlgorithmEnvironment mergeParameterValues( AlgorithmEnvironment ae, Map<String, String> params ) {
		for ( Parameter p : ae.getAlgorithmAttributes().getParameters() ) {
			String paramKey = p.getKey();
			String paramValue = params.get( paramKey );
			if ( paramValue != null ) {
				p.setValue( paramValue );
			}
		}
		return ae;
	}

}
