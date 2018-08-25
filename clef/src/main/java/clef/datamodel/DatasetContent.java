package clef.datamodel;

public class DatasetContent extends ClefDataObject {

	private int id;
	private String collection;
	private String datasetName;
	private String filename;
	
	public DatasetContent( String coll, String dset, String fname ) {
		this.collection = coll;
		this.datasetName = dset;
		this.filename = fname;
	}
	
	public int getId() {
		return id;
	}
	
	public String getCollection() {
		return collection;
	}
	
	public String getDatasetName() {
		return datasetName;
	}
	
	public String getFilename() {
		return filename;
	}
	
	
	public void setId( int id ) {
		this.id = id;
	}
	
	public void setCollection( String coll ) {
		this.collection = coll;
	}
	
	public void setDatasetName( String dset ) {
		this.datasetName = dset;
	}
	
	public void setFilename( String fname ) {
		this.filename = fname;
	}

}
