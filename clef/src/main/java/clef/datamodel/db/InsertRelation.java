package clef.datamodel.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import clef.common.ClefException;
import clef.datamodel.Relation;

public class InsertRelation extends ClefStatement implements Insert {

	private static final String SQL = "INSERT INTO %s VALUES ( ?, ? );";
	
	public <T extends Relation> InsertRelation( T t ) {
		super( t );
	}
	
	
	public PreparedStatement prepareAndBind( Connection conn ) throws SQLException, ClefException {
		
		Relation r = (Relation) this.cdo;
		String tbl = this.getTablename();

		String sql = String.format( SQL, tbl );
		
		PreparedStatement ps = conn.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS );
		ps.setInt( 1, r.getRelationId() );
		ps.setInt( 2, r.getRelatedToId() );
		
		return ps;
	}
}
