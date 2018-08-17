package clef.datamodel.metadata;

import java.util.List;

import clef.api.domain.ClefResult;
import clef.mir.AlgorithmEnvironmentResponse;

/**
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public interface MetadataService {

	public List<ClefResult> mapMetadata( AlgorithmEnvironmentResponse aer );
	
}
