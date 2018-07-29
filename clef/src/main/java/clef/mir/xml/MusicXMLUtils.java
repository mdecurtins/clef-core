package clef.mir.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class MusicXMLUtils {

	public static boolean containsChords( String musicxml ) {
		NodeList nl = getNodesWhere( getDocument( musicxml ), "//note/chord" );
		return ( nl.getLength() > 0 );
	}
	
	public static void getDistinctOffsets( String musicxml ) {
		
	}
	
	private static Document getDocument( String musicxml ) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			db.setEntityResolver( new ClefEntityResolver() );
			
			doc = db.parse( new InputSource( new StringReader(musicxml) ) );
		} catch ( ParserConfigurationException pce ) {
			pce.printStackTrace();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		} catch ( SAXException saxe ) {
			saxe.printStackTrace();
		}
		
		return doc;
	}
	
	public static int getMonophonicInputStaffIndex( String musicxml ) {
		
		List<Integer> staves = staffNumbersByNote( musicxml );
		
		if ( staves != null && staves.isEmpty() ) {
			return 0;
		}
		
		return staves.get( 0 );
	}
	
	
	private static NodeList getNodesWhere( Document doc, String xpath ) {
		NodeList nl = null;
		XPath xp = XPathFactory.newInstance().newXPath();
		try {
			XPathExpression xpr = xp.compile( xpath );
			nl = (NodeList) xpr.evaluate( doc, XPathConstants.NODESET );	
		} catch( XPathExpressionException xpee ) {
			xpee.printStackTrace();
		}
		return nl;
	}
	
	public static List<Integer> getPolyphonicInputStaffIndices( String musicxml ) {
		
		return null;
	}
	
	
	public static boolean musicIsOnOneStaff( String musicxml ) {
		List<Integer> staves = staffNumbersByNote( musicxml );
		return staves.stream().distinct().limit(2).count() <= 1;
	}
	
	
	
	private static List<Integer> staffNumbersByNote( String musicxml ) {
		
		List<Integer> values = new ArrayList<Integer>();
		Document doc = getDocument( musicxml );
		
		if ( doc != null ) {
			
			// Get <staff> elements where the parent <note> does not have a <rest/> child element.
			NodeList nl = getNodesWhere( doc, "//note[not(rest)]/staff" );
			if ( nl.getLength() > 0 ) {
				for ( int i = 0; i < nl.getLength(); i++ ) {
					Node n = nl.item( i );
					if ( n.getNodeType() == Node.ELEMENT_NODE ) {
						Element el = (Element) n;
						try {
							Integer val = Integer.parseInt( el.getTextContent() );
							values.add( val );
						} catch ( DOMException dome ) {
							dome.printStackTrace();
						} catch ( NumberFormatException nfe ) {
							values.add( 0 );
						}
					}
				}
			}
		} else {
			System.out.println( "staffNumbersByNote: Document is null" );
		}
		return values;
	}
	
	public static List<Node> trimTrailingRests( String musicxml, int staffIdx ) {
		String xpath = String.format( "//note[staff=%d]", staffIdx );
		NodeList nl = getNodesWhere( getDocument( musicxml ), xpath );
		List<Node> trimmed = new LinkedList<Node>();
		if ( nl.getLength() > 0 ) {
			// Starting at the last node, work backwards.
			int i;
			for ( i = nl.getLength(); i > 0; i-- ) {
				Node n = nl.item( i );
				if ( n.getNodeType() == Node.ELEMENT_NODE ) {
					Element el = (Element) n;
					if ( el.getElementsByTagName( "rest" ).getLength() != 0 ) {
						// This <note> contains a <rest/> element; keep going.
						continue;
					} else {
						// This <note> is not a rest; stop iteration.
						break;
					}
				}
			}
			// Starting at the beginning, work forwards until the ith node.
			for ( int j = 0; j < i; j++ ) {
				trimmed.add( nl.item( j ) );
			}
		}
		return trimmed;
	}
	
}
