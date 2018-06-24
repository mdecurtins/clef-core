package clef.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import clef.api.domain.*;
import clef.api.domain.response.*;
import clef.mir.*;
import clef.common.music.datamodel.*;

public class ClefService {

	private AlgorithmEnvironmentService aes;
	private SearchService ss;
	
	public ClefService() throws ClefException, IOException {
		
		this.aes = AlgorithmEnvironmentServiceImpl.getInstance();
		if ( ! (this.aes instanceof AlgorithmEnvironmentService) ) {
			throw new ClefException( "Fatal: could not obtain an instance of AlgorithmEnvironmentService." );
		}
		
		this.ss = new SearchServiceImpl();
	}
	
	public ClefResponse doSearch( String algorithm, Map<String, String> params ) {
		
		ClefResponse response = null;
		
		try {
			String query = this.getQueryString( params );
			
			AlgorithmEnvironment ae = this.aes.getAlgorithmEnvironment( algorithm );
			if ( ae != null ) {
				ae = this.aes.mergeParameterValues( ae, params );
			}
			
			// Proxy the request to the given AlgorithmEnvironment and deserialize the JSON response as 
			// an instance of AlgorithmEnvironmentResponse.
			AlgorithmEnvironmentResponse aer = this.executeRequest( query, ae );
			
			List<ClefItem> items = this.mapAlgorithmResponse( aer );
			
			// Choose the response type based on the response from the AlgorithmEnvironment.
			ResponseType rt = ( aer.getStatus().equals("success") ) ? ResponseType.RESULT : ResponseType.ERROR;
			
			response = ClefResponseFactory.make( rt , items );
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
	
	public void doSearch( List<String> algorithms, Map<String, String> params ) {
		//String query = this.getQueryString( params );
	}
	
	private AlgorithmEnvironmentResponse executeRequest( String q, AlgorithmEnvironment ae ) throws IOException {
		AlgorithmEnvironmentResponse aer = null;
		ObjectMapper mapper = new ObjectMapper();
		aer = mapper.readValue( this.ss.query(q, ae), AlgorithmEnvironmentResponse.class );
		return aer;
	}
	
	private String getQueryString( Map<String, String> params ) {
		return params.get( "q" );
	}
	
	private List<ClefItem> mapAlgorithmResponse( AlgorithmEnvironmentResponse aer ) {
		List<ClefItem> items = new ArrayList<ClefItem>();
		if ( aer.getStatus().equals( "success" ) ) {
			for ( Result r : aer.getResults() ) {
				// Initialize the components.
				ClefResult cr = new ClefResult();
				Work w = new Work();
				w.setTitle( r.getFilename() );
				Composer c = new Composer( "Johann Sebastian Bach", "1685", "1750" );
				
				cr.setRanking( r.getID() );
				cr.setComposer( c );
				cr.setWork(w);
				
				items.add( cr );
			}
		} else if ( aer.getStatus().equals( "error" ) ) {
			if ( ! aer.getErrors().isEmpty() ) {
				for ( String err : aer.getErrors() ) {
					items.add( new ClefError( err ) );
				}
			}
		}
		return items;
	}
}
