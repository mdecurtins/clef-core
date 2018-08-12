package clef.mir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Map;
import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import clef.utility.CheckedFunction;

public class AlgorithmEnvironment {

	private AlgorithmAttributes aa;
	private ContainerAttributes ca;
	private String image;
	
	@JsonIgnore
	private static final Predicate<Path> isClefinfoFile = path -> path.toAbsolutePath().toString().endsWith( "clefinfo.json" );
	
	@JsonIgnore
	private static final CheckedFunction<Path, AlgorithmEnvironment> createAlgorithmEnvironment = path -> AlgorithmEnvironment.fromFile( path );
	
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
	
	
	@JsonProperty
	public static CheckedFunction<Path, AlgorithmEnvironment> getCreatorFunction() {
		return createAlgorithmEnvironment;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getImageName() {
		return image;
	}
	
	@JsonProperty
	public static Predicate<Path> getPredicate() {
		return isClefinfoFile;
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
