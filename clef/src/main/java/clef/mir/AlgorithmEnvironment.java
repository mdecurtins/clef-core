package clef.mir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

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

}
