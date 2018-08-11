package clef.datamodel.db;

import clef.common.ClefException;
import clef.datamodel.Single;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertSingle extends ClefStatement implements Insert {

	private static final String SQL = "INSERT INTO %s VALUES ( ? );";
	
	public <T extends Single> InsertSingle( T t ) {
		super( t );
	}

	
	public PreparedStatement prepareAndBind( Connection conn ) throws SQLException, ClefException {
		
		String tbl = this.getTablename();
		Single obj = (Single) this.cdo;
		
		String query = String.format( SQL, tbl );
		
		PreparedStatement ps = conn.prepareStatement( query, PreparedStatement.RETURN_GENERATED_KEYS );
		ps.setString( 1, obj.getValue() );
		
		return ps;
	}
}
