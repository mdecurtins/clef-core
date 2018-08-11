package clef.datamodel.db;

import clef.common.ClefException;
import clef.datamodel.ClefDataObject;

public abstract class ClefStatement {

	protected ClefDataObject cdo;
	
	public ClefStatement( ClefDataObject cdo ) {
		this.cdo = cdo;
	}
	
	public ClefDataObject getClefDataObject() {
		return cdo;
	}
	
	protected String getTablename() throws ClefException {
		String tbl = "";
		String clazzName = this.cdo.getClass().getSimpleName();
		switch ( clazzName  ) {
		case "Composer":
			tbl = "composers";
			break;
		case "DatasetContents":
			tbl = "dataset_contents";
			break;
		case "Era":
			tbl = "eras";
			break;
		case "Tag":
			tbl = "tags";
			break;
		case "TagRelation":
			tbl = "tag_relations";
			break;
		case "Work":
			tbl = "works";
			break;
		case "WorkType":
			tbl = "work_type";
			break;
		default:
			throw new ClefException( "Unrecognized class name: " + clazzName + ". Cannot map class name to a table name." );
		}
		return tbl;
	}
	
}
