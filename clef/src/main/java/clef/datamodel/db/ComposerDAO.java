package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clef.datamodel.Composer;
import clef.datamodel.Work;
import clef.datamodel.metadata.Metadata;

public class ComposerDAO extends ClefDAO {

	private static final Logger logger = LoggerFactory.getLogger( ComposerDAO.class );
	
	private static BiPredicate<Composer, Metadata> matchComposer = ( composer, m ) -> composer.getName().equals( m.getComposer().getName() );
	private static BiConsumer<Composer, Metadata> updateComposer = ( composer, m ) -> doUpdate( composer, m );
	
	
	/**
	 * 
	 * @param composers
	 * @return
	 */
	public int batchInsert( List<Composer> composers ) {
		int retval = 0;
		String sql = "INSERT INTO composers ( composer_name, born, died ) VALUES ( ?, ?, ? ) ON DUPLICATE KEY UPDATE id = id;";
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
			logger.error( sqle.getMessage() );
		}
		return retval;
	}
	
	
	/**
	 * Update the metadata object, observing foreign key dependency.
	 * 
	 * @param c
	 * @param m
	 * @since 1.0.0
	 */
	private static void doUpdate( Composer c, Metadata m ) {
		// Set the composer.
		m.setComposer( c );
		
		// Get and update the Metadata's work with the composer's new ID
		Work w = m.getWork();
		w.setComposerId( c.getId() );
		
		// Replace the Metadata's work.
		m.setWork( w );
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static BiPredicate<Composer, Metadata> getMatchingPredicate() {
		return matchComposer;	
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static BiConsumer<Composer, Metadata> getUpdateConsumerFunction() {
		return updateComposer;
	}
	
	
	/**
	 * 
	 * @param meta
	 * @return
	 */
	public List<Composer> mapFromMetadata( List<Metadata> meta ) {
		List<Composer> composers = new ArrayList<Composer>();
		for ( Metadata m : meta ) {
			composers.add( m.getComposer() );
		}
		return composers;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Composer> selectAll() {
		List<Composer> allComposers = new ArrayList<Composer>();
		String sql = "SELECT * FROM composers;";
		Database db = new Database();
		ResultSet rs = db.select( sql );
		try {
			while ( rs.next() ) {
				allComposers.add( new Composer( rs.getInt( "id" ), rs.getString( "composer_name" ), rs.getInt( "born" ), rs.getInt( "died" ) ) );
			}
			rs.close();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		}
		return allComposers;
	}
	
	
	/**
	 * 
	 * @param meta
	 * @param composers
	 */
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
