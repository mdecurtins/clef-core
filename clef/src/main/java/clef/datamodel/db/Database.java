package clef.datamodel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clef.common.ClefException;

/**
 * Class that provides database connectivity for the Clef metadata store.
 * 
 * @author Max DeCurtins
 * @since 1.0.0
 */
public class Database {

	private static final Logger logger = LoggerFactory.getLogger( Database.class );
	
	/**
	 * 
	 * @return
	 * @since 1.0.0
	 */
	protected Connection getConnection() {
		Connection conn = null;
		String p = System.getenv( "MYSQL_ROOT_PASSWORD" );
		String db = System.getenv( "MYSQL_DATABASE" );
		
		if ( p == null || db == null || p.equals("") || db.equals("") ) {
			logger.error( "Error: could not get database credentials from the environment." );
			return conn;
		}
		try {
			// Suppress MySQL SSL warning - consider SSL in future development
			String url = String.format( "jdbc:mysql://db:3306/%s?user=root&password=%s&useSSL=false", db, p );
			
			conn = DriverManager.getConnection( url );
		} catch ( SQLException sqle ) {
			sqle.printStackTrace();
		}
		if ( conn == null ) {
			logger.error( "getConnection(): connection was null");
		}
		return conn;
	}
	
	
	/**
	 * Method to insert a single record in the database.
	 * 
	 * @param clefStatement
	 * @return
	 * @since 1.0.0
	 */
	public <T extends Insert> int insert( T clefStatement ) {
		
		int insertedId = -1;
		
		try {
			PreparedStatement ps = clefStatement.prepareAndBind( this.getConnection() );
			
			if ( ps != null ) {
				ResultSet rs = ps.executeQuery();
				
				if ( rs.next() ) {
					insertedId = rs.getInt( 1 );
					rs.close();
				} else {
					throw new SQLException( "Error: could not retrieve last inserted ID." );
				}
			}
			
		} catch ( SQLException sqle ) {
			sqle.printStackTrace();
		} catch ( ClefException ce ) {
			ce.printStackTrace();
		}
		
		return insertedId;
	}
	
	
	/**
	 * Method to select from the database.
	 * 
	 * @param sql
	 * @return
	 * @since 1.0.0
	 */
	public ResultSet select( String sql ) {
		ResultSet rs = null;
		Connection conn = this.getConnection();
		
		if ( conn != null ) {
			try {
				Statement st = conn.createStatement();
				rs = st.executeQuery( sql );
			} catch ( SQLException sqle ) {
				sqle.printStackTrace();
			}
			
		}
		return rs;
	}
	
	
}
