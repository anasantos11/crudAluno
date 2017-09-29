package Outros;
/**
 * @authors Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.util.Comparator;

public class ComparatorAluno implements Comparator<Aluno> {
	private TipoOrdenacao ordem;

	public void tipoOrdenacao(TipoOrdenacao ordem) {
		this.ordem = ordem;
	}

	@Override
	public int compare(Aluno person1, Aluno person2) {
		switch (ordem) {
		case CodigoCrescente:
			return person1.getCodigo().compareTo(person2.getCodigo());
		case CodigoDecrescente:
			return - person1.getCodigo().compareTo(person2.getCodigo());
		case Nome:
			return person1.getString().compareTo(person2.getString());
		default:
			throw new RuntimeException("N�o � poss�vel ordenar por esse tipo");
		}

	}

}