package clef.datamodel.metadata.parsers;

import clef.datamodel.*;
import clef.datamodel.metadata.Metadata;
import clef.mir.dataset.Dataset;
import clef.utility.CheckedBiFunction;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.springframework.web.util.HtmlUtils;

/**
 * Class to parse Humdrum metadata.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class Humdrum {
	
	private static final Logger logger = LoggerFactory.getLogger( Humdrum.class );
	
	private static final Predicate<Path> isHumdrumFile = path -> path.toAbsolutePath().toString().endsWith( ".krn" );
	
	private static final CheckedBiFunction<Path, Dataset, Metadata> createMetadata = ( p, d ) -> Humdrum.parse( p, d );
	
	public static Predicate<Path> getPredicate() {
		return isHumdrumFile;
	}
	
	public static CheckedBiFunction<Path, Dataset, Metadata> getCreatorFunction() {
		return createMetadata;
	}
	
	private static List<Tag> getTags( String metaval ) {
		List<String> vals = Arrays.asList( metaval.split( "," ) );
		return vals.stream().map( s -> new Tag( s ) ).collect( Collectors.toList() );
	}

	public static Metadata parse( Path p, Dataset d ) throws IOException {
		
		Metadata m = new Metadata();
		
		Composer c = new Composer();
		DatasetContent dc = new DatasetContent(
					d.getCollection(),
					d.getDatasetAttributes().getName(),
					p.getFileName().toString()
				);
		
		Era era = null;
		List<Tag> tags = new ArrayList<Tag>();
		Work w = new Work();
		WorkType wt = null;
		
		for ( String line : Files.readAllLines( p, StandardCharsets.UTF_8 ) ) {
			
			// Lines that do not start with !!! are not metadata.
			if ( line.startsWith( "!!!" ) ) {
				String[] parts = line.split( ":" );
				String metakey = parts[0].replaceAll( "!", "" );
				String metaval = parts[1].trim();
				switch ( metakey ) {
				case "COM":
					c.setName( metaval );
					break;
				case "CDT":
					setComposerDates( c, metaval );
					break;
				case "OTL@@DE":
					w.setTitle( unescapeTitle( metaval ) );
					break;
				case "SCT":
					setWorkCatalogAndCatalogName( w, metaval );
					break;
				case "PC#":
					w.setPCN( metaval.trim() );
					break;
				case "AST":
					era = new Era( metaval );
					break;
				case "AFR":
					wt = new WorkType( metaval );
					break;
				case "AGN":
					tags = getTags( metaval );
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
		m.setTags( tags );
		
		return m;
	}
	
	
	/**
	 * 
	 * @since 1.0.0
	 * @param range
	 */
	private static void setComposerDates( Composer c, String range ) {
		
		Pattern pattern = Pattern.compile( "(\\d{4})" );
		Matcher matcher = pattern.matcher( range );
		
		String born = "";
		String died = "";
		
		int i = 1;
		while ( matcher.find() ) {
			if ( i == 1 ) {
				born = matcher.group();
			} else if ( i == 2 ) {
				died = matcher.group();
			}
			i++;
		}
		
		c.setDates( born, died );
	}
	
	
	/**
	 * 
	 * @param w
	 * @param metaval
	 * @since 1.0.0
	 */
	private static void setWorkCatalogAndCatalogName( Work w, String metaval ) {
		String[] both = metaval.split( "\\s+" );
		w.setCatalog( both[0] );
		w.setCatalogNumber( both[1] );
	}
	
	
	/**
	 * Converts any unescaped HTML entities in a string.
	 * 
	 * @param metaval
	 * @return
	 * @since 1.0.0
	 */
	private static String unescapeTitle( String metaval ) {
		return HtmlUtils.htmlUnescape( metaval );
	}
	
}
