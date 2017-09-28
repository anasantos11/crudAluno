/**
 * @authors Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.io.IOException;

public interface Registro {

	public void setCodigo(int codigo);

	public Integer getCodigo();

	public void setStatus(int status);

	public int getStatus();

	public String getString();

	public void setString(String dados);

	public byte[] getByteArray() throws IOException;

	public void setByteArray(byte[] b) throws IOException;

}
