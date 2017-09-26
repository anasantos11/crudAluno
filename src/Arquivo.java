
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JFileChooser;

public class Arquivo {
	private String db;
	private String indexador;
	private static String dados;
	private static long ponteiroAnterior;
	private static long ponteiroAnteriorIndex;
	private static final int TAM_INDEX = Integer.BYTES + Integer.BYTES + Long.BYTES;

	public Arquivo() throws Exception {
		this.db = "bd.txt";
		this.indexador = "indexes.txt";
	}

	/**
	 * Metodo para pegar arquivo do dispositivo utilizado na execuï¿½ï¿½o
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
	 * Metodo para setar novo registro no arquivo, caso receba como parï¿½metro um
	 * seek diferente de -1 ira sobrescrever os dados da posiï¿½ï¿½o passada
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
	 * Mï¿½todo irï¿½ retornar string com os registros do arquivo
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
	
	public void criarIndex(Index i, long seek) throws IOException {
		RandomAccessFile rf = new RandomAccessFile(this.indexador, "rw");
		long posicaoFinal = rf.length();
		if(seek != -1){
			posicaoFinal = seek;
		}
		
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
				byte[] b = new byte[TAM_INDEX];
				rf.read(b);
				i = new Index();
				i.setByteArray(b);
				if(i.getLapide() != 1){
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
	 * Mï¿½todo para retornar objeto caso seja localizado no registro
	 * 
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	public Aluno getRegistro(int codigo) throws Exception {
		RandomAccessFile file = new RandomAccessFile(db, "rw");
		Index i = getIndexbyCode(codigo);
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
			throw new Exception("Registro nï¿½o localizado");
		}
	}
	
	public Index getIndexbyCode (int code) throws IOException{
		Index i = new Index();
		byte[] b = new byte[TAM_INDEX];
		RandomAccessFile rf = new RandomAccessFile(this.indexador, "r");
		
		while(rf.getFilePointer() < rf.length()){
			Arquivo.ponteiroAnteriorIndex = rf.getFilePointer();
			rf.read(b);
			i.setByteArray(b);
			if(i.getCodigo() == code){
				Arquivo.ponteiroAnterior = i.getPosicaoArquivo();
				return i;
			}
		}
		return i;
	}

	/**
	 * Mï¿½todo para setar lï¿½pide como inativa em determinado registro que serï¿½
	 * pesquisado pelo codigo
	 * 
	 * @param codigo
	 * @throws Exception
	 */
	public void inativarRegistro(int codigo) throws Exception {
		Index i = getIndexbyCode(codigo);
		Aluno a = getRegistro(codigo);
		i.setLapide(1);
		a.setStatus(1);
		criarRegistro(a, ponteiroAnterior);
		criarIndex(i, ponteiroAnteriorIndex);
	}
	
	/**
	 * Mï¿½todo para setar lï¿½pide como ativa em determinado registro que serï¿½
	 * pesquisado pelo codigo
	 * 
	 * @param codigo
	 * @throws Exception
	 */
	public void ativarRegistro(int codigo) throws Exception {
		Aluno a = getRegistro(codigo);
		Index i = getIndexbyCode(codigo);
		a.setStatus(0);
		i.setLapide(0);
		criarRegistro(a, ponteiroAnterior);
		criarIndex(i, ponteiroAnteriorIndex);
	}

	/**
	 * Mï¿½todo para atualizar um registro, ele setarï¿½ lï¿½pide no registro anterior e
	 * criarï¿½ um novo no arquivo na ï¿½ltima posiï¿½ï¿½o
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
