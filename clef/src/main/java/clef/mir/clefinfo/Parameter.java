package clef.mir.clefinfo;

import clef.common.ClefException;

public class Parameter {

	private String name;
	private String description;
	private String key;
	private ParameterType type;
	private Object value;
	
	private static enum ParameterType {
		BOOLEAN, NUMBER, STRING
	}
	
	public Parameter() {
		this.type = null;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		String type = "";
		switch ( this.type ) {
		case BOOLEAN:
			type = "boolean";
			break;
		case NUMBER:
			type = "number";
			break;
		case STRING:
			type = "string";
			break;
		}
		return type;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setDescription( String description ) {
		this.description = description;
	}
	
	public void setKey( String key ) {
		this.key = key;
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public void setType( String type ) throws ClefException {
		switch ( type.toLowerCase() ) {
		case "boolean":
			this.type = ParameterType.BOOLEAN;
			break;
		case "number":
			this.type = ParameterType.NUMBER;
			break;
		case "string":
			this.type = ParameterType.STRING;
			break;
		default:
			throw new ClefException( "Unsupported parameter type: " + type + ", supported types are: boolean, number, string" );
		}
	}
	
	public void setValue( Object o ) {
		this.value = o;
	}
}
