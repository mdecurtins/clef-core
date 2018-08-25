package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clef.datamodel.Work;
import clef.datamodel.WorkType;
import clef.datamodel.metadata.Metadata;

/**
 * Data access class for WorkType objects.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class WorkTypeDAO extends ClefDAO {
	
	private static final Logger logger = LoggerFactory.getLogger( WorkTypeDAO.class );
	
	private static BiPredicate<WorkType, Metadata> matchWorkType = ( wt, m ) -> wt.getValue().equals( m.getWorkType().getValue() );
	private static BiConsumer<WorkType, Metadata> updateWorkType = ( wt, m ) -> doUpdate( wt, m );

	/**
	 * 
	 * @param wtypes
	 * @return
	 * @since 1.0.0
	 */
	public int batchInsert( List<WorkType> wtypes ) {
		int retval = 0;
		String sql = "INSERT INTO work_type ( work_type ) VALUES ( ? ) ON DUPLICATE KEY UPDATE id = id;";
		Database db = new Database();
		try {
			Connection conn = db.getConnection();
			PreparedStatement ps = this.prepare( conn, sql, AutoCommit.OFF );
			for ( WorkType wt : wtypes ) {
				ps.setString( 1, wt.getValue() );
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
	
	
	private static void doUpdate( WorkType wt, Metadata m ) {
		m.setWorkType( wt );
		Work w = m.getWork();
		w.setWorkTypeId( wt.getId() );
		m.setWork( w );
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public static BiPredicate<WorkType, Metadata> getMatchingPredicate() {
		return matchWorkType;
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public static BiConsumer<WorkType, Metadata> getUpdateConsumerFunction() {
		return updateWorkType;
	}
	
	
	/**
	 * 
	 * @param wt
	 * @return
	 * @since 1.0.0
	 */
	public int insertWorkType( WorkType wt ) {
		return this.insertSingle( wt );
	}
	
	
	/**
	 * 
	 * @param meta
	 * @return
	 * @since 1.0.0
	 */
	public List<WorkType> mapFromMetadata( List<Metadata> meta ) {
		List<WorkType> wtypes = new ArrayList<WorkType>();
		for ( Metadata m : meta ) {
			wtypes.add( m.getWorkType() );
		}
		return wtypes;
	}
	
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public List<WorkType> selectAll() {
		List<WorkType> wtypes = new ArrayList<WorkType>();
		String sql = "SELECT * FROM work_type;";
		Database db = new Database();
		ResultSet rs = db.select( sql ); 
		try {
			while ( rs.next() ) {
				wtypes.add( new WorkType( rs.getInt( "id" ), rs.getString( "work_type" ) ) );
			}
			rs.close();
		} catch ( SQLException sqle ) {
			logger.error( sqle.getMessage() );
		}
		return wtypes;
	}
	
	
	/**
	 * 
	 * @param meta
	 * @param wtypes
	 * @since 1.0.0
	 */
	public void updateMetadata( List<Metadata> meta, List<WorkType> wtypes ) {
		for ( WorkType wt : wtypes ) {
			for ( Metadata m : meta ) {
				if ( matchWorkType.test( wt, m ) ) {
					updateWorkType.accept( wt, m );
				}
			}
		}
	}
}
