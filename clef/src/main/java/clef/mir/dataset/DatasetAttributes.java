package clef.mir.dataset;

import clef.mir.MusicFormat;

import java.util.Map;
import java.util.HashMap;

/**
 * Class that represents attributes of a symbolic music dataset.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class DatasetAttributes {

	private Map<String, Object> capabilities;
	private String displayName;
	private MusicFormat format;
	private int itemCount;
	private String name;
	
	public DatasetAttributes() {
		this.capabilities = new HashMap<String, Object>();
	}
	
	
	/**
	 * 
	 * @return capabilities map with keys "monophonic" and "polyphonic"
	 * @since 1.0.0
	 */
	public Map<String, Object> getCapabilities() {
		return capabilities;
	}
	
	
	/**
	 * 
	 * @return human-friendly display name of this Dataset
	 * @since 1.0.0
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	
	/**
	 * 
	 * @return the symbolic format of the music in this Dataset, e.g. humdrum
	 * @since 1.0.0
	 */
	public String getFormat() {
		return format.toString();
	}
	
	
	/**
	 * 
	 * @return the number of items in this Dataset
	 * @since 1.0.0
	 */
	public int getItemCount() {
		return itemCount;
	}
	
	
	/**
	 * 
	 * @return machine-friendly name of this Dataset
	 * @since 1.0.0
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public boolean isMonophonic() {
		return (Boolean) this.capabilities.get( "monophonic" );
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public boolean isPolyphonic() {
		return (Boolean) this.capabilities.get( "polyphonic" );
	}
	
	
	/**
	 * 
	 * @param caps Keys should be "monophonic" and "polyphonic"
	 * @since 1.0.0
	 */
	public void setCapabilities( Map<String,Object> caps ) {
		this.capabilities = caps;
	}
	
	
	/**
	 * 
	 * @param dname human-friendly name of this Dataset
	 * @since 1.0.0
	 */
	public void setDisplayName( String dname ) {
		this.displayName = dname;
	}
	
	
	/**
	 * 
	 * @param mf symbolic format of the music in this Dataset, e.g. humdrum
	 * @see MusicFormat
	 * @since 1.0.0
	 */
	public void setFormat( String mf ) {
		this.format = MusicFormat.valueOf( mf.toUpperCase() );
	}
	
	
	/**
	 * 
	 * @param count the number of items in this Dataset
	 * @since 1.0.0
	 */
	public void setItemCount( int count ) {
		this.itemCount = count;
	}
	
	
	/**
	 * 
	 * @param n machine-friendly name of this Dataset
	 * @since 1.0.0
	 */
	public void setName( String n ) {
		this.name = n;
	}
	
	
	/**
	 * 
	 * @param m whether this Dataset contains monophonic music
	 * @since 1.0.0
	 */
	public void setMonophonic( boolean m ) {
		this.capabilities.put( "monophonic", m );
	}
	
	
	/**
	 * 
	 * @param p whether this Dataset contains polyphonic music
	 * @since 1.0.0
	 */
	public void setPolyphonic( boolean p ) {
		this.capabilities.put( "polyphonic", p );
	}
}
