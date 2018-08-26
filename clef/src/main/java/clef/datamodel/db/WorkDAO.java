package clef.datamodel.db;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clef.datamodel.Work;
import clef.datamodel.metadata.Metadata;

/**
 * Data access class for single musical works.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class WorkDAO extends ClefDAO {
	
	private static final Logger logger = LoggerFactory.getLogger( WorkDAO.class );

	private static BiPredicate<Work, Metadata> matchWork = ( w, m ) -> doPredicate( w, m );

	private static BiConsumer<Work, Metadata> updateWork = ( w, m ) -> m.setWork( w );
	
	
	/**
	 * 
	 * @param works
	 * @return
	 * @since 1.0.0
	 */
	public int batchInsert( List<Work> works ) {
		int retval = 0;
		String sql = "INSERT INTO works ( title, catalog, catalog_number, pcn, composer_id, era_id, work_type_id, dataset_contents_id ) "
				+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ON DUPLICATE KEY UPDATE id = id;";
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			PreparedStatement ps = this.prepare( conn, sql, AutoCommit.OFF );
			for ( Work w : works ) {
				ps.setString( 1, w.getTitle() );
				ps.setString( 2, w.getCatalog() );
				ps.setString( 3, w.getCatalogNumber() );
				ps.setString( 4, w.getPCN() );
				ps.setInt( 5, w.getComposerId() );
				ps.setInt( 6, w.getEraId() );
				ps.setInt( 7, w.getWorkTypeId() );
				ps.setInt( 8, w.getDatasetContentsId() );
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
	 * @since 1.0.0
	 */
	public static BiPredicate<Work, Metadata> getMatchingPredicate() {
		return matchWork;
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public static BiConsumer<Work, Metadata> getUpdateConsumerFunction() {
		return updateWork;
	}
	
	
	/**
	 * 
	 * @param meta
	 * @return 
	 * @since 1.0.0
	 */
	public List<Work> mapFromMetadata( List<Metadata> meta ) {
		List<Work> works = new ArrayList<Work>();
		for ( Metadata m : meta ) {
			works.add( m.getWork() );
		}
		return works;
	}
	
	
	private static boolean doPredicate( Work w, Metadata m ) {
		boolean title = false;
		boolean composer = false;
		boolean dataset_content = false;
		
		// Check if work title matches the Metadata's work's title
		title = w.getTitle().equals( m.getWork().getTitle() );
		
		// Check if work composer ID matches the Metadata's composer's ID
		composer = w.getComposerId() == m.getComposer().getId();
		
		// Check if work dataset content ID matches the Metadata's dataset content ID
		dataset_content = w.getDatasetContentsId() == m.getDatasetContent().getId();
		
		return title && composer && dataset_content;
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<Work> selectAll() {
		List<Work> allWorks = new ArrayList<Work>();
		String sql = "SELECT * FROM works;";
		Database db = new Database();
		ResultSet rs = db.select( sql );
		try {
			while ( rs.next() ) {
				Work w = new Work();
				w.setId( rs.getInt( "id" ) );
				w.setTitle( rs.getString( "title" ) );
				w.setCatalog( rs.getString( "catalog" ) );
				w.setCatalogNumber( rs.getString( "catalog_number" ) );
				w.setPCN( rs.getString( "pcn" ) );
				w.setComposerId( rs.getInt( "composer_id" ) );
				w.setEraId( rs.getInt( "era_id" ) );
				w.setWorkTypeId( rs.getInt( "work_type_id" ) );
				w.setDatasetContentsId( rs.getInt( "dataset_contents_id" ) );
				allWorks.add( w );
			}
			rs.close();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		} 
		return allWorks;
	}
	
	
	/**
	 * 
	 * @param meta
	 * @param works
	 * @since 1.0.0
	 */
	public void updateMetadata( List<Metadata> meta, List<Work> works ) {
		logger.debug( "Calling updateMetadata()...");
		for ( Work w : works ) {
			for ( Metadata m : meta ) {				
				if ( matchWork.test( w, m ) ) {
					updateWork.accept( w, m );
				}			
			}
		}
	}
}
