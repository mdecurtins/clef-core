package clef.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Era extends Single {

	@JsonIgnore
	private int id;
	private String era;
	
	public Era( String era ) {
		this.era = era;
	}
	
	public Era( int id, String era ) {
		this.id = id;
		this.era = era;
	}
	
	@JsonIgnore
	public int getId() {
		return id;
	}
	
	public String getValue() {
		return era;
	}
}
