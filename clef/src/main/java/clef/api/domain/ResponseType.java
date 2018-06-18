package clef.api.domain;

public enum ResponseType {
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
