package clef.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import clef.datamodel.Composer;
import clef.datamodel.Work;

import java.util.List;

/**
 * Class representing a single result returned from a MIR algorithm.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClefResult extends ClefItem {

	private Composer composer;
	private int ranking;
	private List<String> tags;
	private Work work;

	public Composer getComposer() {
		return composer;
	}
	
	public int getRanking() {
		return ranking;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public Work getWork() {
		return work;
	}
	
	public void setComposer( Composer c ) {
		this.composer = c;
	}
	
	public void setRanking( int r ) {
		this.ranking = r;
	}
	
	public void setTags( List<String> t ) {
		this.tags = t;
	}
	
	public void setWork( Work w ) {
		this.work = w;
	}
}
