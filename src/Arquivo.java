
/**
 * @author Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arquivo {
	private String db;
	private String indexador;
	public static Index indice;
	private static Aluno aluno;
	private static byte[] b;
	private static RandomAccessFile fileDB;
	private static RandomAccessFile fileIndice;
	private static List<Aluno> listaAlunos;
	private static List<Index> listaIndices;
	private static String dadosRegistro;
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
	 * Metodo retornara string com os dados do indice primario que nao tem lapide
	 * 
	 * @return
	 * @throws IOException
	 */
	public String listarIndexes() throws IOException {
		indice = null;
		String dadosIndex = "";
		try {
			fileIndice = new RandomAccessFile(this.indexador, "r");
			while (fileIndice.getFilePointer() < fileIndice.length()) {
				byte[] b = new byte[TAM_INDEX];
				fileIndice.read(b);
				indice = new Index();
				indice.setByteArray(b);
				if (indice.getLapide() != 1) {
					dadosIndex += indice.toString();
				}
			}
			fileIndice.close();
		} catch (FileNotFoundException e) {
			return "Não existe nenhum index";
		}
		return dadosIndex;
	}

	/**
	 * Metodo retornara List<Index> com os dados do indice c/ e s/ lapide
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Index> getListIndexes() throws Exception {
		indice = null;
		listaIndices = new ArrayList<Index>();
		try {
			RandomAccessFile rf = new RandomAccessFile(this.indexador, "r");
			while (rf.getFilePointer() < rf.length()) {
				byte[] b = new byte[TAM_INDEX];
				rf.read(b);
				indice = new Index();
				indice.setByteArray(b);

				/*
				 * if (indice.getLapide() != 1) { listaIndices.add(indice); }
				 */

				listaIndices.add(indice);
			}
			rf.close();
		} catch (FileNotFoundException e) {
			throw new Exception(e.getMessage());
		}
		return listaIndices;
	}

	public String imprimirDados() throws Exception {
		if (dadosRegistro == null) {
			getDados().forEach(x -> {
				if (dadosRegistro == null) {
					dadosRegistro = x.toString();
				} else {
					dadosRegistro = dadosRegistro + "\n" + x.toString();
				}
			});

		}
		// JOptionPane.showMessageDialog(null, dadosRegistro);
		return dadosRegistro;
	}

	/**
	 * Metodo retornara List<Alunos> com os dados do arquivo
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<Aluno> getDados() throws IOException {
		if (listaAlunos == null) {
			indice = null;
			fileIndice = new RandomAccessFile(this.indexador, "r");
			fileDB = new RandomAccessFile(db, "r");
			listaAlunos = new ArrayList<Aluno>();
			while (fileIndice.getFilePointer() < fileIndice.length()) {
				// Pegando posicao no indice
				b = new byte[TAM_INDEX];
				fileIndice.read(b);
				indice = new Index();
				indice.setByteArray(b);
				if (indice.getLapide() != 1) {
					// Pula para posicao recuperada no indice
					fileDB.seek(indice.getPosicaoArquivo());
					// Pegando registro procurado
					b = new byte[fileDB.readInt()];
					fileDB.read(b);
					aluno = new Aluno();
					aluno.setByteArray(b);
					listaAlunos.add(aluno);
				}
			}
			fileIndice.close();
			fileDB.close();
		}
		return listaAlunos;
	}

	/**
	 * Metodo retornara List<Alunos> com os registros do arquivo
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<Aluno> getListRegistros() throws IOException {
		if (listaAlunos == null) {
			RandomAccessFile file = new RandomAccessFile(db, "r");
			listaAlunos = new ArrayList<Aluno>();
			while (file.getFilePointer() < file.length()) {
				int tamanho = file.readInt();
				byte[] b = new byte[tamanho];
				file.read(b);
				Aluno aluno = new Aluno();
				aluno.setByteArray(b);
				if (aluno != null) {
					listaAlunos.add(aluno);
				}
			}
			file.close();
		}
		return listaAlunos;
	}

	/**
	 * Metodo para retornar objeto Aluno caso seja localizado no registro
	 * 
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	public Aluno getRegistroByCode(int codigo) throws Exception {
		indice = getIndexbyCode(codigo);
		fileDB = new RandomAccessFile(db, "rw");
		long seek = (indice == null) ? -1 : indice.getPosicaoArquivo();
		if (seek != -1) {
			fileDB.seek(seek);
			int tamanho = fileDB.readInt();
			b = new byte[tamanho];
			fileDB.read(b);
			aluno = new Aluno();
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
		fileIndice = new RandomAccessFile(this.indexador, "r");
		indice = new Index();
		b = new byte[TAM_INDEX];

		while (fileIndice.getFilePointer() < fileIndice.length()) {
			Arquivo.ponteiroAnteriorIndex = fileIndice.getFilePointer();
			fileIndice.read(b);
			indice.setByteArray(b);
			if (indice.getCodigo() == code && indice.getLapide() != 1) {
				Arquivo.ponteiroAnterior = indice.getPosicaoArquivo();
				return indice;
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
		/*
		 * Index i = getIndexbyCode(codigo); if(i == null) { throw new Exception
		 * ("Não foi encontrado o registro no índice."); } Aluno a =
		 * getRegistro(codigo);
		 */
		indice.setLapide(1);
		a.setStatus(1);
		criarRegistro(a, Arquivo.ponteiroAnterior);
		criarIndex(indice, Arquivo.ponteiroAnteriorIndex);
	}

	/**
	 * Metodo para atualizar um registro, ele colocara lapide no registro anterior e
	 * criara um novo no arquivo na ultima posicao
	 * 
	 * @param r
	 * @param codigo
	 * @throws Exception
	 */
	public void atualizarRegistro(Registro anterior, Registro novo, int codigo) throws Exception {
		inativarRegistro(anterior);
		criarRegistro(novo, -1);
	}

	public void organizarArquivo(TipoOrdenacao tipo) throws IOException {
		if (listaAlunos == null) {
			listaAlunos = getDados();
		}
		ComparatorAluno comparator = new ComparatorAluno();
		File tmpFile = File.createTempFile("arquivo", ".tmp");
		RandomAccessFile temp = new RandomAccessFile(tmpFile, "rw");
		long seek;

		comparator.tipoOrdenacao(tipo);
		Collections.sort(listaAlunos, comparator);

		for (Aluno r : listaAlunos) {
			seek = tmpFile.length();
			temp.seek(seek);
			temp.writeInt(r.getByteArray().length);
			temp.write(r.getByteArray());
		}
		FileChannel src = new FileInputStream(tmpFile).getChannel();
		FileChannel dest = new FileOutputStream(db, false).getChannel();
		dest.transferFrom(src, 0, src.size());
		// FileOutputStream arquivo = new FileOutputStream(db, false);

		temp.close();
		src.close();
		dest.close();
		tmpFile.deleteOnExit();

	}

	public void organizarIndice(TipoOrdenacao tipo) throws Exception {
		if (listaIndices == null) {
			listaIndices = getListIndexes();
		}
		ComparatorIndex comparator = new ComparatorIndex();
		File tmpFile = File.createTempFile("arquivo", ".tmp");
		RandomAccessFile temp = new RandomAccessFile(tmpFile, "rw");
		long seek;

		comparator.tipoOrdenacao(tipo);
		Collections.sort(listaIndices, comparator);

		for (Index i : listaIndices) {
			seek = tmpFile.length();
			temp.seek(seek);
			temp.write(i.getByteArray());
		}
		FileChannel src = new FileInputStream(tmpFile).getChannel();
		FileChannel dest = new FileOutputStream(this.indexador, false).getChannel();
		dest.transferFrom(src, 0, src.size());
		// FileOutputStream arquivo = new FileOutputStream(db, false);

		temp.close();
		src.close();
		dest.close();
		tmpFile.deleteOnExit();

	}

	/**
	 * Getters and Setters
	 * @param listaAlunos
	 */
	public static void setListaAlunos(List<Aluno> listaAlunos) {
		Arquivo.listaAlunos = listaAlunos;
	}

	public static void setDadosRegistro(String dadosRegistro) {
		Arquivo.dadosRegistro = dadosRegistro;
	}

	public static void setListaIndices(List<Index> listaIndices) {
		Arquivo.listaIndices = listaIndices;
	}

	/**
	 * Metodo para criar registros a partir do arquivo nomes.txt Deve ser executado
	 * apenas se não existir arquivos bd e indexes
	 * 
	 * @throws IOException
	 */
	public void criarBase() throws IOException {
		RandomAccessFile nomes = new RandomAccessFile("nomes.txt", "rw");
		// RandomAccessFile matriculas = new RandomAccessFile("matriculas.txt", "rw");
		String[] registro = null;

		Aluno a = null;

		for (int i = 0; i < 209; i++) {
			registro = nomes.readLine().split(";");
			a = new Aluno(Integer.parseInt(registro[0]), registro[1], 0, Integer.parseInt(registro[2]), registro[4],
					Double.parseDouble(registro[3]));
			criarRegistro(a, -1);
		}

		nomes.close();
		// matriculas.close();
	}

}
