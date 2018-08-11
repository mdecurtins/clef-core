package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import clef.common.ClefException;
import clef.datamodel.Composer;

public class InsertComposer extends ClefStatement implements Insert {

	private static final String SQL = "INSERT INTO %s VALUES ( ?, ?, ? );";
	
	public InsertComposer( Composer c ) {
		super( c );
	}


	public PreparedStatement prepareAndBind( Connection conn ) throws SQLException, ClefException {
		
		String tbl = this.getTablename();
		
		String sql = String.format( SQL, tbl );
		
		PreparedStatement ps = conn.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS );
		
		Composer c = (Composer) this.cdo;
		
		// Set the composer name.
		ps.setString( 1, c.getName() );
		
		// If we have a birth year for the composer, set it. Otherwise, set NULL.
		if ( c.born() != -1 ) {
			ps.setInt( 2, c.born() );
		} else {
			ps.setNull( 2, Types.INTEGER );
		}
		
		// If we have a death year for the composer, set it. Otherwise, set NULL.
		if ( c.died() != -1 ) {
			ps.setInt( 3,  c.died() );
		} else {
			ps.setNull( 3, Types.INTEGER );
		}
	
		return ps;
	}
}
