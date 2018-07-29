package clef.mir;

import java.util.List;
import java.util.ArrayList;

public class PolyphonicQuery extends Query {

	private List<Integer> staffIdxs;
	
	public PolyphonicQuery() {
		this.staffIdxs = new ArrayList<Integer>();
	}
	
	public List<Integer> getStaffIndexes() {
		return staffIdxs;
	}
	
	public void setMusicXML( String xml ) {
		this.musicxml = xml;
	}
}
