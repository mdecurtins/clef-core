package clef.mir;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;

import clef.mir.clefinfo.Parameter;

public class SearchServiceImpl implements SearchService {

	
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
	
	
	/**
	 * Gets an HTTP connection specific to the algorithm environment {@code ae}.
	 * 
	 * There is currently no specification for Clef algorithm containers to accept GET requests,
	 * or for this service to process responses to such requests. This method creates 
	 * an HTTP POST request with Content-Type: application/xml.
	 * 
	 * @since 1.0.0
	 * @param ae the algorithm container to which a request should be proxied.
	 * @return
	 */
	private HttpURLConnection getConnection( AlgorithmEnvironment ae ) {
		AlgorithmAttributes aa = ae.getAlgorithmAttributes();
		ContainerAttributes ca = ae.getContainerAttributes();
		URL url = null;
		HttpURLConnection conn = null;
		String parameters = "";
		try {
			// Get the parameter string for this algorithm environment.
			parameters = getParameterString( ae.getAlgorithmAttributes() );
			// Construct a URL of the form http://algorithm-container-network-alias:queryPort[?param=value&param=value...]
			url = new URL( "http://" + ca.getNetworkAlias() + ":" + ca.getQueryPort().getPort() + aa.getQueryRoute() + parameters );
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
		
		// This will be a POST request.
		try {
			conn.setRequestMethod( "POST" );
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
	 * @param aa
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String getParameterString( AlgorithmAttributes aa ) throws UnsupportedEncodingException {
		
		List<Parameter> params = aa.getParameters();
		
		StringBuilder paramString = new StringBuilder();
		
		// Append the URL path/parameter delimiter.
		paramString.append( "?" );
		
		boolean first = true;
		for ( Parameter p : params ) {
			if ( p.getValue() == null ) {
				continue;
			}
			if ( ! first ) {
				paramString.append( "&" );
			}
			paramString.append( URLEncoder.encode( p.getKey(), "UTF-8" ) );
			paramString.append( "=" );
			paramString.append( URLEncoder.encode( p.getValue().toString(), "UTF-8" ) );
			first = false;
		}
		
		// If there are no parameters, paramString will consist of a lonely "?" only.
		return ( paramString.toString().equals( "?" ) ) ? "" : paramString.toString();
	}
	
	
	/**
	 * 
	 * @since 1.0.0
	 * @param ae
	 * @param musicxml
	 * @return 
	 */
	public String query( AlgorithmEnvironment ae, String musicxml ) {
	
		HttpURLConnection conn = this.getConnection( ae );
		this.sendRequest( conn, musicxml );
		
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
		
	}
}
