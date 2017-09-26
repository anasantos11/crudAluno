/**
 * @author Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Arquivo {
	private String db;
	private String indexador;
	private static long ponteiroAnterior;
	private static long ponteiroAnteriorIndex;
	public static Index teste;
	private static final int TAM_INDEX = Integer.BYTES + Integer.BYTES + Long.BYTES;

	public Arquivo() throws Exception {
		this.db = "bd.txt";
		this.indexador = "indexes.txt";
	}

	/**
	 * Metodo para inserir novo registro no arquivo e atualizar o indice primario.
	 * Caso receba como parametro um seek diferente de -1 ira sobrescrever os dados
	 * na posicao passada
	 * 
	 * @param r
	 * @param seek
	 * @throws IOException
	 */
	public void criarRegistro(Registro r, long seek) throws IOException {
		RandomAccessFile file = new RandomAccessFile(db, "rw");
		if (seek == -1) {
			seek = file.length();
			criarIndex(new Index(r.getCodigo(), seek), -1);
			file.seek(seek);
		} else {
			file.seek(seek);
		}

		file.writeInt(r.getByteArray().length);
		file.write(r.getByteArray());
		file.close();
	}

	/**
	 * Método para criar indice primario dos registros de alunos
	 * 
	 * @param i
	 * @param seek
	 * @throws IOException
	 */

	public void criarIndex(Index i, long seek) throws IOException {
		RandomAccessFile rf = new RandomAccessFile(this.indexador, "rw");
		long posicaoFinal = rf.length();
		if (seek != -1) {
			posicaoFinal = seek;
		}
		rf.seek(posicaoFinal);
		rf.write(i.getByteArray());
		rf.close();
	}

	/**
	 * Metodo retornara string com os registros do arquivo
	 * 
	 * @return
	 * @throws IOException
	 */
	public String listarRegistros() throws IOException {
		RandomAccessFile file = new RandomAccessFile(db, "r");
		String dados = null;
		while (file.getFilePointer() < file.length()) {
			int tamanho = file.readInt();
			byte[] b = new byte[tamanho];
			file.read(b);
			Aluno aluno = new Aluno();
			aluno.setByteArray(b);
			if (aluno != null && aluno.getStatus() != 1) {
				if (dados == null) {
					dados = aluno.toString();
				} else {
					dados = dados + "\n" + aluno.toString();
				}
			}
		}
		file.close();
		return dados;
	}

	/**
	 * Metodo retornara string com os dados do indice primario
	 * 
	 * @return
	 * @throws IOException
	 */
	public String listarIndexes() throws IOException {
		Index i = null;
		String dadosIndex = "";
		try {
			RandomAccessFile rf = new RandomAccessFile(this.indexador, "r");
			while (rf.getFilePointer() < rf.length()) {
				byte[] b = new byte[TAM_INDEX];
				rf.read(b);
				i = new Index();
				i.setByteArray(b);
				if (i.getLapide() != 1) {
					dadosIndex += i.toString();
				}
			}

			rf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return "Não existe nenhum index";
		}
		return dadosIndex;
	}

	/**
	 * Metodo para retornar objeto Aluno caso seja localizado no registro
	 * 
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	public Aluno getRegistro(int codigo) throws Exception {
		@SuppressWarnings("resource")
		RandomAccessFile file = new RandomAccessFile(db, "rw");
		Index i = getIndexbyCode(codigo);
		Arquivo.teste = i;
		long seek = (i == null) ? -1 : i.getPosicaoArquivo();
		if (seek != -1) {
			file.seek(seek);
			int tamanho = file.readInt();
			byte[] b = new byte[tamanho];
			file.read(b);
			Aluno aluno = new Aluno();
			aluno.setByteArray(b);
			return aluno;
		} else {
			throw new Exception("Registro nao localizado");
		}
	}

	/**
	 * Metodo para buscar posicao de um determinado registro pela matricula
	 * utilizando o indice primario
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	public Index getIndexbyCode(int code) throws IOException {
		Index i = new Index();
		byte[] b = new byte[TAM_INDEX];
		@SuppressWarnings("resource")
		RandomAccessFile rf = new RandomAccessFile(this.indexador, "r");
		while (rf.getFilePointer() < rf.length()) {
			Arquivo.ponteiroAnteriorIndex = rf.getFilePointer();
			rf.read(b);
			i.setByteArray(b);
			if (i.getCodigo() == code && i.getLapide() != 1) {
				Arquivo.ponteiroAnterior = i.getPosicaoArquivo();
				return i;
			}
		}
		return null;
	}

	/**
	 * Metodo para setar lapide como inativa em determinado registro que sera
	 * pesquisado pelo codigo
	 * 
	 * @param codigo
	 * @throws Exception
	 */
	public void inativarRegistro(Registro a) throws Exception {
		/*Index i = getIndexbyCode(codigo);
		if(i == null) {
			throw new Exception ("Não foi encontrado o registro no índice.");
		}
		Aluno a = getRegistro(codigo);*/
		Arquivo.teste.setLapide(1);
		a.setStatus(1);
		criarRegistro(a, Arquivo.ponteiroAnterior);
		criarIndex(teste, Arquivo.ponteiroAnteriorIndex);
	}

	/**
	 * Metodo para atualizar um registro, ele colocara lapide no registro
	 * anterior e criara um novo no arquivo na ultima posicao
	 * 
	 * @param r
	 * @param codigo
	 * @throws Exception
	 */
	public void atualizarRegistro(Registro anterior, Registro novo, int codigo) throws Exception {
		inativarRegistro(anterior);
		criarRegistro(novo, -1);
	}
}
