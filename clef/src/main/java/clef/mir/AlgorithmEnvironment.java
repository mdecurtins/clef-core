package clef.mir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import clef.mir.clefinfo.Parameter;

public class AlgorithmEnvironment {

	private AlgorithmAttributes aa;
	private ContainerAttributes ca;
	private String image;
	
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static AlgorithmEnvironment fromFile( Path path ) throws IOException {
		System.out.println( "AlgorithmEnvironment: Constructing from file: " + path.toString() );
		AlgorithmEnvironment ae = null;
		ObjectMapper mapper = new ObjectMapper();
		ae = mapper.readValue( Files.newInputStream( path ), AlgorithmEnvironment.class );
		
		return ae;
	}

	
	/**
	 * 
	 * @return
	 */
	public AlgorithmAttributes getAlgorithmAttributes() {
		return aa;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public ContainerAttributes getContainerAttributes() {
		return ca;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getImageName() {
		return image;
	}
	
	
	/**
	 * 
	 * @param aa
	 */
	public void setAlgorithmAttributes( AlgorithmAttributes aa ) {
		this.aa = aa;
	}
	
	
	/**
	 * 
	 * @param ca
	 */
	public void setContainerAttributes( ContainerAttributes ca ) {
		this.ca = ca;
	}
	
	/**
	 * 
	 * @param img
	 */
	@JsonProperty("image")
	public void setImage( Map<String, Object> img ) {
		this.image = (String) img.get( "name" );
	}
	
	public void print() {
		System.out.println( "Algorithm Attributes:" );
		System.out.println( "Name: " + this.aa.getName() );
		System.out.println( "Display Name: " + this.aa.getDisplayName() );
		System.out.println( "Query Route: " + this.aa.getQueryRoute() );
		System.out.println( "Type: " + this.aa.getType() );
		System.out.println( "Dataset Capabilities: " );
		System.out.println( "Monophonic: " + this.aa.getDatasetCapabilities().isMonophonicData() );
		System.out.println( "Polyphonic: " + this.aa.getDatasetCapabilities().isPolyphonicData() );
		System.out.println( "Allowed File Types: " + this.aa.getDatasetCapabilities().getAllowedFileTypes() );
		System.out.println( "Input Capabilities:" );
		System.out.println( "Monophonic: " + this.aa.getInputCapabilities().acceptsMonophonicInput() );
		System.out.println( "Polyphonic: " + this.aa.getInputCapabilities().acceptsPolyphonicInput() );
		System.out.println( "Query Size Range: Min: " + this.aa.getInputCapabilities().getQuerySizeMin() + " Max: " + this.aa.getInputCapabilities().getQuerySizeMax());
		System.out.println( "Parameters:" );
		for ( Parameter p : this.aa.getParameters() ) {
			System.out.println( "Description: " + p.getDescription() );
			System.out.println( "Name: " + p.getName() );
			System.out.println( "Keyname: " + p.getKey() );
			System.out.println( "Type: " + p.getType() );
		}
		System.out.println( "Container Attributes: " );
		System.out.println( "Name: " + this.ca.getName() );
		System.out.println( "Network Alias: " + this.ca.getNetworkAlias() );
		System.out.println( "Query port: " + this.ca.getQueryPort().getPort() );
	}
}
