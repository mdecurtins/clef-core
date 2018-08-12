package clef.mir.dataset;

import java.util.List;
import java.util.function.Predicate;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import clef.utility.CheckedFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Dataset {

	private DatasetAttributes da;
	private String collection;
	private List<String> tags;
	
	@JsonIgnore
	private String parentDirectory;
	
	@JsonIgnore
	private static final Predicate<Path> isClefdatasetFile = path -> path.toAbsolutePath().toString().endsWith( "clefdataset.json" );
	
	@JsonIgnore
	private static final CheckedFunction<Path, Dataset> createDataset = path -> Dataset.fromFile( path );
	
	public Dataset() {
		this.collection = "";
		this.parentDirectory = "";
		this.tags = new ArrayList<String>();
	}
	
	public static Dataset fromFile( Path path ) throws IOException {
		Dataset dset = null;
		ObjectMapper mapper = new ObjectMapper();
		dset = mapper.readValue( Files.newInputStream( path ), Dataset.class );		
		return dset;
	}
	
	public void addTag( String tag ) {
		this.tags.add( tag );
	}
	
	public String getCollection() {
		return collection;
	}
	
	public DatasetAttributes getDatasetAttributes() {
		return da;
	}
	
	@JsonProperty
	public static CheckedFunction<Path, Dataset> getCreatorFunction() {
		return createDataset;
	}
	
	@JsonProperty
	public String getParentDirectory() {
		return parentDirectory;
	}
	
	@JsonProperty
	public static Predicate<Path> getPredicate() {
		return isClefdatasetFile;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setDatasetAttributes( DatasetAttributes datatt ) {
		this.da = datatt;
	}
	
	public void setCollection( String coll ) {
		this.collection = coll;
	}
	
	@JsonIgnore
	public void setParentDirectory( String dir ) {
		this.parentDirectory = dir;
	}
	
	public void setTags( List<String> t ) {
		this.tags = t;
	}
	
}
