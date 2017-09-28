/**
 * @author Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;

public class Aplicacao {
	private static int quantAlunos;
	private static List<Aluno> listaAlunos = new ArrayList<Aluno>();
	private static Arquivo arquivo;

	public static void main(String[] args) throws Exception {
		try {
			arquivo = new Arquivo();
			/*
			List<Index> listaIndices = arquivo.getListIndexes();
			System.out.println("Lista anterior: \n");
			listaIndices.forEach( x ->{
				System.out.println(x.toString());
			});
			ComparatorIndex comparator = new ComparatorIndex();
			comparator.tipoOrdenacao(TipoOrdenacao.Lapide);
			Collections.sort(listaIndices, comparator); 
			System.out.println("Nova Lista: \n");
			listaIndices.forEach( x ->{
				System.out.println(x.toString());
			});*/
			
			arquivo.criarBase();
			
			/*try {
//				pegarDadosAlunos();
//				Aluno bruna = new Aluno(58,"Daniela");
//				arquivo.atualizarRegistro(bruna, 2);
//				arquivo.pegarRegistroEspecifico(1);
//				arquivo.atualizarRegistroEspecifico(4);
//				arquivo.inativarRegistro(1010);
//				Aluno a = new Aluno(1, "Teste");
//				arquivo.atualizarRegistro(bruna, 58);
//				arquivo.ativarRegistro(1010);
				
				JOptionPane.showMessageDialog(null, arquivo.listarRegistros(), "Alunos cadastrados",
						JOptionPane.INFORMATION_MESSAGE);
				
				JOptionPane.showMessageDialog(null, arquivo.listarIndexes(), "Indexes cadastrados",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
			}*/
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Mensagem", JOptionPane.WARNING_MESSAGE);
		} finally {
			JOptionPane.showMessageDialog(null, "Programa encerrado!", "", JOptionPane.WARNING_MESSAGE);
		}

	}

	public static void pegarDadosAlunos() {
		quantAlunos = Integer
				.parseInt(JOptionPane.showInputDialog(null, "Digite a quantidade de alunos que deseja cadastrar"));
		int cont = 0;
		int codigo;
		String nome;
		while (cont < quantAlunos) {
			codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite a matricula"));
			nome = JOptionPane.showInputDialog(null, "Digite o nome");
//			listaAlunos.add(new Aluno(codigo, nome));
			cont++;
		}

		if (listaAlunos.size() > 0) {
			registrarAlunos();
		}
	}

	public static void registrarAlunos() {
		listaAlunos.forEach(x -> {
			try {
				arquivo.criarRegistro(x, -1);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
			}
		});

	}
	
	public static void atualizarAluno() throws Exception {
		int codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite a matricula"));
		String nome = JOptionPane.showInputDialog(null, "Digite o nome");
		int inativo = 1;
		Aluno a = new Aluno();
		a.setCodigo(codigo);
		a.setString(nome);
		a.setStatus(inativo);
		//arquivo.atualizarRegistro(a, 2);
		
	}
}
