package clef.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WorkType extends Single {

	@JsonIgnore
	private int id;
	private String workType;
	
	public WorkType( String wt ) {
		this.workType = wt;
	}
	
	public WorkType( int id, String wt ) {
		this.id = id;
		this.workType = wt;
	}
	
	@JsonIgnore
	public int getId() {
		return id;
	}
	
	public String getValue() {
		return workType;
	}
	
}
