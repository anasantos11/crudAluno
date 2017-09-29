/**
 * @authors Ana Paula dos Santos and Luiz Henrique Silva Jesus
 */
import java.util.Comparator;

public class ComparatorIndex implements Comparator<Index> {
	private TipoOrdenacao ordem;

	public void tipoOrdenacao(TipoOrdenacao ordem) {
		this.ordem = ordem;
	}

	@Override
	public int compare(Index indice1, Index indice2) {
		switch (ordem) {
		case CodigoCrescente:
			return indice1.getCodigo().compareTo(indice2.getCodigo());
		case CodigoDecrescente:
			return - indice1.getCodigo().compareTo(indice2.getCodigo());
		default:
			throw new RuntimeException("Não é possível ordenar por esse tipo");
		}
	}
}