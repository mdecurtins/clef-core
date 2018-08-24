package clef.datamodel;

/**
 * Class that represents the relationship between a tag and a musical work.
 * 
 * In this representation, the tag is the 'primary' object, and the work is the 
 * object to which it's related.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class TagRelation extends Relation {

	private int tagId;
	private int workId;
	
	public TagRelation( int tId, int wId ) {
		this.tagId = tId;
		this.workId = wId;
	}
	
	public int getRelationId() {
		return tagId;
	}
	
	public int getRelatedToId() {
		return workId;
	}
	
	public void setRelationId( int tagId ) {
		this.tagId = tagId;
	}
	
	public void setRelatedToId( int workId ) {
		this.workId = workId;
	}
	
}
