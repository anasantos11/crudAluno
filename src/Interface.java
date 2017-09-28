/**
 * @author Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import net.miginfocom.swing.MigLayout;

public class Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Arquivo arquivo;
	private JTabbedPane aba;
	private JPanel tela;
	private JPanel cadastroAlunos;
	private JLabel tituloCadastroCliente;

	private JLabel labelNome;
	private JLabel labelCPF;
	private JLabel labelRG;
	private JLabel labelDataNascimento;
	private JLabel labelTelefone;
	private JLabel labelEmail;

	private JLabel labelRUA;
	private JLabel labelNumero;
	private JLabel labelBairro;
	private JLabel labelEstado;
	private JLabel labelCidade;
	private JLabel labelCep;

	private JTextField nome;
	private JTextField cpf;
	private JTextField rgCliente;
	private JTextField dataNascimentoCliente;
	private JTextField telefoneCliente;
	private JTextField emailCliente;

	private JTextField ruaCliente;
	private JTextField numeroCliente;
	private JTextField estadoCliente;
	private JTextField cidadeCliente;
	private JTextField bairroCliente;
	private JTextField cepCliente;

	private JButton btnCadastrarCliente;
	private JPanel listaAlunos;
	private JLabel lblClientesCadastrados;
	private JButton buttonListarClientes;
	private JEditorPane clientesCadastrados;
	private JLabel lblMatrcula;
	private JTextField matricula;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public Interface() throws Exception {
		// Metodos metodos = Metodos.getInstance();
		// Services service = Services.getInstance(); 
		arquivo = new Arquivo();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 525, 524);
		tela = new JPanel();
		tela.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(tela);
		tela.setLayout(new GridLayout(1, 0, 0, 0));

		aba = new JTabbedPane(JTabbedPane.TOP);
		tela.add(aba);

		listaAlunos = new JPanel();
		aba.addTab("Alunos", null, listaAlunos, null);
		listaAlunos.setLayout(new MigLayout("", "[138px,grow][138px,grow]", "[19px][grow]"));

		lblClientesCadastrados = new JLabel("ALUNOS CADASTRADOS     ");
		lblClientesCadastrados.setFont(new Font("Tahoma", Font.BOLD, 13));
		listaAlunos.add(lblClientesCadastrados, "cell 0 0,alignx right,aligny center");

		buttonListarClientes = new JButton("Listar Alunos");
		buttonListarClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

					clientesCadastrados.setText("Não implementado");

				
			}
		});
		buttonListarClientes.setForeground(new Color(0, 0, 128));
		buttonListarClientes.setFont(new Font("Tahoma", Font.BOLD, 13));
		listaAlunos.add(buttonListarClientes, "cell 1 0,alignx left,aligny center");

		clientesCadastrados = new JEditorPane();
		listaAlunos.add(clientesCadastrados, "cell 0 1 2 1,grow");

		cadastroAlunos = new JPanel();
		aba.addTab("Cadastrar Alunos", null, cadastroAlunos, null);
		cadastroAlunos.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("79px"),
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("137px"), ColumnSpec.decode("10dlu"),
						ColumnSpec.decode("center:max(24dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("19px"), RowSpec.decode("28px"),
						RowSpec.decode("20px"), FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("20px"),
						FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("20px"),
						FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("20px"),
						FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("20px"),
						FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("20px"), RowSpec.decode("20px"),
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px"), FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
						RowSpec.decode("20px"), FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("20px"),
						FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("20px"), }));

		tituloCadastroCliente = new JLabel("CADASTRO DO ALUNO");
		tituloCadastroCliente.setFont(new Font("Tahoma", Font.BOLD, 15));
		cadastroAlunos.add(tituloCadastroCliente, "2, 2, 7, 1, center, top");

		lblMatrcula = new JLabel("Matr\u00EDcula:");
		cadastroAlunos.add(lblMatrcula, "2, 4, left, default");

		matricula = new JTextField();
		matricula.setColumns(10);
		cadastroAlunos.add(matricula, "4, 4, fill, center");

		labelNome = new JLabel("Nome:");
		cadastroAlunos.add(labelNome, "6, 4, left, center");

		nome = new JTextField();
		cadastroAlunos.add(nome, "8, 4, fill, center");
		nome.setColumns(20);

		labelCPF = new JLabel("CPF:");
		cadastroAlunos.add(labelCPF, "2, 6, left, center");

		cpf = new JTextField();
		cadastroAlunos.add(cpf, "4, 6, fill, center");
		cpf.setColumns(10);

		labelRG = new JLabel("RG:");
		cadastroAlunos.add(labelRG, "6, 6, fill, center");

		rgCliente = new JTextField();
		rgCliente.setColumns(10);
		cadastroAlunos.add(rgCliente, "8, 6, fill, top");

		labelDataNascimento = new JLabel("DataNascimento");
		cadastroAlunos.add(labelDataNascimento, "2, 8, left, center");

		dataNascimentoCliente = new JTextField();
		dataNascimentoCliente.setColumns(10);
		cadastroAlunos.add(dataNascimentoCliente, "4, 8, fill, center");

		labelTelefone = new JLabel("Telefone");
		cadastroAlunos.add(labelTelefone, "2, 12, left, center");

		telefoneCliente = new JTextField();
		telefoneCliente.setColumns(20);
		cadastroAlunos.add(telefoneCliente, "4, 12, fill, center");

		labelEmail = new JLabel("E-mail:");
		cadastroAlunos.add(labelEmail, "6, 12, left, center");

		emailCliente = new JTextField();
		emailCliente.setColumns(20);
		cadastroAlunos.add(emailCliente, "8, 12, fill, center");

		labelRUA = new JLabel("Rua:");
		cadastroAlunos.add(labelRUA, "2, 15, left, center");

		ruaCliente = new JTextField();
		cadastroAlunos.add(ruaCliente, "4, 15, fill, center");
		ruaCliente.setColumns(10);

		labelNumero = new JLabel("N\u00FAmero:");
		cadastroAlunos.add(labelNumero, "6, 15, left, center");

		numeroCliente = new JTextField();
		cadastroAlunos.add(numeroCliente, "8, 15, fill, center");
		numeroCliente.setColumns(10);

		labelBairro = new JLabel("Bairro:");
		cadastroAlunos.add(labelBairro, "2, 17, left, center");

		bairroCliente = new JTextField();
		cadastroAlunos.add(bairroCliente, "4, 17, fill, center");
		bairroCliente.setColumns(10);

		labelCep = new JLabel("Cep:");
		cadastroAlunos.add(labelCep, "6, 17, left, center");

		cepCliente = new JTextField();
		cadastroAlunos.add(cepCliente, "8, 17, fill, center");
		cepCliente.setColumns(10);

		labelCidade = new JLabel("Cidade:");
		cadastroAlunos.add(labelCidade, "2, 19, left, center");

		cidadeCliente = new JTextField();
		cadastroAlunos.add(cidadeCliente, "4, 19, fill, center");
		cidadeCliente.setColumns(10);

		labelEstado = new JLabel("Estado:");
		cadastroAlunos.add(labelEstado, "6, 19, left, center");

		estadoCliente = new JTextField();
		cadastroAlunos.add(estadoCliente, "8, 19, fill, center");
		estadoCliente.setColumns(10);

		btnCadastrarCliente = new JButton("Cadastrar");
		btnCadastrarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					nome.setText(null);
					cpf.setText(null);
					rgCliente.setText(null);
					dataNascimentoCliente.setText(null);
					telefoneCliente.setText(null);
					emailCliente.setText(null);
					ruaCliente.setText(null);
					numeroCliente.setText(null);
					bairroCliente.setText(null);
					cepCliente.setText(null);
					cidadeCliente.setText(null);
					estadoCliente.setText(null);

				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Dados preenchidos com formato incorreto, tente novamente.",
							"Erro preenchimento formulário", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		btnCadastrarCliente.setForeground(new Color(0, 0, 128));
		btnCadastrarCliente.setFont(new Font("Tahoma", Font.BOLD, 15));
		cadastroAlunos.add(btnCadastrarCliente, "4, 23, fill, top");

	}

}
