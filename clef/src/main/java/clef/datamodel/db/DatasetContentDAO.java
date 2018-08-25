package clef.datamodel.db;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.BiPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

import clef.datamodel.*;
import clef.datamodel.metadata.Metadata;

public class DatasetContentDAO extends ClefDAO {
	
	private static final Logger logger = LoggerFactory.getLogger( DatasetContentDAO.class );

	private static BiPredicate<DatasetContent, Metadata> matchDatasetContent = ( dc, m ) -> dc.getDatasetName().equals( m.getDatasetContent().getDatasetName() ) &&
			dc.getFilename().equals( m.getDatasetContent().getFilename() );
	
	private static BiConsumer<DatasetContent, Metadata> updateDatasetContent = ( dc, m ) -> m.setDatasetContent( dc );
	
	public int batchInsert( List<DatasetContent> dsetCont ) {
		int retval = 0;
		String sql = "INSERT INTO dataset_contents ( collection, dataset_name, filename ) VALUES ( ?, ?, ? ) ON DUPLICATE KEY UPDATE id = id;";
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			PreparedStatement ps = this.prepare( conn, sql, AutoCommit.OFF );
			for ( DatasetContent dc : dsetCont ) {
				ps.setString( 1, dc.getCollection() );
				ps.setString( 2, dc.getDatasetName() );
				ps.setString( 3, dc.getFilename() );
			}
			int[] results = ps.executeBatch();
			retval = this.sumBatchInsert( results );
			conn.commit();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		}
		return retval;
	}
	
	public static BiPredicate<DatasetContent, Metadata> getMatchingPredicate() {
		return matchDatasetContent;
	}
	
	public static BiConsumer<DatasetContent, Metadata> getUpdateConsumerFunction() {
		return updateDatasetContent;
	}
	
	public List<DatasetContent> mapFromMetadata( List<Metadata> meta ) {
		List<DatasetContent> dsetConts = new ArrayList<DatasetContent>();
		for ( Metadata m : meta ) {
			dsetConts.add( m.getDatasetContent() );
		}
		return dsetConts;
	}
	
	public List<DatasetContent> selectAll() {
		List<DatasetContent> dsetConts = new ArrayList<DatasetContent>();
		String sql = "SELECT * FROM dataset_contents;";
		Database db = new Database();
		ResultSet rs = db.select( sql );
		try {
			while ( rs.next() ) {
				DatasetContent dc = new DatasetContent( rs.getString( "collection" ), rs.getString( "dataset_name" ), rs.getString( "filename" ) );
				dc.setId( rs.getInt( "id" ) );
				dsetConts.add( dc );
			}
			rs.close();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		}
		return dsetConts;
	}
	
	public void updateMetadata( List<Metadata> meta, List<DatasetContent> dsetCont ) {
		for ( DatasetContent dc : dsetCont ) {
			for ( Metadata m : meta ) {
				if ( matchDatasetContent.test( dc, m ) ) {
					updateDatasetContent.accept( dc, m );
				}
			}
		}
	}
	
}
