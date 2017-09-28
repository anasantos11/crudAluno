
/**
 * @author Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
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
	private static List<Aluno> listaAlunos = new ArrayList<Aluno>();
	private static Arquivo arquivo;

	public static void main(String[] args) throws Exception {
		arquivo = new Arquivo();

		if (args.length == 0) {
			menuPrograma(-1);
		} else {
			String cmd = args[0];
			menuPrograma(-1);
		}
	}

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
			listaAlunos.add(new Aluno(codigo, nome, 0, idade, curso, notaMedia));
			cont++;
		}
	}

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
			consultarRegistros();
		}

	}

	public static void consultarIndices() throws IOException {
		try {
		JTextArea textArea = new JTextArea(arquivo.listarIndexes());
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		scrollPane.setPreferredSize( new Dimension( 350, 450 ) );
		JOptionPane.showMessageDialog(null, scrollPane);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
		}
	}
	public static void consultarRegistros() throws IOException {
		try {
		JTextArea textArea = new JTextArea(arquivo.imprimirDados());
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		scrollPane.setPreferredSize( new Dimension( 800, 600 ) );
		JOptionPane.showMessageDialog(null, scrollPane);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
		}
	}


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

	public static int menuPrograma(int escolha) throws Exception {
		@SuppressWarnings("unused")
		String dados = null;

		String x = JOptionPane.showInputDialog(null,
				"Escolha uma das opções abaixo: \n  0  -  Encerrar programa \n  1  -  Consultar Registros \n  2  -  Buscar Registro Específico \n  3  -  Consultar Indices"
						+ "\n  4  -  Criar Registro" + "\n  5  -  Alterar Registro" + "\n  6  -  Remover Registro"
						+ "\n  7  -  Ordenar Registros");
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
		case 1: // Consultar Registros
			consultarRegistros();
			menuPrograma(-1);
			break;
		case 2: // Buscar registro especifico
			Aluno a = buscarRegistroEspecifico();
			if(a != null) {
				JOptionPane.showMessageDialog(null, "Registro buscado: " + "\n\n" +  a.toString());
			}

			menuPrograma(-1);
			break;
		case 3: // Consultar Indices
			consultarIndices();
			menuPrograma(-1);
			break;
		case 4: // Criar Registro
			registrarAlunos();
			Arquivo.setDadosRegistro(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			menuPrograma(-1);
			break;

		case 5: // Alterar Registro
			alterarRegistro();
			Arquivo.setDadosRegistro(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			menuPrograma(-1);
			break;

		case 6: // Remover Registro
			excluirRegistro();
			Arquivo.setDadosRegistro(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			menuPrograma(-1);
			break;
		case 7: // Ordenar Registros
			arquivo.organizarIndice(TipoOrdenacao.Codigo);
			Arquivo.setDadosRegistro(null);
			Arquivo.setListaAlunos(null);
			Arquivo.setListaIndices(null);
			consultarRegistros();
			menuPrograma(-1);
			break;
		default:
			JOptionPane.showMessageDialog(null, "ATENÇÃO - Opção inválida, verifique o menu.");
			menuPrograma(-1);
			break;
		}
		return 0;
	}

}
