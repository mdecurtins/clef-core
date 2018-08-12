package clef.mir;

import java.util.List;
import java.util.Map;

import clef.common.ClefException;
import clef.mir.AlgorithmEnvironment;

/**
 * Simple interface for algorithm environment service operations.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public interface AlgorithmEnvironmentService {

	public AlgorithmEnvironment getAlgorithmEnvironment( String algorithmName ) throws ClefException;
	public List<String> listAlgorithmNames();
	public AlgorithmEnvironment mergeParameterValues( AlgorithmEnvironment ae, Map<String, String> params );
	
}
