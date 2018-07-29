package clef.mir;

import java.util.List;
import java.util.ArrayList;

import clef.common.ClefException;
import clef.mir.dataset.Dataset;

public abstract class Query {

	protected AlgorithmEnvironment ae;
	protected List<Dataset> datasets;
	protected String musicxml;
	
	public static final int MONOPHONIC = 1;
	public static final int POLYPHONIC = 2;
	
	protected Query() {
		this.datasets = new ArrayList<Dataset>();
	}
	
	protected void addDataset( Dataset dset ) {
		if ( ! this.datasets.contains( dset ) ) {
			this.datasets.add( dset );
		}
	}
	
	public AlgorithmEnvironment getAlgorithmEnvironment() {
		return ae;
	}
	
	public List<Dataset> getDatasets() {
		return datasets;
	}
	
	public String getMusicXML() {
		return musicxml;
	}
	
	
	public boolean queryRangeOk() {
		boolean ok = false;
		if ( this.ae != null && this.musicxml != null ) {
			
		}
		return ok;
	}
	
	
	public void setAlgorithmEnvironment( AlgorithmEnvironment ae ) {
		this.ae = ae;
	}
	
	public abstract void setMusicXML( String xml ) throws ClefException;
	
	
	
	
}
