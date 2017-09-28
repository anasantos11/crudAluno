import java.util.Comparator;

public class ComparatorIndex implements Comparator<Index> {
	private TipoOrdenacao ordem;

	public void tipoOrdenacao(TipoOrdenacao ordem) {
		this.ordem = ordem;
	}

	@Override
	public int compare(Index indice1, Index indice2) {
		switch (ordem) {
		case Codigo:
			return indice1.getCodigo().compareTo(indice2.getCodigo());
		case Lapide:
			return indice1.getLapide().compareTo(indice2.getLapide());
		default:
			throw new RuntimeException("N�o � poss�vel ordenar por esse tipo");
		}
	}
}