package compiladores.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class principal {

    private static List<TokenClass> listTokensClass = new ArrayList();
	
	public static void main(String[] args) throws IOException, Exception {

        String archivoFuente = "C://fuente.txt";
        String archivoTokens = "C://tokens.txt";
        String archivoSalida = "C://output.txt";

        try{
        	System.out.println("************** INICIANDO ANALISIS LEXICO.....");
        	System.out.println("\n");
        	listTokensClass = AnalizadorLexico.procesar(archivoFuente, archivoTokens, archivoSalida);        	
            
        }catch(IOException ex){
            throw new IOException("Ha ocurrido un error: " + ex.getMessage());
        }       
    }

}
