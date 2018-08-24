package clef.mir;

import java.util.Map;

public class Result {

	private int id;
	private String datasetName;
	private String filename;
	private Map<String, Object> properties;
	
	public String getDatasetName() {
		return datasetName;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public int getID() {
		return id;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void setDatasetName( String dset ) {
		this.datasetName = dset;
	}
	
	public void setFilename( String f ) {
		this.filename = f;
	}
	
	public void setId( int i ) {
		this.id = i;
	}
	
	public void setProperties( Map<String, Object> props ) {
		this.properties = props;
	}
}
