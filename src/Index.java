import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Index {
	private int codigo;
	private long posicaoArquivo;
	
	public Index(int codigo, long posicaoArquivo) {
		this.codigo = codigo;
		this.posicaoArquivo = posicaoArquivo;
	}
	
	public Index() {
		this.codigo = 0;
		this.posicaoArquivo = 0;
	}

	public byte[] getByteArray() throws IOException {
		ByteArrayOutputStream registro = new ByteArrayOutputStream();
		DataOutputStream saida = new DataOutputStream(registro);
		saida.writeInt(codigo);
		saida.writeLong(posicaoArquivo);
		return registro.toByteArray();
	}

	public void setByteArray(byte[] b) throws IOException {
		ByteArrayInputStream registro = new ByteArrayInputStream(b);
		DataInputStream entrada = new DataInputStream(registro);
		this.codigo = entrada.readInt();
		this.posicaoArquivo = entrada.readLong();
	}

	@Override
	public String toString() {
		return "Index [codigo=" + codigo + ", posicaoArquivo=" + posicaoArquivo + "]\n";
	}
	
	
}
