package clef.mir.clefinfo;

public class Port {

	private String name;
	private int containerPort;
	private int hostPort;
	
	public String getName() {
		return name;
	}
	
	public int getContainerPort() {
		return containerPort;
	}
	
	public int getHostPort() {
		return hostPort;
	}
	
	public void setName( String n ) {
		this.name = n;
	}
	
	public void setContainerPort( int p ) {
		this.containerPort = p;
	}
	
	public void setHostPort( int p ) {
		this.hostPort = p;
	}
}
