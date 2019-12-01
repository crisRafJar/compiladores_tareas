package compiladores.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class principal {

	
	public static void main(String[] args) throws IOException, Exception {
		Respuesta respuesta = new Respuesta();
		
		/*Archivos Fuentes*/
        String archivoFuente = "C:/compiladores_tareas/Compiladores_Tareas/src/compiladores/files/fuente.txt";
        String archivoTokens = "C:/compiladores_tareas/Compiladores_Tareas/src/compiladores/files/tokens.txt";
        String archivoSalida = "C:/compiladores_tareas/Compiladores_Tareas/src/compiladores/files/output.txt";
        
        String archivoSalidaTraductor = "C:/compiladores_tareas/Compiladores_Tareas/src/compiladores/files/outputTraductor.txt";
                
        try{
        	System.out.println("************** INICIANDO ANALISIS LEXICO.....");
        	System.out.println("\n");
        	respuesta = AnalizadorLexico.procesar(archivoFuente, archivoTokens, archivoSalida);
        	
        	System.out.println("************** INICIANDO ANALISIS SINTACTICO.....");
        	System.out.println("\n");
        	AnalizadorSintactico.procesar(respuesta.getResultadoLexico());
        	
        	System.out.println("************** INICIANDO TRADUCCION JSON A XML.....");
        	Traductor.traducir(respuesta.getListTokenClass(), archivoSalidaTraductor); 
            
        }catch(IOException ex){
            throw new IOException("Ha ocurrido un error: " + ex.getMessage());
        }       
    }

}
