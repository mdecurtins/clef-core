package clef.mir;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import clef.common.ClefException;
import clef.mir.clefinfo.Parameter;


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
		
		List<Path> algorithmPaths = this.getDefinedAlgorithms();
		
		if ( algorithmPaths.isEmpty() ) {
			throw new ClefException( "AlgorithmEnvironmentService: No clefinfo.json files found within the algorithms directory tree." );
		}
		
		for ( Path path : algorithmPaths ) {
			System.out.println( "AESImpl: Constructing AlgorithmEnvironment from file: " + path.toString() );
			AlgorithmEnvironment ae = AlgorithmEnvironment.fromFile( path );
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
	 * Gets a list of algorithms defined for use by Clef.
	 * 
	 * @return a list of absolute paths to clefinfo.json files defining algorithm environments.
	 */
	public List<Path> getDefinedAlgorithms() throws ClefException {
		
		String algorithmsPath = System.getenv( ENV_ALGORITHMS_DIR );
		if ( algorithmsPath == null ) {
			throw new ClefException( "Environment variable " + ENV_ALGORITHMS_DIR + " is not defined or could not be retrieved." );
		}
		System.out.println( "Looking in " + algorithmsPath + " for clefinfo files." );
		Path p = FileSystems.getDefault().getPath( algorithmsPath );
		
		List<Path> clefinfoPaths = new ArrayList<Path>();
		
		try {
			Files.walkFileTree( p, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
					String absoluteFilename = file.toAbsolutePath().toString();
					if ( absoluteFilename.endsWith( "clefinfo.json" ) ) {
						clefinfoPaths.add( file.toAbsolutePath() );
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		
		return clefinfoPaths;
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
	
	private void printMap() {
		if ( this.algorithms.isEmpty() ) {
			System.out.println( "Algorithms map is empty!" );
		} else {
			for ( Map.Entry<String, AlgorithmEnvironment> entry : this.algorithms.entrySet() ) {
				System.out.println( "Entry:   Name: " + entry.getKey() + " Value: " + entry.getValue() );
			}
		}
	}
}
