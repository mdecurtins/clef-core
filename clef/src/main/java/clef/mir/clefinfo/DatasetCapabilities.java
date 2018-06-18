package clef.mir.clefinfo;

import java.util.ArrayList;
import java.util.List;

public class DatasetCapabilities {

	private List<String> allowedFileTypes;
	private boolean monophonic;
	private boolean polyphonic;
	
	public DatasetCapabilities() {
		this.allowedFileTypes = new ArrayList<String>();
		this.monophonic = false;
		this.polyphonic = false;
	}
	
	public List<String> getAllowedFileTypes() {
		return allowedFileTypes;
	}
	
	public boolean isMonophonicData() {
		return monophonic;
	}
	
	public boolean isPolyphonicData() {
		return polyphonic;
	}
	
	public void setAllowedFileTypes( List<String> extensions ) {
		this.allowedFileTypes = extensions;
	}
	
	public void setMonophonic( boolean m ) {
		this.monophonic = m;
	}
	
	public void setPolyphonic( boolean p ) {
		this.polyphonic = p;
	}
}
