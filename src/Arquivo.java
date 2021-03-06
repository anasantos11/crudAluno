/**
 * @authors Ana Paula dos Santos and Luiz Henrique Silva Jesus
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
	private static String dadosRegistroLapide;
	private static String dadosIndices;
	private static String dadosIndicesLapide;
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
	 * M�todo para criar indice primario dos registros de alunos
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
	 * Metodo retornara List<Index> dos indices COM e SEM lapide Usado para
	 * organizar indices
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Index> getListIndices() throws Exception {
		indice = null;
		listaIndices = new ArrayList<Index>();
		try {
			fileIndice = new RandomAccessFile(this.indexador, "r");
			while (fileIndice.getFilePointer() < fileIndice.length()) {
				byte[] b = new byte[TAM_INDEX];
				fileIndice.read(b);
				indice = new Index();
				indice.setByteArray(b);
				/*
				 * if (indice.getLapide() != 1) { listaIndices.add(indice); }
				 */
				listaIndices.add(indice);
			}
			fileIndice.close();
		} catch (FileNotFoundException e) {
			throw new Exception(e.getMessage());
		}
		return listaIndices;
	}

	/**
	 * Metodo retornara String dos indices SEM lapide
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getStringIndices() throws IOException {
		if (dadosIndices == null) {
			indice = null;
			dadosIndices = "";
			try {
				fileIndice = new RandomAccessFile(this.indexador, "r");
				while (fileIndice.getFilePointer() < fileIndice.length()) {
					b = new byte[TAM_INDEX];
					fileIndice.read(b);
					indice = new Index();
					indice.setByteArray(b);
					if (indice.getLapide() != 1) {
						dadosIndices += indice.toString();
					}
				}
				fileIndice.close();
			} catch (FileNotFoundException e) {
				return "N�o existe nenhum index";
			}
		}
		return dadosIndices;
	}

	/**
	 * Metodo retornara String dos indices COM lapide
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getStringIndicesLapide() throws IOException {
		if (dadosIndicesLapide == null) {
			indice = null;
			dadosIndicesLapide = "";
			try {
				fileIndice = new RandomAccessFile(this.indexador, "r");
				while (fileIndice.getFilePointer() < fileIndice.length()) {
					b = new byte[TAM_INDEX];
					fileIndice.read(b);
					indice = new Index();
					indice.setByteArray(b);
					if (indice.getLapide() == 1) {
						dadosIndicesLapide += indice.toString();
					}
				}
				fileIndice.close();
			} catch (FileNotFoundException e) {
				return "N�o existe nenhum index";
			}
		}
		return dadosIndicesLapide;
	}

	/**
	 * Metodo retornara List<Alunos> com registros SEM lapide
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<Aluno> getListRegistros() throws IOException {
		if (listaAlunos == null) {
			indice = null;
			fileIndice = new RandomAccessFile(this.indexador, "r");
			fileDB = new RandomAccessFile(db, "r");
			listaAlunos = new ArrayList<Aluno>();
			while (fileIndice.getFilePointer() < fileIndice.length()) {
				// Pega posicao no indice
				b = new byte[TAM_INDEX];
				fileIndice.read(b);
				indice = new Index();
				indice.setByteArray(b);
				//if (indice.getLapide() != 1) {
					// Pula para posicao recuperada no indice
					fileDB.seek(indice.getPosicaoArquivo());
					// Pega registro procurado
					b = new byte[fileDB.readInt()];
					fileDB.read(b);
					aluno = new Aluno();
					aluno.setByteArray(b);
					// Adiciona na lista de alunos o registro
					listaAlunos.add(aluno);
				//}
			}
			fileIndice.close();
			fileDB.close();
		}
		return listaAlunos;
	}

	/**
	 * Metodo retornara String dos registros SEM lapide
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getStringRegistros() throws Exception {
		if (dadosRegistro == null) {
			getListRegistros().forEach(x -> {
				if (x.getStatus() != 1) {
					if (dadosRegistro == null) {
						dadosRegistro = x.toString();
					} else {
						dadosRegistro = dadosRegistro + "\n" + x.toString();
					}
				}
			});
		}
		return dadosRegistro;
	}
	
	
	/**
	 * Metodo retornara String dos registros COM lapide
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getStringRegistrosLapide() throws Exception {
		if (dadosRegistroLapide == null) {
			getListRegistros().forEach(x -> {
				if (x.getStatus() == 1) {
					if (dadosRegistroLapide == null) {
						dadosRegistroLapide = x.toString();
					} else {
						dadosRegistroLapide = dadosRegistroLapide + "\n" + x.toString();
					}
				}
			});
		}
		return dadosRegistroLapide;
	}
	
	/**
	 * Metodo para retornar objeto Aluno caso seja localizado no registro
	 * 
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	public Aluno getRegistroByCode(int codigo) throws Exception {
		// Pegar no indice a posicao no registro
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
			ponteiroAnteriorIndex = fileIndice.getFilePointer();
			fileIndice.read(b);
			indice.setByteArray(b);
			if (indice.getCodigo() == code && indice.getLapide() != 1) {
				ponteiroAnterior = indice.getPosicaoArquivo();
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
		 * ("N�o foi encontrado o registro no �ndice."); } Aluno a =
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

	
	/**
	 * Metodo ira organizar indice pelo codigo/matricula
	 * 
	 * @param tipo
	 * @throws Exception
	 */
	public void organizarIndice(TipoOrdenacao tipo) throws Exception {
		if (listaIndices == null) {
			listaIndices = getListIndices();
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
		@SuppressWarnings("resource")
		FileChannel src = new FileInputStream(tmpFile).getChannel();
		@SuppressWarnings("resource")
		FileChannel dest = new FileOutputStream(this.indexador, false).getChannel();
		dest.transferFrom(src, 0, src.size());

		temp.close();
		src.close();
		dest.close();
		tmpFile.deleteOnExit();

	}

	/**
	 * Setters
	 * 
	 * @param listaAlunos
	 */

	public static void setListaAlunos(List<Aluno> listaAlunos) {
		Arquivo.listaAlunos = listaAlunos;
	}

	
	public static void setDadosRegistro(String dadosRegistro) {
		Arquivo.dadosRegistro = dadosRegistro;
	}

	
	public static void setDadosIndices(String dadosIndices) {
		Arquivo.dadosIndices = dadosIndices;
	}


	public static void setDadosRegistroLapide(String dadosRegistroLapide) {
		Arquivo.dadosRegistroLapide = dadosRegistroLapide;
	}



	public static void setDadosIndicesLapide(String dadosIndicesLapide) {
		Arquivo.dadosIndicesLapide = dadosIndicesLapide;
	}

	public static void setListaIndices(List<Index> listaIndices) {
		Arquivo.listaIndices = listaIndices;
	}

	/**
	 * Metodo para criar registros a partir do arquivo nomes.txt Deve ser executado
	 * apenas se n�o existir arquivos bd e indexes
	 * 
	 * @throws IOException
	 */
	public void criarBase() throws IOException {
		RandomAccessFile nomes = new RandomAccessFile("nomes.txt", "rw");
		String[] registro = null;

		Aluno a = null;

		for (int i = 0; i < 209; i++) {
			registro = nomes.readLine().split(";");
			a = new Aluno(Integer.parseInt(registro[0]), registro[1], Integer.parseInt(registro[2]), registro[4],
					Double.parseDouble(registro[3]));
			criarRegistro(a, -1);
		}

		nomes.close();
	}


}
