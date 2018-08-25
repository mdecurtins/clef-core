package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import clef.datamodel.Era;
import clef.datamodel.Work;
import clef.datamodel.metadata.Metadata;

/**
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class EraDAO extends ClefDAO {
	
	private static final Logger logger = LoggerFactory.getLogger( EraDAO.class );
	
	private static BiPredicate<Era, Metadata> matchEra = ( era, m ) -> era.getValue().equals( m.getEra().getValue() );
	private static BiConsumer<Era, Metadata> updateEra = ( era, m ) -> doUpdate( era, m );
	
	
	/**
	 * 
	 * @param eras
	 * @return
	 * @since 1.0.0
	 */
	public int batchInsert( List<Era> eras ) {
		logger.info( "EraDAO: calling batchInsert()...");
		int retval = 0;
		String sql = "INSERT INTO eras ( era ) VALUES ( ? ) ON DUPLICATE KEY UPDATE id = id;";
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			PreparedStatement ps = this.prepare( conn, sql, AutoCommit.OFF );
			for ( Era era : eras ) {
				ps.setString( 1, era.getValue() );
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
	
	
	private static void doUpdate( Era era, Metadata m ) {
		// Set the era on the Metadata instance
		m.setEra( era );
		
		//
		Work w = m.getWork();
		w.setEraId( era.getId() );
		
		// Update the Metadata work instance
		m.setWork( w );
	}
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public static BiPredicate<Era, Metadata> getMatchingPredicate() {
		return matchEra;
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public static BiConsumer<Era, Metadata> getUpdateConsumerFunction() {
		return updateEra;
	}
	
	
	/**
	 * 
	 * @param era
	 * @return
	 * @since 1.0.0
	 */
	public int insertEra( Era era ) {
		return this.insertSingle( era );
	}
	
	
	/**
	 * 
	 * @param meta
	 * @return
	 * @since 1.0.0
	 */
	public List<Era> mapFromMetadata( List<Metadata> meta ) {
		List<Era> eras = new ArrayList<Era>();
		for ( Metadata m : meta ) {
			eras.add( m.getEra() );
		}
		return eras;
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<Era> selectAll() {
		List<Era> eras = new ArrayList<Era>();
		String sql = "SELECT * FROM eras;";
		Database db = new Database();
		ResultSet rs = db.select( sql ); 
		try {
			while ( rs.next() ) {
				eras.add( new Era( rs.getInt( "id" ), rs.getString( "era" ) ) );
			}			
			rs.close();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		}
		return eras;
	}
	
	
	/**
	 * 
	 * @param meta
	 * @param eras
	 * @since 1.0.0
	 */
	public void updateMetadata( List<Metadata> meta, List<Era> eras ) {	
		// Loop over all combinations of Eras and Metadata.
		for ( Era era : eras ) {
			for ( Metadata m : meta ) {
				// If this Metdata contains an Era whose value matches the current Era instance value.
				if ( matchEra.test( era, m ) ) {
					// Replace the Metadata's Era with an Era instance containing a database ID.
					updateEra.accept( era, m );
				}
			}
		}
	}
}
