package clef.mir.clefinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clef.common.ClefException;

public class InputCapabilities {

	private boolean monophonic;
	private boolean polyphonic;
	private Map<String, Integer> querySize;
	
	public InputCapabilities() {
		this.monophonic = false;
		this.polyphonic = false;
		this.querySize = new HashMap<String, Integer>();
	}
	
	public boolean acceptsMonophonicInput() {
		return monophonic;
	}
	
	public boolean acceptsPolyphonicInput() {
		return polyphonic;
	}
	
	public int getQuerySizeMax() {
		int max = 0;
		if ( this.querySize.get( "max" ) != null ) {
			Integer m = this.querySize.get( "max" );
			max = m.intValue();
		}
		return max;
	}
	
	public int getQuerySizeMin() {
		int min = 0;
		if ( this.querySize.get( "min" ) != null ) {
			Integer m = this.querySize.get( "min" );
			min = m.intValue();
		}
		return min;
	}
	
	public Map<String, Integer> getQuerySizeRange() {
		return querySize;
	}
	
	public void setMonophonic( boolean m ) {
		this.monophonic = m;
	}
	
	public void setPolyphonic( boolean p ) {
		this.polyphonic = p;
	}
	
	public void setQuerySizeRange( Map<String, Integer> qs ) throws ClefException {
		List<String> allowedKeys = new ArrayList<String>();
		allowedKeys = Arrays.asList( "min", "max" );
		
		
		boolean diff = false;
		for ( String key : qs.keySet() ) {
			if ( ! allowedKeys.contains( key ) ) {
				diff = true;
			}
		}
		if ( diff ) {
			throw new ClefException( "Invalid keys passed, expecting \"min\" and \"max\"." );
		} else {
			this.querySize = qs;
		}
	}
}
