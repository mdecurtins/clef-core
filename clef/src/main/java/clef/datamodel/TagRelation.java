package clef.datamodel;

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
	
}
