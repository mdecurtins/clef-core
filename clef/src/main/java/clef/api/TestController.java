package clef.api;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import clef.api.domain.*;
import clef.api.domain.response.ClefResponse;
import clef.api.domain.response.ClefResponseFactory;
import clef.api.domain.response.ResponseType;
import clef.api.utility.QueryHelper;
import clef.common.ClefException;


@RestController
public class TestController {

	@RequestMapping("/test")
	public Map<String, Object> getTestResponse() {
		Map<String, Object> testResponse = new HashMap<String, Object>();
		testResponse.put( "foo", new String( "bar" ) );
		testResponse.put( "baz", new Integer( 3 ) );
		return testResponse;
	}
	
	@RequestMapping("/test/foo")
	public Test getTestFoo() {
		return new Test( "foo", "Test Foo", "This is a test foo." );
	}
	
	@RequestMapping(value = "/test/params/algorithms", method = RequestMethod.POST)
	public ClefResponse testAlgorithms( @RequestParam Map<String, String> params ) {
	
		List<String> algorithms;
		ClefResponse response = null;
		try {
			algorithms = QueryHelper.getRequestedAlgorithms( params );
			response = ClefResponseFactory.make( ResponseType.ERROR, algorithms.stream().map( s -> new ClefError(s) ).collect( Collectors.toList() ) );
		} catch (ClefException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	@RequestMapping(value = "/test/params/required", method = RequestMethod.POST)
	public ClefResponse testRequiredParams( @RequestParam Map<String, String> params ) {
		ClefResponse response = null;
		try {
			Set<String> required = new HashSet<String>( Arrays.asList( "q", "algorithms" ) );
			boolean ok = QueryHelper.checkRequiredParameters( params, required );
			if ( ! ok ) throw new ClefException( "One or more required parameters missing" ); 
		} catch ( ClefException ce ) {
			List<String> errs = Arrays.asList( ce.getMessage() );
			response = ClefResponseFactory.make( ResponseType.ERROR, errs );
		} finally {
			if ( response == null ) {
				response = ClefResponseFactory.make( ResponseType.EMPTY );
			}
		}
		return response;
	}
	
	@RequestMapping("/cleftest")
	public ClefResponse testClefResponse() {
		ClefResponse response = ClefResponseFactory.make( ResponseType.EMPTY );
		return response;
	}
	
	@RequestMapping("/test/error")
	public ClefResponse testErrors() {
		List<ClefItem> errors = Arrays.asList(
				new ClefError( "Oh noooo the server returned an error." ),
				new ClefError( "Your query is too general." ),
				new ClefError( "Do you even know anything about music?" )
				);
		
		ClefResponse response = ClefResponseFactory.make( ResponseType.ERROR, errors );
		return response;
	}
}
