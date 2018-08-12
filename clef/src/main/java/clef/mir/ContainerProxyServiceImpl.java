package clef.mir;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

import clef.mir.clefinfo.Parameter;

public class ContainerProxyServiceImpl implements ContainerProxyService {

	
	/**
	 * Gets the response body from the HTTP connection.
	 * 
	 * The response body is expected to be a JSON-formatted string conforming to the 
	 * specification for responses from Clef algorithm containers.
	 * 
	 * @since 1.0.0
	 * @param conn
	 * @return
	 */
	private String extractResponse( HttpURLConnection conn ) {
		try {
			int responseCode = conn.getResponseCode();
			if ( responseCode != -1 ) {
				System.out.println( "Response code is: " + responseCode );
			}
			String responseMsg = conn.getResponseMessage();
			if ( responseMsg != null ) {
				System.out.println( "Response message is: " + responseMsg );
			}
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		BufferedReader br = null;
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
			while ( ( line = br.readLine() ) != null ) {
				sb.append( line );
			}
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		
		if ( br != null ) {
			try {
				br.close();
			} catch ( IOException ioe ) {
				ioe.printStackTrace();
			}
		}
		
		return sb.toString();
	}
	
	
	private String getBaseURL( Query q ) {
		AlgorithmAttributes aa = q.getAlgorithmEnvironment().getAlgorithmAttributes();
		ContainerAttributes ca = q.getAlgorithmEnvironment().getContainerAttributes();
		return "http://" + ca.getNetworkAlias() + ":" + ca.getQueryPort().getContainerPort() + aa.getQueryRoute();
	}
	
	
	/**
	 * Gets an HTTP connection specific to the algorithm environment {@code ae}.
	 * 
	 * There is currently no specification for Clef algorithm containers to accept GET requests,
	 * or for this service to process responses to such requests. This method creates 
	 * an HTTP GET request with Content-Type: application/xml.
	 * 
	 * @since 1.0.0
	 * @param ae the algorithm container to which a request should be proxied.
	 * @return
	 */
	private HttpURLConnection getConnection( Query q ) {
		
		URL url = null;
		HttpURLConnection conn = null;
		String parameters = "";
		try {
			// Get the parameter string for this algorithm environment.
			parameters = getParameterString( q );
			// Construct a URL of the form http://algorithm-container-network-alias:queryPort[?param=value&param=value...]
			url = new URL( this.getBaseURL( q ) + parameters );
		} catch ( MalformedURLException mue ) {
			mue.printStackTrace();
		} catch ( UnsupportedEncodingException uee ) {
			uee.printStackTrace();
		}
		
		if ( url != null ) {
			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch ( IOException ioe ) {
				ioe.printStackTrace();
			}
		}
		
		// This will be a GET request.
		try {
			conn.setRequestMethod( "GET" );
		} catch ( ProtocolException pe ) {
			pe.printStackTrace();
		}
		
		// Declare that this request will have an XML payload.
		try {
			conn.setRequestProperty( "Content-Type", "application/xml" );
		} catch ( IllegalStateException ise ) {
			ise.printStackTrace();
		} catch ( NullPointerException npe ) {
			npe.printStackTrace();
		}
		
		return conn;
	}
	
	
	/**
	 * 
	 * @since 1.0.0
	 * @param q
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String getParameterString( Query q ) throws UnsupportedEncodingException {
		
		AlgorithmAttributes aa = q.getAlgorithmEnvironment().getAlgorithmAttributes();
		List<Parameter> params = aa.getParameters();
		List<String> kvpairs = new LinkedList<String>();
		
		// Assume for now that we are working with a monophonic query.
		MonophonicQuery mq = (MonophonicQuery) q;
		
		for ( Parameter p : params ) {
			if ( p.getValue() == null ) {
				continue;
			}
			
			StringBuilder paramString = new StringBuilder();
			
			paramString.append( URLEncoder.encode( p.getKey(), "UTF-8" ) );
			paramString.append( "=" );
			paramString.append( URLEncoder.encode( p.getValue().toString(), "UTF-8" ) );
			kvpairs.add( paramString.toString() );
		}
		
		// Suffix the staff index parameter. This parameter provides a direct way for monophonic queries to 
		// indicate which staff of the MusicXML contains the query input notation.
		String staffIdxParam = "staffIdx=" +  mq.getStaffIndex();
		kvpairs.add( staffIdxParam );
		
		// Return the ? URL delimiter followed by the kvpairs, joined by the & character.
		return String.format( "?%s", String.join( "&", kvpairs ) );
	}
	
	
	
	
	/**
	 * 
	 * @since 1.0.0
	 * @param ae
	 * @param musicxml
	 * @return 
	 */
	public String query( Query q ) {
	
		HttpURLConnection conn = this.getConnection( q );
		this.sendRequest( conn, q.getMusicXML() );
		
		return this.extractResponse( conn );
	}
	
	
	/**
	 * Adds a parameter string to a connection and makes an HTTP request.
	 * 
	 * @see URLConnection#getOutputStream()
	 * 
	 * @param conn
	 * @param paramString
	 */
	private void sendRequest( HttpURLConnection conn, String musicxml ) {
		
		conn.setDoOutput( true );
		
		DataOutputStream dos = null;
		
		// Write the MusicXML string to the connection and send the request.
		try {
			dos = new DataOutputStream( conn.getOutputStream() );
			
			dos.writeBytes( musicxml );
			
			dos.flush();
			dos.close();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		System.out.println( "Successfully sent request.");
	}
}
