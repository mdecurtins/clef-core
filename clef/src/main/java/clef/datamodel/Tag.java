package clef.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag extends Single {

	@JsonIgnore
	private int id;
	private String tag;
	
	public Tag( String t ) {
		this.tag = t;
	}
	
	public Tag( int id, String t ) {
		this.id = id;
		this.tag = t;
	}
	
	@JsonIgnore
	public int getId() {
		return id;
	}
	
	public String getValue() {
		return tag;
	}
	
	@JsonProperty
	public void setId( int id ) {
		this.id = id;
	}
}
