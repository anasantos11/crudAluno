
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JFileChooser;

public class Arquivo {
	private String db;
	private String indexador;
	private static String dados;
	private static long ponteiroAnterior;

	public Arquivo() throws Exception {
		this.db = "bd.txt";
		this.indexador = "indexes.txt";
	}

	/**
	 * M�todo para pegar arquivo do dispositivo utilizado na execu��o
	 * 
	 * @return
	 * @throws Exception
	 */

	/*public static String pegarArquivo() throws Exception {
		JFileChooser arquivo = new JFileChooser();
		int retorno = arquivo.showOpenDialog(null);
		if (retorno == JFileChooser.CANCEL_OPTION) {
			throw new Exception("Processo cancelado");
		}
		String fileName = arquivo.getSelectedFile().getPath();
		return fileName;
	}*/

	/**
	 * M�todo para setar novo registro no arquivo, caso receba como par�metro um
	 * seek diferente de -1 ir� sobrescrever os dados da posi��o passada
	 * 
	 * @param r
	 * @param seek
	 * @throws IOException
	 */
	public void criarRegistro(Registro r, long seek) throws IOException {
		RandomAccessFile file = new RandomAccessFile(db, "rw");
		if (seek == -1) {
			seek = file.length();
			file.seek(seek);
		} else {
			file.seek(seek);
		}
		
		file.writeInt(r.getByteArray().length);
		file.write(r.getByteArray());
		file.close();
		criarIndex(new Index(r.getCodigo(), seek));
	}

	/**
	 * M�todo ir� retornar string com os registros do arquivo
	 * 
	 * @return
	 * @throws IOException
	 */
	public String listarRegistros() throws IOException {
		RandomAccessFile file = new RandomAccessFile(db, "r");
		while (file.getFilePointer() < file.length()) {
			int tamanho = file.readInt();
			byte[] b = new byte[tamanho];
			file.read(b);
			Aluno aluno = new Aluno();
			aluno.setByteArray(b);
			if (aluno != null) {
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
	
	public void criarIndex(Index i) throws IOException {
		RandomAccessFile rf = new RandomAccessFile(this.indexador, "rw");
		long posicaoFinal = rf.length();
		rf.seek(posicaoFinal);
		
		rf.write(i.getByteArray());
		rf.close();
	}
	
	public String listarIndexes() throws IOException {
		Index i = null;
		String dadosIndex = "";
		try {
			RandomAccessFile rf = new RandomAccessFile(this.indexador, "r");
			while(rf.getFilePointer() < rf.length()) {
				byte[] b = new byte[Integer.BYTES + Long.BYTES];
				rf.read(b);
				i = new Index();
				i.setByteArray(b);
				dadosIndex += i.toString();
			}
			
			rf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return "N�o existe nenhum index";
		}
		return dadosIndex;
	}

	/**
	 * M�todo para retornar objeto caso seja localizado no registro
	 * 
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	public Aluno getRegistro(int codigo) throws Exception {
		RandomAccessFile file = new RandomAccessFile(db, "rw");
		long seek = getPonteiroRegistro(codigo);
		if (seek != -1) {
			file.seek(seek);
			int tamanho = file.readInt();
			byte[] b = new byte[tamanho];
			file.read(b);
			Aluno aluno = new Aluno();
			aluno.setByteArray(b);
			return aluno;
		} else {
			throw new Exception("Registro n�o localizado");
		}
	}

	/**
	 * M�todo retorna ponteiro do registro pesquisado por c�digo
	 * 
	 * @param codigo
	 * @return
	 * @throws IOException
	 */
	public long getPonteiroRegistro(int codigo) throws IOException {
		RandomAccessFile file = new RandomAccessFile(db, "r");
		while (file.getFilePointer() < file.length()) {
			ponteiroAnterior = file.getFilePointer();
			int tamanho = file.readInt();
			byte[] b = new byte[tamanho];
			file.read(b);
			Aluno aluno = new Aluno();
			aluno.setByteArray(b);
			if (aluno != null) {
				if (aluno.getCodigo() == codigo) {
					return ponteiroAnterior;
				}
			}
		}
		file.close();
		return -1;
	}

	/**
	 * M�todo para setar l�pide como inativa em determinado registro que ser�
	 * pesquisado pelo codigo
	 * 
	 * @param codigo
	 * @throws Exception
	 */
	public void inativarRegistro(int codigo) throws Exception {
		Aluno a = getRegistro(codigo);
		a.setStatus(1);
		criarRegistro(a, ponteiroAnterior);
	}
	
	/**
	 * M�todo para setar l�pide como ativa em determinado registro que ser�
	 * pesquisado pelo codigo
	 * 
	 * @param codigo
	 * @throws Exception
	 */
	public void ativarRegistro(int codigo) throws Exception {
		Aluno a = getRegistro(codigo);
		a.setStatus(0);
		criarRegistro(a, ponteiroAnterior);
	}

	/**
	 * M�todo para atualizar um registro, ele setar� l�pide no registro anterior e
	 * criar� um novo no arquivo na �ltima posi��o
	 * 
	 * @param r
	 * @param codigo
	 * @throws Exception
	 */
	public void atualizarRegistro(Registro r, int codigo) throws Exception {
		inativarRegistro(codigo);
		criarRegistro(r, -1);
	}
}
