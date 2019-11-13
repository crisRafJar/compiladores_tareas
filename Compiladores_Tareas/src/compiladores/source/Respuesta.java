package compiladores.source;

import java.util.List;

public class Respuesta {
	private String resultadoLexico;
	private List<TokenClass> ListTokenClass;
	
	public String getResultadoLexico() {
		return resultadoLexico;
	}
	public void setResultadoLexico(String resultadoLexico) {
		this.resultadoLexico = resultadoLexico;
	}
	public List<TokenClass> getListTokenClass() {
		return ListTokenClass;
	}
	public void setListTokenClass(List<TokenClass> listTokenClass) {
		ListTokenClass = listTokenClass;
	}
}
