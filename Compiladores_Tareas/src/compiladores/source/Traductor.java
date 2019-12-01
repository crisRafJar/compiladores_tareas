package compiladores.source;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Traductor {

	private static Stack<TokenClass> pila = new Stack<TokenClass>();
	static TokenClass tokenActual = new TokenClass();

	static boolean error = false;
	
	
	public static void traducir(List<TokenClass> listTokensClass, String archivoSalida) throws Exception {
		File file = new File(archivoSalida);

		// Si el archivo no existe es creado
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);

		TokenClass[] arrayTokens = new TokenClass[listTokensClass.size()];
		int i = 0;
		for (TokenClass token : listTokensClass) {
			if (!token.tipo.equals(" ")) {
				arrayTokens[i] = token;
				i++;
			}
		}

		for (int n = arrayTokens.length - 1; n > -1; n--) {
			if (arrayTokens[n] != null)
				pila.push(arrayTokens[n]);
		}

		tokenActual = pila.peek();

		String resultado = "";

		resultado = resultado + json();

		bw.write(resultado);
		bw.newLine();

		System.out.println(resultado);

		bw.close();
	}
	
	

	public static void getToken() {
		try {
			pila.pop();
			tokenActual = pila.peek();
		} catch (Exception e) {
			tokenActual = new TokenClass(Globales.EOF,Globales.EOF);
		}		
	}

	public static String json() throws Exception {
		return element();

	}

	public static String element() throws Exception {
		switch (tokenActual.tipo) {
		case Globales.L_LLAVE:
			return object();
		case Globales.L_CORCHETE:
			return array();
		default:
			return "";
		}
	}

	public static String object() throws Exception {
		String funcion = "object()";
		String valor = "<item>";
		String retorno = "";
		switch (tokenActual.tipo) {
		case Globales.L_LLAVE:
			getToken();
			retorno = valor + o();
			return retorno;
		default:
			error(tokenActual.tipo, funcion, Globales.L_LLAVE);
			return "";
		}
	}

	public static String o() throws Exception {
		String funcion = "o()";
		String retorno = "";
		String valor = "</item>";
		switch (tokenActual.tipo) {
		case Globales.LITERAL_CADENA:
			retorno = attributeList() + valor;
			getToken();
			return retorno;
		case Globales.R_LLAVE:
			getToken();
			return valor;
		default:
			error(tokenActual.tipo, funcion, Globales.LITERAL_CADENA + ", " + Globales.R_LLAVE);
			getToken();
			return "";
		}
	}

	public static String attributeList() throws Exception {
		return attribute() + at();
	}

	public static String attribute() throws Exception {
		String retorno = "";
		String valor1 = "";
		String valor2 = "";
		valor1 = "<" + attributeName() + ">";
		valor2 = "</" + attributeName() + ">";
		getToken();
		if (Globales.DOS_PUNTOS.equals(tokenActual.tipo)) {
			getToken();
		}
		retorno = valor1 + attributeValue() + valor2;
		getToken();
		return retorno;
	}

	public static String at() throws Exception {
		switch (tokenActual.tipo) {
		case Globales.COMA:
			getToken();
			return attribute() + at();
		default:
			return "";
		}
	}

	public static String attributeName() throws Exception {

		switch (tokenActual.tipo) {
		case Globales.LITERAL_CADENA:
			return tokenActual.valor;
		default:
			return "";
		}
	}

	public static String attributeValue() throws Exception {
		String funcion = "attributeValue()";
		switch (tokenActual.tipo) {
		case Globales.L_LLAVE:
			return element();
		case Globales.L_CORCHETE:
			return element();
		case Globales.LITERAL_CADENA:
			return tokenActual.getValor();
		case Globales.LITERAL_NUM:
			return tokenActual.getValor();
		case Globales.PR_TRUE:
			return tokenActual.getValor();
		case Globales.PR_FALSE:
			return tokenActual.getValor();
		case Globales.PR_NULL:
			return tokenActual.getValor();
		default:
			error(tokenActual.tipo, funcion,
					Globales.L_LLAVE + ", " + Globales.L_CORCHETE + ", " + Globales.LITERAL_CADENA + ", "
							+ Globales.LITERAL_NUM + ", " + Globales.PR_TRUE + ", " + Globales.PR_FALSE + ", "
							+ Globales.PR_NULL);
			getToken();
			return "";
		}
	}

	public static String array() throws Exception {

		String funcion = "array()";
		String retorno = "";
		switch (tokenActual.tipo) {
		case Globales.L_CORCHETE:
			getToken();
			retorno = a();
			return retorno;
		default:
			error(tokenActual.tipo, funcion, Globales.L_CORCHETE);
			getToken();
			return "";
		}
	}

	public static String a() throws Exception {
		String funcion = "a()";
		String retorno = "";
		switch (tokenActual.tipo) {
		case Globales.L_LLAVE:
			retorno = elementList();
			getToken();
			return retorno;
		case Globales.L_CORCHETE:
			retorno = elementList();
			getToken();
			return retorno;
		case Globales.R_CORCHETE:
			return "";
		default:
			error(tokenActual.tipo, funcion,
					Globales.L_LLAVE + ", " + Globales.L_CORCHETE + ", " + Globales.R_CORCHETE);
			getToken();
			return "";
		}
	}

	public static String elementList() throws Exception {
		return element() + e();
	}

	public static String e() throws Exception {
		switch (tokenActual.tipo) {
		case Globales.COMA:
			getToken();
			return element() + e();
		default:
			getToken();
			return "";
		}
	}

	public static void error(String token, String funcion, String esperado) throws Exception {
		error = true;
		System.out.println("Se encontro " + token + ". Funcion: " + funcion + ". Se esperaba " + esperado + "\n");
	}

}
