package clef.datamodel.metadata;


import java.util.List;

import clef.datamodel.*;

/**
 * Transport class for metadata when parsing from a file.
 * 
 *
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class Metadata {

	private Composer c;
	private DatasetContent dc;
	private Era era;
	private List<Tag> tags;
	private Work w;
	private WorkType wt;

	public Composer getComposer() {
		return c;
	}
	
	public DatasetContent getDatasetContent() {
		return dc;
	}
	
	public Era getEra() {
		return era;
	}
	
	public List<Tag> getTags() {
		return tags;
	}
	
	public Work getWork() {
		return w;
	}
	
	public WorkType getWorkType() {
		return wt;
	}
	
	public void setComposer( Composer c ) {
		this.c = c;
	}
	
	public void setDatasetContent( DatasetContent dc ) {
		this.dc = dc;
	}
	
	public void setEra( Era era ) {
		this.era = era;
	}
	
	public void setTags( List<Tag> tags ) {
		this.tags = tags;
	}
	
	public void setWork( Work w ) {
		this.w = w;
	}
	
	public void setWorkType( WorkType wt ) {
		this.wt = wt;
	}
	
	
}
