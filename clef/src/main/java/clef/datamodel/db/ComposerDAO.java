package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;


import clef.datamodel.Composer;
import clef.datamodel.metadata.Metadata;

public class ComposerDAO extends ClefDAO {

	private static BiPredicate<Composer, Metadata> matchComposer = ( composer, m ) -> composer.getName().equals( m.getComposer().getName() );
	private static BiConsumer<Composer, Metadata> updateComposer = ( composer, m ) -> m.setComposer( composer );
	
	public int batchInsert( List<Composer> composers ) {
		int retval = 0;
		String sql = "INSERT INTO composers VALUES ( ?, ?, ? ) ON DUPLICATE KEY UPDATE id = id;";
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			PreparedStatement ps = this.prepare( conn, sql, AutoCommit.OFF );
			for ( Composer c : composers ) {
				ps.setString( 1, c.getName() );
				ps.setInt( 2, c.born() );
				ps.setInt( 3, c.died() );
				ps.addBatch();
			}
			int[] results = ps.executeBatch();
			retval = this.sumBatchInsert( results );
			conn.commit();
		} catch ( SQLException sqle ) {
			
		}
		return retval;
	}
	
	public static BiPredicate<Composer, Metadata> getMatchingPredicate() {
		return matchComposer;	
	}
	
	public static BiConsumer<Composer, Metadata> getUpdateConsumerFunction() {
		return updateComposer;
	}
	
	public List<Composer> mapFromMetadata( List<Metadata> meta ) {
		List<Composer> composers = new ArrayList<Composer>();
		for ( Metadata m : meta ) {
			composers.add( m.getComposer() );
		}
		return composers;
	}
	
	public List<Composer> selectAll() {
		List<Composer> allComposers = new ArrayList<Composer>();
		String sql = "SELECT * FROM composers;";
		Database db = new Database();
		ResultSet rs = db.select( sql );
		try {
			while ( rs.next() ) {
				
			}
			rs.close();
		} catch ( SQLException sqle ) {
			
		}
		return allComposers;
	}
	
	public void updateMetadata( List<Metadata> meta, List<Composer> composers ) {
		for ( Composer c : composers ) {
			for ( Metadata m : meta ) {
				if ( matchComposer.test( c, m ) ) {
					updateComposer.accept( c, m );
				}
			}
		}
	}
}
