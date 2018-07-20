package clef.mir;

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
