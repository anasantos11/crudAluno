/**
 * @authors Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class App {
	private static int quantAlunos;
	private static int cont = 0 ;
	private static int codigo = 0;
	private static int idade = 0;
	private static double notaMedia = 0;
	private static String nome = "";
	private static String curso = "";
	private static Aluno aluno = null;
	private static List<Aluno> listaAlunos = new ArrayList<Aluno>();
	private static Arquivo arquivo;

	public static void main(String[] args) throws Exception {
		arquivo = new Arquivo();
		if (args.length == 0) {
			menuPrograma(-1);
		} else {
			//String cmd = args[0];
			menuPrograma(-1);
		}
	}
	
	/**
	 * Menu para utilizacao do programa
	 * @param escolha
	 * @return
	 * @throws Exception
	 */
	public static int menuPrograma(int escolha) throws Exception {
		@SuppressWarnings("unused")
		String dados = null;

		String x = JOptionPane.showInputDialog(null,
				"Escolha uma das opções abaixo: \n  0  -  Encerrar programa \n\n  CONSULTAS: \n" 
						+ "\n  1  -  Consultar Registros Sem Lapide" 
						+ "\n  2  -  Consultar Registros Com Lapide" 
						+ "\n  3  -  Consultar Indices Sem Lapide" 
						+ "\n  4  -  Consultar Indices Com Lapide"
						+ "\n  5  -  Consultar Registro Específico"
						+ "\n\nDEMAIS FUNCIONALIDADES \n" 
						+ "\n  6  -  Criar Registro" 
						+ "\n  7  -  Alterar Registro" 
						+ "\n  8  -  Remover Registro"
						+ "\n  9  -  Ordenar Registros Crescente "
						+ "\n 10  -  Ordenar Registros Decrescente \n");
		if (x == null) {
			JOptionPane.showMessageDialog(null, "Encerrando programa...");
			escolha = 0;
		} else if (x.equals("")) {
			escolha = 556454532;
		} else {
			try {
				escolha = Integer.parseInt(x);
			} catch (NumberFormatException e) {
				escolha = 556454532;
			}
		}

		switch (escolha) {
		case 0: // Encerrar programa
			JOptionPane.showMessageDialog(null, "Até logo !!!!");
			return 0;
		case 1: // Consultar Registros Sem Lapide
			consultarRegistrosSemLapide();
			menuPrograma(-1);
			break;
		case 2: // Consultar Registros Com Lapide
			consultarRegistrosComLapide();
			menuPrograma(-1);
			break;
		case 3: // Consultar Indices Sem Lapide
			consultarIndicesSemLapide();
			menuPrograma(-1);
			break;
		case 4: // Consultar Indices Com Lapide
			consultarIndicesComLapide();
			menuPrograma(-1);
			break;
		case 5: // Consultar registro especifico
			aluno = buscarRegistroEspecifico();
			if(aluno != null) {
				JOptionPane.showMessageDialog(null, "Registro buscado: " + "\n\n" +  aluno.toString());
			}
			menuPrograma(-1);
			break;

		case 6: // Criar Registro
			registrarAlunos();
			Arquivo.setDadosRegistro(null);
			Arquivo.setDadosRegistroLapide(null);
			Arquivo.setDadosIndices(null);
			Arquivo.setDadosIndicesLapide(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			consultarRegistrosSemLapide();
			menuPrograma(-1);
			break;

		case 7: // Alterar Registro
			alterarRegistro();
			Arquivo.setDadosRegistro(null);
			Arquivo.setDadosRegistroLapide(null);
			Arquivo.setDadosIndices(null);
			Arquivo.setDadosIndicesLapide(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			consultarRegistrosSemLapide();
			menuPrograma(-1);
			break;

		case 8: // Remover Registro
			excluirRegistro();
			Arquivo.setDadosRegistro(null);
			Arquivo.setDadosRegistroLapide(null);
			Arquivo.setDadosIndices(null);
			Arquivo.setDadosIndicesLapide(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			consultarRegistrosSemLapide();
			menuPrograma(-1);
			break;
		case 9: // Ordenar Registros
			arquivo.organizarIndice(TipoOrdenacao.CodigoCrescente);
			Arquivo.setDadosRegistro(null);
			Arquivo.setDadosRegistroLapide(null);
			Arquivo.setDadosIndices(null);
			Arquivo.setDadosIndicesLapide(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			consultarRegistrosSemLapide();
			menuPrograma(-1);
			break;
		case 10: // Ordenar Registros
			arquivo.organizarIndice(TipoOrdenacao.CodigoDecrescente);
			Arquivo.setDadosRegistro(null);
			Arquivo.setDadosRegistroLapide(null);
			Arquivo.setDadosIndices(null);
			Arquivo.setDadosIndicesLapide(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			consultarRegistrosSemLapide();
			menuPrograma(-1);
			break;
		default:
			JOptionPane.showMessageDialog(null, "ATENÇÃO - Opção inválida, verifique o menu.");
			menuPrograma(-1);
			break;
		}
		return 0;
	}

	/**
	 * Metodo para pegar dados do aluno para ser registrado 
	 */
	public static void pegarDadosAlunos() {
		quantAlunos = Integer
				.parseInt(JOptionPane.showInputDialog(null, "Digite a quantidade de alunos que deseja cadastrar"));
		cont = 0;
		while (cont < quantAlunos) {
			codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite a matricula"));
			nome = JOptionPane.showInputDialog(null, "Digite o nome");
			idade = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite a idade"));
			curso = JOptionPane.showInputDialog(null, "Digite o nome do curso");
			notaMedia = Double.parseDouble(JOptionPane.showInputDialog(null, "Digite a nota média"));
			listaAlunos.add(new Aluno(codigo, nome, idade, curso, notaMedia));
			cont++;
		}
	}
	
	/**
	 * Metodo para criar registro dos alunos inseridos
	 * @throws Exception
	 */
	public static void registrarAlunos() throws Exception {
		pegarDadosAlunos();
		if (listaAlunos.size() > 0) {
			listaAlunos.forEach(x -> {
				try {
					arquivo.criarRegistro(x, -1);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
				}
			});
		}

	}
	
	/**
	 * Metodo para listar os indices sem lapide
	 * @throws IOException
	 */
	public static void consultarIndicesSemLapide() throws IOException {
		try {
		JTextArea textArea = new JTextArea(arquivo.getStringIndices());
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		scrollPane.setPreferredSize( new Dimension( 350, 450 ) );
		JOptionPane.showMessageDialog(null, scrollPane);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Metodo para listar os indices com lapide
	 * @throws IOException
	 */
	public static void consultarIndicesComLapide() throws IOException {
		try {
		JTextArea textArea = new JTextArea(arquivo.getStringIndicesLapide());
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		scrollPane.setPreferredSize( new Dimension( 350, 450 ) );
		JOptionPane.showMessageDialog(null, scrollPane);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 *  Metodo para listar os registros do arquivo sem lapide
	 * @throws IOException
	 */
	public static void consultarRegistrosSemLapide() throws IOException {
		try {
		JTextArea textArea = new JTextArea(arquivo.getStringRegistros());
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		scrollPane.setPreferredSize( new Dimension( 800, 600 ) );
		JOptionPane.showMessageDialog(null, scrollPane);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 *  Metodo para listar os registros do arquivo com lapide
	 * @throws IOException
	 */
	public static void consultarRegistrosComLapide() throws IOException {
		try {
		JTextArea textArea = new JTextArea(arquivo.getStringRegistrosLapide());
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		scrollPane.setPreferredSize( new Dimension( 800, 600 ) );
		JOptionPane.showMessageDialog(null, scrollPane);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Metodo para buscar registro a partir do codigo/matricula
	 * @return
	 */
	public static Aluno buscarRegistroEspecifico() {
		String y = "";
		int matricula = -1;
		try {
			while (y != null) {
				y = JOptionPane.showInputDialog(null, "Digite o código da matrícula que deseja consultar.",
						"Buscar registro", JOptionPane.INFORMATION_MESSAGE);
				if (y == null) {
					JOptionPane.showMessageDialog(null, "Retornando ao menu...");
				} else if (y.equals("")) {
					JOptionPane.showMessageDialog(null, "ATENÇÃO - Formato inválido!");
				} else {
					try {
						matricula = Integer.parseInt(y);
						y = null;
						return arquivo.getRegistroByCode(matricula);
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "ATENÇÃO - Formato inválido!");
					}
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
		}

		return null;
	}

	/**
	 * Metodo para realizar processo de alteração do registro
	 */
	public static void alterarRegistro() {
		Aluno anterior = buscarRegistroEspecifico();
		if (anterior != null) {
			nome = JOptionPane.showInputDialog(null, "Registro atual = " + anterior.getString() + "\n\n" + "Digite o nome");
			idade = Integer.parseInt(JOptionPane.showInputDialog(null, "Registro atual = " + anterior.getIdade() + "\n\n" + "Digite a idade"));
			curso = JOptionPane.showInputDialog(null, "Registro atual = " + anterior.getCurso() + "\n\n" + "Digite o nome do curso");
			notaMedia = Double.parseDouble(JOptionPane.showInputDialog(null,  "Registro atual = " + anterior.getNotaMedia() + "\n\n" + "Digite a nota média"));
			Aluno novoRegistro = new Aluno();
			novoRegistro.setCodigo(anterior.getCodigo());
			novoRegistro.setIdade(idade);
			novoRegistro.setString(nome);
			novoRegistro.setCurso(curso);
			novoRegistro.setNotaMedia(notaMedia);

			int resposta = JOptionPane.showConfirmDialog(null,
					"Confirma alteração do registro: " + "\n\n" + "Registro anterior: " + anterior.toString() + "\n\n"
							+ "Registro alterado: " + novoRegistro.toString());
			if (resposta == JOptionPane.YES_OPTION) {
				try {
					arquivo.atualizarRegistro(anterior, novoRegistro, anterior.getCodigo());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			} else if (resposta == JOptionPane.NO_OPTION) {
				return;
			} else if (resposta == JOptionPane.CANCEL_OPTION) {
				return;
			}

		}

	}
	
	/**
	 * Metodo para excluir determinado registro
	 */
	public static void excluirRegistro() {
		Aluno anterior = buscarRegistroEspecifico();
		if (anterior != null) {
			int resposta = JOptionPane.showConfirmDialog(null,
					"Confirma exclusão do registro: " + "\n\n" + anterior.toString() + "\n\n");
			if (resposta == JOptionPane.YES_OPTION) {
				try {
					arquivo.inativarRegistro(anterior);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			} else if (resposta == JOptionPane.NO_OPTION) {
				return;
			} else if (resposta == JOptionPane.CANCEL_OPTION) {
				return;
			}

		}

	}
	

}
