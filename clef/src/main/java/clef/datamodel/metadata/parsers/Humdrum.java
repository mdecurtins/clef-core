package clef.datamodel.metadata.parsers;

import clef.datamodel.*;
import clef.datamodel.metadata.Metadata;
import clef.utility.CheckedFunction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to parse Humdrum metadata.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class Humdrum {
	
	private static final Predicate<Path> isHumdrumFile = path -> path.toAbsolutePath().toString().endsWith( ".krn" );
	
	private static final CheckedFunction<Path, Metadata> createMetadata = path -> Humdrum.parse( path );
	
	public static Predicate<Path> getPredicate() {
		return isHumdrumFile;
	}
	
	public static CheckedFunction<Path, Metadata> getCreatorFunction() {
		return createMetadata;
	}

	public static Metadata parse( Path p ) throws IOException {
		
		Metadata m = new Metadata();
		
		Composer c = new Composer();
		DatasetContent dc = new DatasetContent();
		Era era = null;
		Work w = new Work();
		WorkType wt = null;
		
		for ( String line : Files.readAllLines( p, StandardCharsets.ISO_8859_1 ) ) {
			
			// Set the dataset content file name.
			dc.setFilename( p.getFileName().toString() );
			
			if ( line.startsWith( "!!!" ) ) {
				String[] parts = line.split( ":" );
				String metakey = parts[0].replaceAll( "!", "" );
				String metaval = parts[1].trim();
				switch ( metakey ) {
				case "COM":
					c.setName( metaval );
					break;
				case "OTL":
					w.setTitle( trimTitle( metaval ) );
					break;
				case "SCT":
					setWorkCatalogAndCatalogName( w, metaval );
					break;
				case "AST":
					era = new Era( metaval );
					break;
				case "AFR":
					wt = new WorkType( metaval );
					break;
				}
			}
		}
		
		// Assign the populated datamodel objects to the Metadata transport object.
		m.setComposer( c );
		m.setDatasetContent( dc );
		m.setWork( w );
		if ( era != null ) {
			m.setEra( era );
		}
		if ( wt != null ) {
			m.setWorkType( wt );
		}
		
		return m;
	}
	
	private static void setWorkCatalogAndCatalogName( Work w, String metaval ) {
		String[] both = metaval.split( "\\s+" );
		w.setCatalog( both[0] );
		w.setCatalogNumber( both[1] );
	}
	
	private static String trimTitle( String metaval ) {
		String title = "";
		metaval = metaval.trim();
		Pattern p = Pattern.compile( "([A-Za-z\\,'\\s]+)" );
		Matcher m = p.matcher( metaval );
		
		if ( m.matches() ) {
			title = m.group( 1 );
		}
		
		return title;
	}
}
