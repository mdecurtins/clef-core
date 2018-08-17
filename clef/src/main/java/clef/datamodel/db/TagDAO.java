package clef.datamodel.db;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import java.util.List;
import java.util.ArrayList;

import clef.datamodel.Tag;
import clef.datamodel.metadata.Metadata;

public class TagDAO extends ClefDAO {

	private static BiPredicate<Tag, Tag> matchTagValue = ( t1, t2 ) -> t1.getValue().equals( t2.getValue() );
	private static BiPredicate<Tag, Metadata> matchTag = ( t, m ) -> m.getTags().contains( t );
	//private static BiConsumer<Tag, Metadata> updateTag = 
	
	public int batchInsert( List<Tag> tags ) {
		return 0;
	}
	
	public static BiPredicate<Tag, Metadata> getMatchingPredicate() {
		return matchTag;
	}
	
	public List<Tag> mapFromMetadata( List<Metadata> meta ) {
		List<Tag> tags = new ArrayList<Tag>();
		for ( Metadata m : meta ) {
			for ( Tag tag : m.getTags() ) {
				tags.add( tag );
			}
		}
		return tags;
	}
	
	public List<Tag> selectAll() {
		List<Tag> tags = new ArrayList<Tag>();
		return tags;
	}
	
	public void updateMetadata( List<Metadata> meta, List<Tag> tags ) {
		
	}
	
	/**
	 * 
	 * @param t a fully-populated Tag with an ID that has been returned from the database
	 * @param m
	 * @return
	 * @since 1.0.0
	 */
	private static Metadata updateSingleTag( Tag t, Metadata m ) {
		List<Tag> tags = m.getTags();
		for ( int i = 0; i < tags.size(); i++ ) {
			Tag aTag = tags.get( i );
			if ( matchTagValue.test( t, aTag ) ) {
				tags.set( i, t );
			}
		}
		return m;
	}
}
