package compiladores.source;

public class TokenClass {
	public String tipo;
	public String valor;
	
	public TokenClass() {
	}
	
	public TokenClass(String tipo, String valor) {
		this.tipo = tipo;
		this.valor = valor;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	
}
