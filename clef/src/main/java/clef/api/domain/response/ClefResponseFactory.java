package clef.api.domain.response;

import java.util.List;

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
	
	public static ClefResponse make( ResponseType rt ) {
		return getConcreteResponse( rt );
	}
	
	public static <T> ClefResponse make( ResponseType rt, List<T> data ) {
		ClefResponse response = getConcreteResponse( rt );
		if ( rt != ResponseType.EMPTY ) {
			response.setData( data );
		}
		return response;
	}
	
	
}
