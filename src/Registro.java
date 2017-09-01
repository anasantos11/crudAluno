
import java.io.IOException;

@SuppressWarnings("rawtypes")
public interface Registro extends Comparable, Cloneable {

	public void setCodigo(int codigo);

	public int getCodigo();
	
	public void setStatus(int status);

	public int getStatus();

	public String getString();
	
	public void setString(String dados);

	public byte[] getByteArray() throws IOException;

	public void setByteArray(byte[] b) throws IOException;

	public int compareTo(Object b);

	public Object Clone() throws CloneNotSupportedException;
}
