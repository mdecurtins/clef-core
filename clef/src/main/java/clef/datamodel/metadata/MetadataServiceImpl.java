package clef.datamodel.metadata;

import java.util.List;
import java.util.stream.Collectors;

import clef.api.domain.ClefResult;
import clef.datamodel.db.ClefResultDAO;
import clef.mir.AlgorithmEnvironmentResponse;
import clef.mir.Result;

public class MetadataServiceImpl implements MetadataService {


	/**
	 * Gets a string of escaped string values for a SQL IN( 'foo',...n ) clause.
	 * 
	 * A little hack-y, but sometimes that's the way it is. {@code values} is expected to be a list of 
	 * filenames or dataset names, or other strings that should not contain escape characters like ""
	 * 
	 * @param values a list of strings to quote for a SQL IN clause
	 * @return
	 * @since 1.0.0
	 */
	private String getEscapedStringInArray( List<String> values ) {
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for ( String s : values ) {
			if ( first == true ) {
				sb.append( "'" + s + "'" );
				first = false;
			}
			sb.append( ",'" + s + "'" );
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param aer
	 * @return
	 * @since 1.0.0
	 */
	public List<ClefResult> mapMetadata( AlgorithmEnvironmentResponse aer ) {
		
		// Gather dataset names and file names from AER results.
		List<Result> results = aer.getResults();
		
		List<String> resultDatasetNames = results.stream().map( r -> r.getDatasetName() ).collect( Collectors.toList() );
		List<String> resultFilenames = results.stream().map( r -> r.getFilename() ).collect( Collectors.toList() );
		
		// Call stored proc with these values. Gets metadata where dataset IN list of names AND filename IN list of filenames.
		String datasetIN = this.getEscapedStringInArray( resultDatasetNames );
		String filenameIN = this.getEscapedStringInArray( resultFilenames );
		
		ClefResultDAO crd = new ClefResultDAO();
		List<Metadata> meta = crd.getJoinedMetadata( datasetIN, filenameIN );
		// Make instances of ClefResult from ResultSet values, using composition of datamodel objects
		
		return null;
	}
}
