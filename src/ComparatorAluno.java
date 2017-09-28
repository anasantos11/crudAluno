import java.util.Comparator;

public class ComparatorAluno implements Comparator<Aluno> {
	private TipoOrdenacao ordem;

	public void tipoOrdenacao(TipoOrdenacao ordem) {
		this.ordem = ordem;
	}

	@Override
	public int compare(Aluno person1, Aluno person2) {
		switch (ordem) {
		case Codigo:
			return person1.getCodigo().compareTo(person2.getCodigo());
		case Nome:
			return person1.getString().compareTo(person2.getString());
		default:
			throw new RuntimeException("Não é possível ordenar por esse tipo");
		}

	}

}