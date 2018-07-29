package clef.mir;

import clef.common.ClefException;
import clef.mir.dataset.Dataset;
import clef.mir.xml.MusicXMLUtils;

public class MonophonicQuery extends Query {

	private int staffIdx;
	
	public MonophonicQuery() {
		super();
	}
	
	public boolean containsChords() {
		return MusicXMLUtils.containsChords( this.musicxml );
	}
	
	public int getStaffIndex() {
		return staffIdx;
	}
	
	public static MonophonicQuery newInstance( AlgorithmEnvironment ae, String musicxml, Dataset dset ) throws ClefException {
		MonophonicQuery instance = new MonophonicQuery();
		instance.setAlgorithmEnvironment( ae );
		instance.setMusicXML( musicxml );
		instance.addDataset( dset );
		return instance;
	}
	
	public int queryLength() {
		int length = 0;
		
		return length;
	}
	
	public void setMusicXML( String xml ) throws ClefException {
		this.musicxml = xml;
		if ( ! MusicXMLUtils.musicIsOnOneStaff(this.musicxml) ) {
			throw new ClefException( "Music is not on one staff." );
		}
		this.staffIdx = MusicXMLUtils.getMonophonicInputStaffIndex( this.musicxml );
	}
}
