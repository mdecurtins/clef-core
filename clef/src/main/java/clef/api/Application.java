package clef.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import clef.api.domain.*;
import clef.api.domain.response.ClefResponse;
import clef.api.domain.response.ClefResponseFactory;
import clef.api.domain.response.ResponseType;
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
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE )
	public ClefResponse doSearch( @RequestParam Map<String, String> params, @RequestBody String musicxml ) {
		
		// Prepare the response and the container for the response items.
		ClefResponse response = null;
		
		try {
			ClefService cs = new ClefService();
			
			response = cs.doSearch( params, musicxml );
			
		} catch ( ClefException | IOException ce ) {
		
			// Create a ClefError item with the exception message.
			List<ClefItem> errors = Arrays.asList( new ClefError( ce.getMessage() ) );
			
			response = ClefResponseFactory.make( ResponseType.ERROR, errors );
		} finally {
			if ( response == null ) {
				response = ClefResponseFactory.make( ResponseType.EMPTY );
			}
		}
		
		
		return response;
	}
	
	public static void main(String[] args) {
		SpringApplication.run( Application.class, args );
	}

}
