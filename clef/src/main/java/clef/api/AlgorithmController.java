package clef.api;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import clef.api.domain.response.ClefResponse;
import clef.api.domain.response.ClefResponseFactory;
import clef.api.domain.response.ResponseType;
import clef.common.ClefException;
import clef.mir.AlgorithmAttributes;
import clef.mir.AlgorithmEnvironmentService;
import clef.mir.AlgorithmEnvironmentServiceImpl;

@RestController
public class AlgorithmController {

	
	@RequestMapping( value = "/algorithms", method = RequestMethod.GET )
	public ClefResponse listAll() {
		AlgorithmEnvironmentService aes = null;
		try {
			aes  = AlgorithmEnvironmentServiceImpl.getInstance();
		} catch ( ClefException | IOException e ) {
			e.printStackTrace();
		}
		
		ClefResponse response = ClefResponseFactory.make( ResponseType.EMPTY );
		if ( aes != null ) {
			List<String> algorithmNames = aes.listAlgorithmNames();
			List<String> errs = new ArrayList<String>();
			if ( ! algorithmNames.isEmpty() ) {
				List<AlgorithmAttributes> attrs = new ArrayList<AlgorithmAttributes>();
				for ( String a : algorithmNames ) {
					try {
						attrs.add( aes.getAlgorithmEnvironment(a).getAlgorithmAttributes() );
					} catch ( ClefException ce ) {
						errs.add( ce.getMessage() );
					}
				}
				if ( ! attrs.isEmpty() ) {
					response = ClefResponseFactory.make( ResponseType.ALGORITHM_PROFILE, attrs );
				}
			}
			
			if ( ! errs.isEmpty() ) {
				response = ClefResponseFactory.make( ResponseType.ERROR, errs );
			} 
		}
		
		return response;
	}
}
