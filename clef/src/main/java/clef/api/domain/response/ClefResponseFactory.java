package clef.api.domain.response;

import java.util.List;

/**
 * Class to make API response objects.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class ClefResponseFactory {

	private static ClefResponse getConcreteResponse( ResponseType rt ) {
		ClefResponse response;
		switch ( rt ) {
		case ALGORITHM_PROFILE:
			response = new AlgorithmProfileResponse();
			break;
		case DATASET_PROFILE:
			response = new DatasetProfileResponse();
			break;
		case EMPTY:
			response = new EmptyResponse();
			break;
		case ERROR:
			response = new ErrorResponse();
			break;
		case RESULT:
			response = new ResultResponse();
			break;
		default:
			response = new EmptyResponse();
		}
		return response;
	}
	
	/**
	 * Make a response object without any data.
	 * 
	 * @param rt
	 * @return
	 * @see ResponseType
	 * @since 1.0.0
	 */
	public static ClefResponse make( ResponseType rt ) {
		return getConcreteResponse( rt );
	}
	
	
	/**
	 * Make a response object with data.
	 * 
	 * @param rt
	 * @param data
	 * @return
	 * @see ResponseType
	 * @since 1.0.0
	 */
	public static <T> ClefResponse make( ResponseType rt, List<T> data ) {
		ClefResponse response = getConcreteResponse( rt );
		if ( rt != ResponseType.EMPTY ) {
			response.setData( data );
		}
		return response;
	}
	
	
}
