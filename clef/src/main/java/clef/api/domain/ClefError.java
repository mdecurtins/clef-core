package clef.api.domain;

public class ClefError extends ClefItem {

	private String errorMsg;
	
	public ClefError( String msg ) {
		this.errorMsg = msg;
	}
	
	public String getMessage() {
		return this.errorMsg;
	}
}
