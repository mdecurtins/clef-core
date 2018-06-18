package clef.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import clef.api.domain.*;
import clef.api.utility.QueryHelper;
import clef.common.ClefException;
import clef.common.ClefService;

@SpringBootApplication
@RestController
public class Application {

	public static final double VERSION = 0.1;
	public static final String SERVICE_NAME = "Clef REST API v" + VERSION;
	
	@RequestMapping("/")
	public String root() {
		return "Welcome to the " + SERVICE_NAME;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ClefResponse doSearch( @RequestParam Map<String, String> params ) {
		
		// Prepare the response and the container for the response items.
		ClefResponse response = null;
		ClefResponseBody container = null;
		
		try {
			ClefService cs = new ClefService();
			
			Set<String> required = new HashSet<String>( Arrays.asList( "q", "algorithms" ) );
			boolean ok = QueryHelper.checkRequiredParameters( params, required );
			if ( ! ok ) {
				throw new ClefException( "One or more required parameters missing" ); 
			}
			
			List<String> algorithms = QueryHelper.getRequestedAlgorithms( params );
			
			if ( algorithms.size() == 1 ) {
				response = cs.doSearch( algorithms.get(0), params );
			} else {
				throw new ClefException( "Multiple algorithms not yet implemented." );
			}
			
			
		} catch ( ClefException | IOException ce ) {
		
			response = new ClefResponse( VERSION, ResponseType.ERROR.toString(), SERVICE_NAME );
			container = new ClefErrorsContainer();
			
			// Create a ClefError item with the exception message.
			List<ClefItem> errors = Arrays.asList( new ClefError( ce.getMessage() ) );
			
			// Add the response item to the container, and the container to the response;
			container.setAllItems( errors );
			response.setResponseBody( container );
			
			
		} finally {
			if ( response == null ) {
				response = new ClefResponse( VERSION, ResponseType.EMPTY.toString(), SERVICE_NAME );
			}
		}
		
		
		return response;
	}
	
	public static void main(String[] args) {
		SpringApplication.run( Application.class, args );
	}

}
