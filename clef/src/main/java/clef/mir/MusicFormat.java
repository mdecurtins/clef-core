package clef.mir;

public enum MusicFormat {
	HUMDRUM ( "humdrum" ),
	JSON ( "json" ),
	KERN ( "kern" ),
	MIDI ( "midi" ),
	MUSEDATA ( "musedata" ),
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
