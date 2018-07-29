package clef.mir.xml;

import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ClefEntityResolver implements EntityResolver {

	public InputSource resolveEntity( String publicId, String systemId ) {
		
		InputSource src = null;
		
		if ( publicId.equals( "-//Recordare//DTD MusicXML 3.0 Partwise//EN" ) || systemId.equals( "http://www.musicxml.org/dtds/partwise.dtd" ) ) {
			//src = new InputSource( "/usr/local/dtds/partwise.dtd" );
			src = new InputSource( new StringReader( "" ) );
		}
		
		return src;
	}
}
