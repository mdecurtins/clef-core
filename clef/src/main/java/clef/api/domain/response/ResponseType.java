package clef.api.domain.response;

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
