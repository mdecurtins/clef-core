package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import clef.datamodel.WorkType;
import clef.datamodel.metadata.Metadata;

public class WorkTypeDAO extends ClefDAO {
	
	private static BiPredicate<WorkType, Metadata> matchWorkType = ( wt, m ) -> wt.getValue().equals( m.getWorkType().getValue() );
	private static BiConsumer<WorkType, Metadata> updateWorkType = ( wt, m ) -> m.setWorkType( wt );

	public int batchInsert( List<WorkType> wtypes ) {
		int retval = 0;
		String sql = "INSERT INTO work_type VALUES ( ? ) ON DUPLICATE KEY UPDATE id = id;";
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
			
		}
		return retval;
	}
	
	public static BiPredicate<WorkType, Metadata> getMatchingPredicate() {
		return matchWorkType;
	}
	
	public static BiConsumer<WorkType, Metadata> getUpdateConsumerFunction() {
		return updateWorkType;
	}
	
	public int insertWorkType( WorkType wt ) {
		return this.insertSingle( wt );
	}
	
	public List<WorkType> mapFromMetadata( List<Metadata> meta ) {
		List<WorkType> wtypes = new ArrayList<WorkType>();
		for ( Metadata m : meta ) {
			wtypes.add( m.getWorkType() );
		}
		return wtypes;
	}
	
	public List<WorkType> selectAll() {
		List<WorkType> wtypes = new ArrayList<WorkType>();
		String sql = "SELECT * FROM work_types;";
		Database db = new Database();
		ResultSet rs = db.select( sql ); 
		try {
			while ( rs.next() ) {
				wtypes.add( new WorkType( rs.getInt( "id" ), rs.getString( "work_type" ) ) );
			}
			rs.close();
		} catch ( SQLException sqle ) {
			
		}
		return wtypes;
	}
	
	public void updateMetadata( List<Metadata> meta, List<WorkType> wtypes ) {
		
	}
}
