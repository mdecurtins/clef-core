package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import clef.datamodel.Single;

/**
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public abstract class ClefDAO {
	
	protected enum AutoCommit {
		ON, OFF
	}

	
	/**
	 * 
	 * @param t
	 * @return
	 * @since 1.0.0
	 */
	protected <T extends Single> int insertSingle( T t ) {
		InsertSingle ins = new InsertSingle( t );
		Database db = new Database();
		return db.insert( ins );
	}
	
	
	/**
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @since 1.0.0
	 */
	protected PreparedStatement prepare( Connection conn, String sql ) throws SQLException {	
		return conn.prepareStatement( sql );
	}
	
	
	/**
	 * 
	 * @param conn
	 * @param sql
	 * @param ac
	 * @return
	 * @throws SQLException
	 * @since 1.0.0
	 */
	protected PreparedStatement prepare( Connection conn, String sql, AutoCommit ac ) throws SQLException {
		if ( ac == AutoCommit.OFF ) {
			conn.setAutoCommit( false );
		} else if ( ac == AutoCommit.ON ) {
			conn.setAutoCommit( true );
		}
		return this.prepare( conn, sql );
	}
	
	
	/**
	 * 
	 * @param batchResults
	 * @return
	 * @since 1.0.0
	 */
	protected int sumBatchInsert( int[] batchResults ) {
		int retval = 0;
		for ( int i = 0; i < batchResults.length; i++ ) {
			retval += batchResults[i];
		}
		return retval;
	}
}
