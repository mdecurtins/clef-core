package clef.mir;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;
import java.util.Map;

import clef.mir.clefinfo.Parameter;

public class SearchServiceImpl implements SearchService {

	
	/**
	 * 
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
	 * 
	 * @param ae
	 * @return
	 */
	private HttpURLConnection getConnection( AlgorithmEnvironment ae ) {
		AlgorithmAttributes aa = ae.getAlgorithmAttributes();
		ContainerAttributes ca = ae.getContainerAttributes();
		URL url = null;
		HttpURLConnection conn = null;
		try {
			// Construct a URL of the form http://algorithm-container-network-alias:queryPort
			url = new URL( "http://" + ca.getNetworkAlias() + ":" + ca.getQueryPort().getPort() + aa.getQueryRoute() );
		} catch ( MalformedURLException mue ) {
			mue.printStackTrace();
		}
		
		if ( url != null ) {
			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch ( IOException ioe ) {
				ioe.printStackTrace();
			}
		}
		
		try {
			conn.setRequestMethod( "POST" );
		} catch ( ProtocolException pe ) {
			pe.printStackTrace();
		}
		
		return conn;
	}
	
	
	/**
	 * 
	 * @param q
	 * @param aa
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getParameterString( String q, AlgorithmAttributes aa ) throws UnsupportedEncodingException {
		
		List<Parameter> params = aa.getParameters();
		
		StringBuilder paramString = new StringBuilder();
		
		// Append the URL path/parameter delimiter.
		paramString.append( "?" );
		
		// Append the query parameter.
		paramString.append( "q=" );
		paramString.append( q );
		
		for ( Parameter p : params ) {
			if ( p.getValue() == null ) {
				continue;
			}
			paramString.append( "&" );
			paramString.append( URLEncoder.encode( p.getKey(), "UTF-8") );
			paramString.append( "=" );
			paramString.append( URLEncoder.encode( p.getValue().toString(), "UTF-8" ) );
		}
		
		return paramString.toString();
	}
	
	
	private void printConnectionProperties( HttpURLConnection conn ) {
		Map<String, List<String>> props = conn.getRequestProperties();
		System.out.println( "Connection Properties:" );
		for ( Map.Entry<String, List<String>> prop : props.entrySet() ) {
			System.out.println( "Property: " + prop.getKey() );
			System.out.println( "Values:" );
			for ( String val : prop.getValue() ) {
				System.out.println( "\t" + val );
			}
		}
	}
	
	/**
	 * 
	 * @param q
	 * @param ae
	 * @return
	 */
	public String query( String q, AlgorithmEnvironment ae ) {
	
		HttpURLConnection conn = this.getConnection( ae );
		
		String parameters = "";
		try {
			parameters = getParameterString( q, ae.getAlgorithmAttributes() );
			System.out.println( "Sending request to: " + conn.getURL().toExternalForm() + parameters );
			this.sendRequest( conn, parameters );
			
		} catch ( UnsupportedEncodingException uee ) {
			uee.printStackTrace();
		}
		
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
	private void sendRequest( HttpURLConnection conn, String paramString ) {
		
		conn.setDoOutput( true );
		
		DataOutputStream dos = null;
		
		// Write the parameters string to the connection and send the request.
		try {
			dos = new DataOutputStream( conn.getOutputStream() );
			
			dos.writeBytes( paramString );
			
			dos.flush();
			dos.close();
		} catch ( IOException ioe ) {
			
			ioe.printStackTrace();
		}
		
	}
}
