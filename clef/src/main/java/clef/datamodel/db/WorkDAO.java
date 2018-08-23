package clef.datamodel.db;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import clef.datamodel.Work;
import clef.datamodel.metadata.Metadata;

public class WorkDAO extends ClefDAO {

	private static BiPredicate<Work, Metadata> matchWork = ( w, m ) -> w.getTitle().equals( m.getWork().getTitle() ) &&
			   w.getCatalog().equals( m.getWork().getCatalog() ) && 
			   w.getCatalogNumber().equals( m.getWork().getCatalogNumber() );

	private static BiConsumer<Work, Metadata> updateWork = ( w, m ) -> m.setWork( w );
	
	
	public int batchInsert( List<Work> works ) {
		int retval = 0;
		String sql = "INSERT INTO works ( title, catalog, catalog_number, composer_id, era_id, work_type_id ) VALUES ( ?, ?, ?, ?, ?, ? ) ON DUPLICATE KEY UPDATE id = id;";
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			PreparedStatement ps = this.prepare( conn, sql, AutoCommit.OFF );
			for ( Work w : works ) {
				ps.setString( 1, w.getTitle() );
				ps.setString( 2, w.getCatalog() );
				ps.setString( 3, w.getCatalogNumber() );
				ps.setInt( 4, w.getComposerId() );
				ps.setInt( 5, w.getEraId() );
				ps.setInt( 6, w.getWorkTypeId() );
				ps.addBatch();
			}
			int[] results = ps.executeBatch();
			retval = this.sumBatchInsert( results );
			conn.commit();
		} catch ( SQLException sqle ) {
			sqle.printStackTrace();
		}
		return retval;
	}
	
	public static BiPredicate<Work, Metadata> getMatchingPredicate() {
		return matchWork;
	}
	
	public static BiConsumer<Work, Metadata> getUpdateConsumerFunction() {
		return updateWork;
	}
	
	public List<Work> mapFromMetadata( List<Metadata> meta ) {
		List<Work> works = new ArrayList<Work>();
		for ( Metadata m : meta ) {
			works.add( m.getWork() );
		}
		return works;
	}
	
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
				w.setComposerId( rs.getInt( "composer_id" ) );
				w.setEraId( rs.getInt( "era_id" ) );
				w.setWorkTypeId( rs.getInt( "work_type_id" ) );
				allWorks.add( w );
			}
			rs.close();
		} catch ( SQLException sqle ) {
			sqle.printStackTrace();
		} 
		return allWorks;
	}
	
	public void updateMetadata( List<Metadata> meta, List<Work> works ) {
		for ( Work w : works ) {
			for ( Metadata m : meta ) {
				if ( matchWork.test( w, m ) ) {
					updateWork.accept( w, m );
				}
			}
		}
	}
}
