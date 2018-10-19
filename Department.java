import java.io.Serializable;

public class Department implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String depName; 
	private String depHead;
	public Department(String depName, String depHead) {
		super();
		this.depName = depName;
		this.depHead = depHead;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getDepHead() {
		return depHead;
	}
	@Override
	public String toString() {
		return "Department [depName=" + depName + ", depHead=" + depHead + "]";
	}
	public void setDepHead(String depHead) {
		this.depHead = depHead;
	}
	
	

}
