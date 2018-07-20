package clef.mir.dataset;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Dataset {

	private DatasetAttributes da;
	private String collection;
	private List<String> tags;
	
	public Dataset() {
		this.collection = "";
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
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setDatasetAttributes( DatasetAttributes datatt ) {
		this.da = datatt;
	}
	
	public void setCollection( String coll ) {
		this.collection = coll;
	}
	
	public void setTags( List<String> t ) {
		this.tags = t;
	}
	
}
