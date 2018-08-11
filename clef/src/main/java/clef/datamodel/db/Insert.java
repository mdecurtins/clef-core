package clef.datamodel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import clef.common.ClefException;

public interface Insert {

	public PreparedStatement prepareAndBind( Connection conn ) throws SQLException, ClefException;
	
}
