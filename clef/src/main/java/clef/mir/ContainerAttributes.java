package clef.mir;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import clef.mir.clefinfo.Port;

public class ContainerAttributes {

	private String name;
	private String networkAlias;
	private List<Port> ports;
	
	public ContainerAttributes() {
		this.ports = new ArrayList<Port>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getNetworkAlias() {
		return networkAlias;
	}
	
	public List<Port> getPorts() {
		return ports;
	}
	
	public Port getQueryPort() {
		Stream<Port> sp = this.ports.stream().filter( p -> p.getName().equals("query") );
		Object[] pa = sp.toArray();
		return (Port) pa[0];
	}
	
	public void setName( String n ) {
		this.name = n;
	}
	
	public void setNetworkAlias( String na ) {
		this.networkAlias = na;
	}
	
	public void setPorts( List<Port> p ) {
		this.ports = p;
	}
}
