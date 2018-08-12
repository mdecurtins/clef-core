package clef.mir;

import java.util.List;
import java.util.Map;
import java.nio.file.Path;

import clef.common.ClefException;
import clef.mir.AlgorithmEnvironment;

public interface AlgorithmEnvironmentService {

	public AlgorithmEnvironment getAlgorithmEnvironment( String algorithmName ) throws ClefException;
	public List<String> listAlgorithmNames();
	public AlgorithmEnvironment mergeParameterValues( AlgorithmEnvironment ae, Map<String, String> params );
	
}
