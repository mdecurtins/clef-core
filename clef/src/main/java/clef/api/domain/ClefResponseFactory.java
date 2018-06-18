package clef.api.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import clef.api.Application;

public class ClefResponseFactory {

	public static ClefResponse emptyResponse() {
		ClefResponse response = new ClefResponse( Application.VERSION, ResponseType.EMPTY.toString(), Application.SERVICE_NAME );
		return response;
	}
	
	public static ClefResponse errorResponse( String msg ) {
		ClefResponse response = new ClefResponse( Application.VERSION, ResponseType.ERROR.toString(), Application.SERVICE_NAME );
		ClefResponseBody container = new ClefErrorsContainer();
		List<ClefItem> err = Arrays.asList( new ClefError( msg ) );
		container.setAllItems( err );
		response.setResponseBody( container );
		return response;
	}
	
	public static ClefResponse errorResponse( List<String> errs ) {
		ClefResponse response = new ClefResponse( Application.VERSION, ResponseType.ERROR.toString(), Application.SERVICE_NAME );
		ClefResponseBody container = new ClefErrorsContainer();
		List<ClefItem> items = errs.stream().map( err -> new ClefError(err) ).collect( Collectors.toList() );
		container.setAllItems( items);
		response.setResponseBody( container );
		return response;
	}
	
	public static ClefResponse resultResponse( List<ClefItem> results ) {
		ClefResponse response = new ClefResponse( Application.VERSION, ResponseType.RESULT.toString(), Application.SERVICE_NAME );
		ClefResponseBody container = new ClefResultsContainer();
		container.setAllItems( results );
		response.setResponseBody( container );
		return response;
	}
}
