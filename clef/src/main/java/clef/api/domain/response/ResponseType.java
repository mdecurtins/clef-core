package clef.api.domain.response;

/**
 * Enum representing the type of an API response.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public enum ResponseType {
	ALGORITHM_PROFILE ( "algorithm_profile" ),
	DATASET_PROFILE ( "dataset_profile" ),
	EMPTY ( "empty" ), 
	ERROR ( "error" ), 
	RESULT ( "result" );
	
	final String format;
	
	ResponseType( String format ) {
		this.format = format;
	}
	
	@Override
	public String toString() {
		return this.format;
	}
}
