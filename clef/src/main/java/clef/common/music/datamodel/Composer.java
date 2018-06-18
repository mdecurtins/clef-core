package clef.common.music.datamodel;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Composer {

	private String name;
	private Map<String, String> dates;
	
	public Composer() {
		this.dates = new HashMap<String, String>();
	}
	
	public Composer( String name ) {
		this.name = name;
		this.dates = new HashMap<String, String>();
	}
	
	public Composer( String name, String born, String died ) {
		this.name = name;
		this.dates = new HashMap<String, String>();
		this.setDates( born, died );
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, String> getDates() {
		return dates;
	}
	
	public void setDates( String born, String died ) {
		this.dates.put( "born", born );
		this.dates.put( "died", died );
	}
	
	public void setName( String n ) {
		this.name = n;
	}
	
	
}
