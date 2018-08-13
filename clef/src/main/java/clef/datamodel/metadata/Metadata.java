package clef.datamodel.metadata;

import java.util.function.BiPredicate;

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
	private Work w;
	private WorkType wt;
	
	// Predicates
	private static BiPredicate<Composer, Metadata> matchComposer = ( composer, m ) -> composer.getName().equals( m.getComposer().getName() );
	private static BiPredicate<Era, Metadata> matchEra = ( era, m ) -> era.getValue().equals( m.getEra().getValue() ); 
	private static BiPredicate<Work, Metadata> matchWork = ( w, m ) -> w.getTitle().equals( m.getWork().getTitle() ) &&
																	   w.getCatalog().equals( m.getWork().getCatalog() ) && 
																	   w.getCatalogNumber().equals( m.getWork().getCatalogNumber() );
	private static BiPredicate<WorkType, Metadata> matchWorkType = ( wt, m ) -> wt.getValue().equals( m.getWorkType().getValue() );
	
	// Functions
	
	
	public Composer getComposer() {
		return c;
	}
	
	public DatasetContent getDatasetContent() {
		return dc;
	}
	
	public Era getEra() {
		return era;
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
	
	public void setWork( Work w ) {
		this.w = w;
	}
	
	public void setWorkType( WorkType wt ) {
		this.wt = wt;
	}
	
	
}
