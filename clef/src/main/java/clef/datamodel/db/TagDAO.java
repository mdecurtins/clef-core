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
	private static BiConsumer<Tag, Metadata> updateTag = ( t, m ) -> updateSingleTag( t, m );
	
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
		for ( Tag t : tags ) {
			for ( Metadata m : meta ) {
				if ( matchTag.test( t, m ) ) {
					updateTag.accept( t, m );
				}
			}
		}
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
