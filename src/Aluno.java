/**
 * @author Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Aluno implements Registro {
	private Integer matricula;
	private String nome;
	private Integer inativo; // Inativo = 1 Ativo = 0
	protected int idade;
	protected String curso;
	protected double notaMedia;
	
	public Aluno() {
		setStatus(0);
		setCodigo(-1);
		setString("");
		setIdade(0);
		setCurso("");
		setNotaMedia(0);
	}

	public Aluno(int matricula, String nome, int inativo, int idade, String curso, double notaMedia) {
		this.matricula = matricula;
		this.nome = nome;
		this.inativo = inativo;
		this.idade = idade;
		this.curso = curso;
		this.notaMedia = notaMedia;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public double getNotaMedia() {
		return notaMedia;
	}

	public void setNotaMedia(double notaMedia) {
		this.notaMedia = notaMedia;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
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
	public Integer getCodigo() {
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
		saida.writeInt(idade);
		saida.writeUTF(curso);
		saida.writeDouble(notaMedia);
		return registro.toByteArray();
	}

	@Override
	public void setByteArray(byte[] b) throws IOException {
		ByteArrayInputStream registro = new ByteArrayInputStream(b);
		DataInputStream entrada = new DataInputStream(registro);
		inativo = entrada.readInt();
		matricula = entrada.readInt();
		nome = entrada.readUTF();
		idade = entrada.readInt();
		curso = entrada.readUTF();
		notaMedia = entrada.readDouble();
	}

	@Override
	public String toString() {
		return "Aluno [matricula=" + matricula + ", inativo=" + inativo + ", nome=" + nome + ", idade=" + idade
				+ ", curso=" + curso + ", notaMedia=" + notaMedia + "]";
	}
	
	



}
