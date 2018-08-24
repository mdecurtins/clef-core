package clef.mir;

/**
 * Enum for symbolic music formats.
 * 
 * JSON is present as a format because Flat.io supports getting music as JSON data.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public enum MusicFormat {
	HUMDRUM ( "humdrum" ),
	JSON ( "json" ),
	MIDI ( "midi" ),
	MUSICXML ( "musicxml" );
	
	final String format;
	
	MusicFormat( String format ) {
		this.format = format;
	}
	
	@Override
	public String toString() {
		return this.format;
	}
}
