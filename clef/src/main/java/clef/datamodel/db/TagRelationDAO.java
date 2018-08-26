package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clef.datamodel.Tag;
import clef.datamodel.TagRelation;
import clef.datamodel.metadata.Metadata;

/**
 * Data access class for {@link TagRelation} objects.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class TagRelationDAO extends ClefDAO {
	
	private static final Logger logger = LoggerFactory.getLogger( TagRelationDAO.class );

	
	/**
	 * Performs a batch insert of rows into the tag_relations table.
	 * 
	 * @param meta
	 * @return
	 * @since 1.0.0
	 */
	public int batchInsert( List<Metadata> meta ) {
		int retval = 0;
		String sql = "INSERT INTO tag_relations ( work_id, tag_id ) VALUES ( ?, ? ) ON DUPLICATE KEY UPDATE id = id;";
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			PreparedStatement ps = this.prepare( conn, sql, AutoCommit.OFF );
			
			for ( Metadata m : meta ) {
				for ( Tag t : m.getTags() ) {
					ps.setInt( 1, m.getWork().getId() );
					ps.setInt( 2, t.getId() );
					ps.addBatch();
				}
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
	 * Selects all tag-work relations.
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<TagRelation> selectAll() {
		List<TagRelation> trs = new ArrayList<TagRelation>();
		String sql = "SELECT * FROM tag_relations;";
		Database db = new Database();
		ResultSet rs = db.select( sql );
		try {
			while ( rs.next() ) {
				TagRelation tr = new TagRelation( rs.getInt( "id" ), rs.getInt( "tag_id" ), rs.getInt( "work_id" ) );
				trs.add( tr );
			}
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		}
		return trs;
	}
}
