package clef.datamodel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Map;

import clef.common.ClefException;

public class Database {

	protected Connection getConnection() {
		Connection conn = null;
		String p = System.getenv( "MYSQL_ROOT_PASSWORD" );
		String db = System.getenv( "MYSQL_DATABASE" );
		
		if ( p == null || db == null || p.equals("") || db.equals("") ) {
			System.err.println( "Error: could not get database credentials from the environment." );
			return conn;
		}
		try {
			String url = String.format( "jdbc:mysql://db:3306/%s?user=root&password=%s", db, p );
			
			conn = DriverManager.getConnection( url );
		} catch ( SQLException sqle ) {
			sqle.printStackTrace();
		}
		return conn;
	}
	
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
	
	public ResultSet selectPrepared( String sql, Map<String, Object> params ) {
		
		return null;
	}
}
