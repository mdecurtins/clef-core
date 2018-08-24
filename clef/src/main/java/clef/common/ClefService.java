package clef.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import clef.api.domain.*;
import clef.api.domain.response.*;
import clef.api.utility.QueryHelper;
import clef.datamodel.metadata.MetadataService;
import clef.datamodel.metadata.MetadataServiceImpl;
import clef.datamodel.metadata.MetadataLoader;
import clef.mir.*;
import clef.mir.dataset.Dataset;
import clef.mir.dataset.DatasetService;
import clef.mir.dataset.DatasetServiceImpl;

/**
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
@Component
public class ClefService {

	private AlgorithmEnvironmentService aes;
	private ContainerProxyService cps;
	private DatasetService ds;
	private MetadataService ms;
	
	/**
	 * 
	 * @since 1.0.0
	 * @throws ClefException
	 * @throws IOException
	 */
	public ClefService() throws ClefException, IOException {
		
		this.aes = AlgorithmEnvironmentServiceImpl.getInstance();
		if ( ! (this.aes instanceof AlgorithmEnvironmentService) ) {
			throw new ClefException( "Fatal: could not obtain an instance of AlgorithmEnvironmentService." );
		}
		
		this.cps = new ContainerProxyServiceImpl();
		
		this.ds = DatasetServiceImpl.getInstance();
		if ( ! (this.ds instanceof DatasetService) ) {
			throw new ClefException( "Fatal: could not obtain an instance of DatasetService." );
		}
		
		this.ms = new MetadataServiceImpl();
		if ( ! (this.ms instanceof MetadataService) ) {
			throw new ClefException( "Fatal: could not obtain an instance of MetadataService." );
		}
	}
	
	
	/**
	 * Executes a music information retrieval search process.
	 * 
	 * @since 1.0.0
	 * @param requestParams
	 * @param requestBody
	 * @return
	 */
	public ClefResponse doSearch( Map<String, String> requestParams, String requestBody ) {
		
		ClefResponse response = null;
		
		try {
			
			if ( ! this.queryContainsRequiredParameters( requestParams ) ) {
				throw new ClefException( "One or more required parameters missing." );
			}
			
			
			List<String> algorithms = QueryHelper.getCSVParameterValues( "algorithms", requestParams ); 
			List<String> datasets = QueryHelper.getCSVParameterValues( "datasets", requestParams );
			
			
			String algorithmName = null;
			String datasetName = null;
			
			if ( datasets.size() != 1 ) {
				throw new ClefNotImplementedException( "Multiple datasets not yet implemented." );
			} else {
				datasetName = datasets.get( 0 );
			}
			
			if ( algorithms.size() != 1 ) {
				throw new ClefNotImplementedException( "Multiple algorithms not yet implemented." );
			} else {
				algorithmName = algorithms.get( 0 );
			}
			
			Query q = null;
			if ( algorithmName != null && datasetName != null ) {
				Map<String, String> algorithmParams = QueryHelper.getAlgorithmParameters( algorithmName, requestParams );
				q = this.makeQueryInstance( Query.MONOPHONIC, requestBody, algorithmName, datasetName, algorithmParams );
			}
			
			
			// Proxy the request to the given AlgorithmEnvironment and deserialize the JSON response as 
			// an instance of AlgorithmEnvironmentResponse.
			if ( q != null ) {
				AlgorithmEnvironmentResponse aer = this.executeRequest( q );
				
				List<ClefItem> items = this.mapAlgorithmResponse( aer );
				
				// Choose the response type based on the response from the AlgorithmEnvironment.
				ResponseType rt = ( aer.getStatus().equals("success") ) ? ResponseType.RESULT : ResponseType.ERROR;
				
				response = ClefResponseFactory.make( rt , items );
			}
			
		} catch ( ClefException | IOException e ) {
			List<ClefError> errs = Arrays.asList( new ClefError(e.getMessage()) );
			response = ClefResponseFactory.make( ResponseType.ERROR, errs );
		} finally {
			if ( response == null ) {
				response = ClefResponseFactory.make( ResponseType.EMPTY );
			}
		}
		
		return response;
	}

	
	/**
	 * Proxies the given {@link Query} to a container serving an algorithm.
	 * 
	 * @since 1.0.0
	 * @param q The query to proxy to the appropriate algorithm container.
	 * @return
	 * @throws IOException
	 */
	private AlgorithmEnvironmentResponse executeRequest( Query q ) throws IOException {
		AlgorithmEnvironmentResponse aer = null;
		ObjectMapper mapper = new ObjectMapper();
		
		// Use the AlgorithmEnvironmentResponse class to deserialize the JSON response returned by the algorithm container.
		aer = mapper.readValue( this.cps.query( q ), AlgorithmEnvironmentResponse.class );
		return aer;
	}
	
	
	/**
	 * Creates a single Query object that carries all necessary query data.
	 * 
	 * @since 1.0.0
	 * @param type One of the constants {@link Query#MONOPHONIC} or {@link Query#POLYPHONIC}.
	 * @param musicxml The MusicXML document to be used as the query input.
	 * @param algorithmName The name of the algorithm that should run this Query.
	 * @param datasetName The name of the dataset that should be searched by this Query.
	 * @param params Any algorithm-specific parameters and their values, retrieved from the request URL.
	 * @return An instance of {@link MonophonicQuery} or {@link PolyphonicQuery}, NULL if no instance could be obtained.
	 * @throws ClefException
	 */
	private Query makeQueryInstance( int type, String musicxml, String algorithmName, String datasetName, Map<String, String> params ) throws ClefException {
		if ( type != Query.MONOPHONIC && type != Query.POLYPHONIC ) {
			throw new ClefException( "Invalid type parameter value, must be one of Query.MONOPHONIC or Query.POLYPHONIC" );
		}
		
		// Get a fresh instance of the AlgorithmEnvironment for this algorithm.
		AlgorithmEnvironment ae = this.aes.getAlgorithmEnvironment( algorithmName );
		if ( ae != null ) {
			// Merge the algorithm-specific parameter values passed in the request URL. Returns an updated instance.
			ae = this.aes.mergeParameterValues( ae, params );
		}
		
		Dataset dset = this.ds.getDataset( datasetName );
		
		Query q = null;
		switch ( type ) {
		case Query.MONOPHONIC:
			q = MonophonicQuery.newInstance( ae, musicxml, dset );
			break;
		case Query.POLYPHONIC:
			
			break;
		}
		return q;
	}
	
	
	/**
	 * 
	 * @param aer
	 * @return
	 * @since 1.0.0
	 */
	private List<ClefItem> mapAlgorithmResponse( AlgorithmEnvironmentResponse aer ) {
		List<ClefItem> items = new ArrayList<ClefItem>();
		if ( aer.getStatus().equals( "success" ) ) {
			List<ClefResult> results = this.ms.mapMetadata( aer );
			items.addAll( results );
		} else if ( aer.getStatus().equals( "error" ) ) {
			if ( ! aer.getErrors().isEmpty() ) {
				for ( String err : aer.getErrors() ) {
					items.add( new ClefError( err ) );
				}
			}
		}
		return items;
	}
	
	
	/**
	 * Checks whether the request URL contains the required parameters.
	 * 
	 * Currently, the required parameters are "algorithms" and "datasets". Future versions may pull these values from a 
	 * configuration file.
	 * 
	 * @since 1.0.0
	 * @param requestParams The parameter key-value pairs present in the request URL.
	 * @return
	 */
	private boolean queryContainsRequiredParameters( Map<String, String> requestParams ) {
		Set<String> required = new HashSet<String>( Arrays.asList( "algorithms", "datasets" ) );
		return QueryHelper.checkRequiredParameters( requestParams, required );
	}

}
