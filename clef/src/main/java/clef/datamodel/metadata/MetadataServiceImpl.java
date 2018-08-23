package clef.datamodel.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import clef.api.domain.ClefResult;
import clef.datamodel.db.ClefResultDAO;
import clef.mir.AlgorithmEnvironmentResponse;
import clef.mir.Result;

/**
 * Class that provides metadata for symbolic music query results.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class MetadataServiceImpl implements MetadataService {

	private BiPredicate<Result, Metadata> matchMeta = ( r, m ) -> r.getDatasetName().equals( m.getDatasetContent().getDatasetName() ) &&
			r.getFilename().equals( m.getDatasetContent().getFilename() );

	/**
	 * Maps algorithm results contained in an instance of {@link AlgorithmEnvironmentResponse} to instances of {@link ClefResult}.
	 *
	 * This method uses the dataset and file names returned in the results from the algorithm to invoke a 
	 * stored procedure that retrieves metadata.
	 * 
	 * @param aer the response returned from a music information retrieval algorithm
	 * @return
	 * @see ClefResultDAO#getJoinedMetadata(String, String)
	 * @see clef.common.ClefService#doSearch(java.util.Map, String)
	 * @since 1.0.0
	 */
	public List<ClefResult> mapMetadata( AlgorithmEnvironmentResponse aer ) {
		
		List<ClefResult> cres = new ArrayList<ClefResult>();
		
		// Gather dataset names and file names from AER results.
		List<Result> results = aer.getResults();
		
		List<String> resultDatasetNames = results.stream().map( r -> r.getDatasetName() ).collect( Collectors.toList() );
		List<String> resultFilenames = results.stream().map( r -> r.getFilename() ).collect( Collectors.toList() );
		
		// Transform the collected result dataset names and filenames into CSV strings.
		String datasetIN = String.join( ",", resultDatasetNames );
		String filenameIN = String.join( ",", resultFilenames );
		
		// Call stored proc with these values. The proc will convert CSV string parameters into temp tables for use in subqueries.
		ClefResultDAO crd = new ClefResultDAO();
		List<Metadata> meta = crd.getJoinedMetadata( datasetIN, filenameIN );
		
		
		// Merge metadata into results
		for ( Result r : results ) {
			ClefResult cr = new ClefResult();
			for ( Metadata m : meta ) {
				if ( this.matchMeta.test( r, m ) ) {
					// We only need some of the properties that Metadata carries.
					cr.setRanking( r.getID() );
					cr.setComposer( m.getComposer() );
					cr.setWork( m.getWork() );
				}
			}
			cres.add( cr );
		}
		
		return cres;
	}
}
