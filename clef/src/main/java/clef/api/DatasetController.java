package clef.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import clef.api.domain.response.ClefResponse;
import clef.api.domain.response.ClefResponseFactory;
import clef.api.domain.response.ResponseType;
import clef.common.ClefException;
import clef.mir.dataset.*;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

/**
 * REST Controller to handle requests for information about symbolic music datasets.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
@RestController
public class DatasetController {

	
	/**
	 * Lists the attributes of all discovered symbolic music datasets.
	 * 
	 * @return a ClefResponse of type DATASET_PROFILE
	 * @see ResponseType
	 * @since 1.0.0
	 */
	@RequestMapping( value = "/datasets", method = RequestMethod.GET )
	public ClefResponse listAll() {
		
		DatasetService ds = null;
		List<String> errs = new ArrayList<String>();
		try {
			ds = DatasetServiceImpl.getInstance();
		} catch ( ClefException | IOException e ) {
			errs.add( e.getMessage() );
			e.printStackTrace();
		}
		
		ClefResponse response = ClefResponseFactory.make( ResponseType.EMPTY );
		if ( ds != null ) {
			List<Dataset> dsets = ds.getAllDatasets();
			
			if ( ! dsets.isEmpty() ) {
				response = ClefResponseFactory.make( ResponseType.DATASET_PROFILE, dsets );
			} else {
				errs.add( "Fatal: no datasets found." );
			}
			
			
		} else {
			System.err.println( "DatasetService instance is null." );
		}
		
		if ( ! errs.isEmpty() ) {
			response = ClefResponseFactory.make( ResponseType.ERROR, errs );
		}
		
		return response;
	}
}
