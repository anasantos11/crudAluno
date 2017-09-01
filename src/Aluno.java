

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Aluno implements Registro {
	protected int matricula;
	protected String nome;
	protected int inativo; // Inativo = 1 Ativo = 0

	public Aluno() {
		setStatus(0);
		setCodigo(-1);
		setString("");
	}

	public Aluno(int matricula, String nome) {
		setStatus(0);
		setString(nome);
		setCodigo(matricula);
	}

	@Override
	public void setStatus(int inativo) {
		this.inativo = inativo;
	}

	@Override
	public int getStatus() {
		return this.inativo;
	}

	@Override
	public void setCodigo(int matricula) {
		this.matricula = matricula;
	}

	@Override
	public int getCodigo() {
		return this.matricula;
	}

	@Override
	public String getString() {
		return this.nome;
	}

	@Override
	public void setString(String nome) {
		this.nome = nome;
	}

	@Override
	public byte[] getByteArray() throws IOException {
		ByteArrayOutputStream registro = new ByteArrayOutputStream();
		DataOutputStream saida = new DataOutputStream(registro);
		saida.writeInt(inativo);
		saida.writeInt(matricula);
		saida.writeUTF(nome);
		return registro.toByteArray();
	}

	@Override
	public void setByteArray(byte[] b) throws IOException {
		ByteArrayInputStream registro = new ByteArrayInputStream(b);
		DataInputStream entrada = new DataInputStream(registro);
		inativo = entrada.readInt();
		matricula = entrada.readInt();
		nome = entrada.readUTF();
	}

	@Override
	public int compareTo(Object b) {
		return matricula - ((Aluno) b).getCodigo();
	}

	@Override
	public Aluno Clone() throws CloneNotSupportedException {

		return ((Aluno) super.clone());

	}

	@Override
	public String toString() {
		return "Aluno [" + "inativo=" + inativo + ", matricula=" + matricula + ", nome=" + nome + "]";
	}

}
