package compiladores.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class principal {

    //private static List<TokenClass> listTokensClass = new ArrayList();
	
	public static void main(String[] args) throws IOException, Exception {
		/*Archivos Fuentes*/
        String archivoFuente = "../compiladores_tareas/Compiladores_Tareas/src/compiladores/files/fuente.txt";
        String archivoTokens = "../compiladores_tareas/Compiladores_Tareas/src/compiladores/files/tokens.txt";
        String archivoSalida = "../compiladores_tareas/Compiladores_Tareas/src/compiladores/files/output.txt";
                
        String resultadoLexico = "";
        
        try{
        	System.out.println("************** INICIANDO ANALISIS LEXICO.....");
        	System.out.println("\n");
        	resultadoLexico = AnalizadorLexico.procesar(archivoFuente, archivoTokens, archivoSalida);
        	
        	System.out.println("************** INICIANDO ANALISIS SINTACTICO.....");
        	System.out.println("\n");
        	AnalizadorSintactico.procesar(resultadoLexico);
            
        }catch(IOException ex){
            throw new IOException("Ha ocurrido un error: " + ex.getMessage());
        }       
    }

}
