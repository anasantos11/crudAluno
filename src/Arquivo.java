
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JFileChooser;

public class Arquivo {
	private String nomeArquivo;
	private static String dados;
	private static long ponteiroAnterior;

	public Arquivo() throws Exception {
		this.nomeArquivo = pegarArquivo();
	}

	/**
	 * M�todo para pegar arquivo do dispositivo utilizado na execu��o
	 * 
	 * @return
	 * @throws Exception
	 */

	public static String pegarArquivo() throws Exception {
		JFileChooser arquivo = new JFileChooser();
		int retorno = arquivo.showOpenDialog(null);
		if (retorno == JFileChooser.CANCEL_OPTION) {
			throw new Exception("Processo cancelado");
		}
		String fileName = arquivo.getSelectedFile().getPath();
		return fileName;
	}

	/**
	 * M�todo para setar novo registro no arquivo, caso receba como par�metro um
	 * seek diferente de -1 ir� sobrescrever os dados da posi��o passada
	 * 
	 * @param r
	 * @param seek
	 * @throws IOException
	 */
	public void criarRegistro(Registro r, long seek) throws IOException {
		RandomAccessFile file = new RandomAccessFile(nomeArquivo, "rw");
		if (seek == -1) {
			file.seek(file.length());
		} else {
			file.seek(seek);
		}
		file.writeInt(r.getByteArray().length);
		file.write(r.getByteArray());
		file.close();
	}

	/**
	 * M�todo ir� retornar string com os registros do arquivo
	 * 
	 * @return
	 * @throws IOException
	 */
	public String listarRegistros() throws IOException {
		RandomAccessFile file = new RandomAccessFile(nomeArquivo, "r");
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

	/**
	 * M�todo para retornar objeto caso seja localizado no registro
	 * 
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	public Aluno getRegistro(int codigo) throws Exception {
		RandomAccessFile file = new RandomAccessFile(nomeArquivo, "rw");
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
		RandomAccessFile file = new RandomAccessFile(nomeArquivo, "r");
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
