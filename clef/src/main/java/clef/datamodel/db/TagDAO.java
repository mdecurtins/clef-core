package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

import clef.datamodel.Tag;
import clef.datamodel.metadata.Metadata;

public class TagDAO extends ClefDAO {
	
	private static final Logger logger = LoggerFactory.getLogger( TagDAO.class );

	private static BiPredicate<Tag, Tag> matchTagValue = ( t1, t2 ) -> t1.getValue().equals( t2.getValue() );
	private static BiPredicate<Tag, Metadata> matchTag = ( t, m ) -> m.getTags().contains( t );
	private static BiConsumer<List<Tag>, Metadata> updateTags = ( tags, m ) -> m.setTags( getUpdatedTagList( tags, m ) );
	
	/**
	 * 
	 * @param tags
	 * @return
	 */
	public int batchInsert( List<Tag> tags ) {
		int retval = 0;
		String sql = "INSERT INTO tags ( tag ) VALUES ( ? ) ON DUPLICATE KEY UPDATE id = id;";
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			PreparedStatement ps = this.prepare( conn, sql, AutoCommit.OFF );
			for ( Tag t : tags ) {
				ps.setString( 1, t.getValue() );
				ps.addBatch();
			}
			int[] results = ps.executeBatch();
			retval = this.sumBatchInsert( results );
			conn.commit();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		}
		return retval;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static BiPredicate<Tag, Metadata> getMatchingPredicate() {
		return matchTag;
	}
	
	
	private static List<Tag> getUpdatedTagList( List<Tag> tags, Metadata m  ) {
		List<Tag> updated = new ArrayList<Tag>();
		// Iterate through all tags retrieved from the database
		for ( Tag t : tags ) {
			// Iterate through the tags of this Metadata instance
			for ( Tag tm : m.getTags() ) {
				// If the current tag value matches a tag value in the Metadata instance
				if ( matchTagValue.test( t, tm ) ) {
					// Add the updated tag to the list
					updated.add( t );
				}
			}
		}
		return updated;
	}
	
	
	/**
	 * 
	 * @param meta
	 * @return
	 */
	public List<Tag> mapFromMetadata( List<Metadata> meta ) {
		List<Tag> tags = new ArrayList<Tag>();
		for ( Metadata m : meta ) {
			for ( Tag tag : m.getTags() ) {
				tags.add( tag );
			}
		}
		return tags;
	}
	

	
	/**
	 * 
	 * @return
	 */
	public List<Tag> selectAll() {
		List<Tag> tags = new ArrayList<Tag>();
		String sql = "SELECT * FROM tags;";
		Database db = new Database();
		ResultSet rs = db.select( sql );
		try {
			while ( rs.next() ) {
				tags.add( new Tag( rs.getInt( "id" ), rs.getString( "tag" ) ) );
			}
			rs.close();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		}
		return tags;
	}
	
	
	/**
	 * 
	 * @param meta
	 * @param tags
	 */
	public void updateMetadata( List<Metadata> meta, List<Tag> tags ) {
		// For each Metadata instance, give it the list of all tags and 
		// then use getUpdatedTagList() to get the right updated tags, 
		// then replace the Metadata's entire list of tags with the new list
		for ( Metadata m : meta ) {
			updateTags.accept( tags, m );
		}
	}
	

}
