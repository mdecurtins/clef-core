package clef.mir.dataset;

import clef.mir.MusicFormat;

import java.util.Map;
import java.util.HashMap;

public class DatasetAttributes {

	private Map<String, Object> capabilities;
	private String displayName;
	private MusicFormat format;
	private int itemCount;
	private String name;
	
	public DatasetAttributes() {
		this.capabilities = new HashMap<String, Object>();
	}
	
	public Map<String, Object> getCapabilities() {
		return capabilities;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getFormat() {
		return format.toString();
	}
	
	public int getItemCount() {
		return itemCount;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isMonophonic() {
		return (Boolean) this.capabilities.get( "monophonic" );
	}
	
	public boolean isPolyphonic() {
		return (Boolean) this.capabilities.get( "polyphonic" );
	}
	
	public void setCapabilities( Map<String,Object> caps ) {
		this.capabilities = caps;
	}
	
	public void setDisplayName( String dname ) {
		this.displayName = dname;
	}
	
	public void setFormat( String mf ) {
		this.format = MusicFormat.valueOf( mf.toUpperCase() );
	}
	
	public void setItemCount( int count ) {
		this.itemCount = count;
	}
	
	public void setName( String n ) {
		this.name = n;
	}
	
	public void setMonophonic( boolean m ) {
		this.capabilities.put( "monophonic", m );
	}
	
	public void setPolyphonic( boolean p ) {
		this.capabilities.put( "polyphonic", p );
	}
}
