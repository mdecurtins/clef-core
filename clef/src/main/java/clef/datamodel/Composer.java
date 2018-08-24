package clef.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Composer extends ClefDataObject {

	@JsonIgnore
	private int id;
	private String name;
	private Map<String, Integer> dates;
	
	@JsonIgnore
	private static final Logger logger = LoggerFactory.getLogger( Composer.class );
	
	public Composer() {
		this.dates = new HashMap<String, Integer>();
	}
	
	public Composer( String name ) {
		this.name = name;
		this.dates = new HashMap<String, Integer>();
	}
	
	public Composer( String name, String born, String died ) {
		this.name = name;
		this.dates = new HashMap<String, Integer>();
		this.setDates( born, died );
	}
	
	public Composer( int id, String name, String born, String died ) {
		this.id = id;
		this.name = name;
		this.dates = new HashMap<String, Integer>();
		this.setDates( born, died );
	}
	
	public Composer( int id, String name, int born, int died ) {
		this.id = id;
		this.name = name;
		this.dates = new HashMap<String, Integer>();
		this.setDates( born, died );
	}
	
	public int born() {
		Integer born = this.dates.get( "born" );
		if ( born != null ) {
			return born;
		}
		return -1;
	}
	
	public int died() {
		Integer died = this.dates.get( "died" );
		if ( died != null ) {
			return died;
		}
		return -1;
	}
	
	@JsonIgnore
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	
	public Map<String, Integer> getDates() {
		return dates;
	}
	
	public void setDates( String born, String died ) {
		Integer b = Integer.parseInt( born );
		Integer d = Integer.parseInt( died );
		this.dates.put( "born", b );
		this.dates.put( "died", d );
	}
	
	public void setDates( int born, int died ) {
		this.dates.put( "born", born );
		this.dates.put( "died", died );
	}
	
	public void setName( String n ) {
		this.name = n;
	}
	
	
}
