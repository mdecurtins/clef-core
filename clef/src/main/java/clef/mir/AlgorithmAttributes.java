package clef.mir;

import java.util.ArrayList;
import java.util.List;

import clef.mir.clefinfo.DatasetCapabilities;
import clef.mir.clefinfo.InputCapabilities;
import clef.mir.clefinfo.Parameter;

public class AlgorithmAttributes {

	
	private DatasetCapabilities datacaps;
	private String displayName;
	private InputCapabilities inputcaps;
	private String name;
	private List<Parameter> parameters;
	private String queryRoute;
	private String type;
	
	public AlgorithmAttributes() {
		this.displayName = "";
		this.parameters = new ArrayList<Parameter>();
	}
	
	public DatasetCapabilities getDatasetCapabilities() {
		return datacaps;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public InputCapabilities getInputCapabilities() {
		return inputcaps;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Parameter> getParameters() {
		return this.parameters;
	}
	
	/**
	 * Gets the route to the algorithm execution endpoint, if one is defined.
	 * 
	 * If a route to the algorithm execution endpoint is defined, this method returns that route with a forward slash prepended.
	 * 
	 * @return if a route to the algorithm execution endpoint is defined, then said route with a forward slash prepended, otherwise NULL.
	 */
	public String getQueryRoute() {
		return ( queryRoute != null && queryRoute.length() > 0 ) ? "/" + queryRoute : null;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDatasetCapabilities( DatasetCapabilities dc ) {
		this.datacaps = dc;
	}
	
	public void setDisplayName( String dn ) {
		this.displayName = dn;
	}
	
	public void setInputCapabilities( InputCapabilities ic ) {
		this.inputcaps = ic;
	}
	
	public void setName( String n ) {
		this.name = n;
	}
	
	public void setQueryRoute( String qr ) {
		this.queryRoute = qr;
	}
	
	public void setType( String t ) {
		this.type = t;
	}
	
	
}
