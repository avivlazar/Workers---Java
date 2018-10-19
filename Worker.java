import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Worker<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name; 
	private T dep; 
	private int salary;
	/** constructor with String for worker without including boss */
	public Worker(String name, T dep, int salary) {
		super();
		this.name = name;
		this.dep =  dep;
		this.salary = salary;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public T getDep() {
		return dep;
	}
	public void setDep(T dep) {
		this.dep = dep;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public void writeWorker(DataOutputStream o, boolean isItDep) throws IOException {
		o.writeUTF(name);
		if(isItDep) {
			o.writeUTF((((Department) dep).getDepName()));
			o.writeUTF((((Department) dep).getDepHead()));
		}
		else {
			o.writeUTF((String) dep);
		}
		o.writeInt(salary);
	}
	@Override
	public String toString() {
		return "Worker [name=" + name + ", dep=" + dep + ", salary=" + salary + "]";
	}
	
}
