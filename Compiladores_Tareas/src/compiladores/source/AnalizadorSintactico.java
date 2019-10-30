package compiladores.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class AnalizadorSintactico {
	
    private static List<String> listGramatica = new ArrayList();    
    private static Stack<String> pila = new Stack<String>();    
    static String tokenActual = "";
    static boolean error = false;
    
    
    public static void procesar(String archivoFuente) throws IOException, Exception {
        
        String[] arrayTokens = archivoFuente.split(" ");
        
        for (int n = arrayTokens.length-1; n > -1 ; n--){
        	if(arrayTokens[n].length() > 0){
        		pila.push(arrayTokens[n]);
        	}			
        }
        
        tokenActual = pila.peek();
        
        json();
        
    }  
    
    public static void match(String tokenEsperado) throws Exception{
    	String funcion = "match()";
    	if(tokenActual.equals(tokenEsperado)){
    		if(!tokenActual.equals(Globales.EOF))
    			getToken();
    	}else{
    		error(tokenActual, funcion, tokenEsperado);
    		if(!tokenActual.equals(Globales.EOF))
    			getToken();
    	}    	
    }
    
    public static void getToken(){
    	pila.pop();
    	tokenActual = pila.peek();
    }
    
    public static void json() throws Exception{
    	element();
    	if(!error){
	    	System.out.println("SINTACTICAMENTE CORRECTO!!!!!!!!!");
	    	System.out.println("\n");
    	}else{
    		System.out.println("ANALISIS SINTACTICO FINALIZADO CON ERRORES.");
    		System.out.println("\n");
    	}
    }
    
    public static void element() throws Exception{
    	String funcion = "element()";
		switch (tokenActual) {
		case Globales.L_LLAVE:
			object();
			break;
		case Globales.L_CORCHETE:
			array();
			break;
		default:
			error(tokenActual, funcion, Globales.L_LLAVE+", "+Globales.L_CORCHETE);
			getToken();
		}			
    }
    
    public static void object() throws Exception{
    	String funcion = "object()";
    	switch (tokenActual) {
		case Globales.L_LLAVE:
			match(Globales.L_LLAVE);
	    	o();
			break;
    	default:
    		error(tokenActual, funcion, Globales.L_LLAVE);
    		getToken();
    	}
    }
    
    public static void o() throws Exception{
    	String funcion = "o()";
    	switch (tokenActual) {
		case Globales.LITERAL_CADENA:
			attributeList();
			match(Globales.R_LLAVE);
			break;				
		case Globales.R_LLAVE:
			match(Globales.R_LLAVE);
			break;
    	default:
    		error(tokenActual, funcion, Globales.LITERAL_CADENA +", "+Globales.R_LLAVE);
    		getToken();
    	}
    }
    
    public static void attributeList() throws Exception{
    	attribute();
    	at();
    }
    
    public static void attribute() throws Exception{
    	attributeName(); 
    	match(Globales.DOS_PUNTOS);
    	attributeValue();
    }
    
    public static void at() throws Exception{
    	switch(tokenActual){
    	case Globales.COMA:
    		match(Globales.COMA);
        	attribute();
        	at(); 
    		break;
    	}   	
    }
    
    public static void attributeName() throws Exception{
    	match(Globales.LITERAL_CADENA);
    }
    
    public static void attributeValue() throws Exception{
    	String funcion = "attributeValue()";
    	switch (tokenActual) {
    	case Globales.L_LLAVE:
			element();
			break;
    	case Globales.L_CORCHETE:
    		element();
			break;
		case Globales.LITERAL_CADENA:
			match(Globales.LITERAL_CADENA);
			break;
		case Globales.LITERAL_NUM:
			match(Globales.LITERAL_NUM);
			break;
		case Globales.PR_TRUE:
			match(Globales.PR_TRUE);
			break;
		case Globales.PR_FALSE:
			match(Globales.PR_FALSE);
			break;
		case Globales.PR_NULL:
			match(Globales.PR_NULL);
			break;
		default:
			error(tokenActual, funcion, Globales.L_LLAVE+", "+Globales.L_CORCHETE+", "
					+Globales.LITERAL_CADENA+", "+Globales.LITERAL_NUM+", "+Globales.PR_TRUE+", "
					+Globales.PR_FALSE+", "+Globales.PR_NULL);
			getToken();
		}
    }    
    
    public static void array() throws Exception{
    	String funcion = "array()";
    	switch (tokenActual) {
		case Globales.L_CORCHETE:
			match(Globales.L_CORCHETE);
			a();
			break;
		default:
			error(tokenActual, funcion, Globales.L_CORCHETE);
			getToken();
		}
    }
    
   public static void a() throws Exception{
	   String funcion = "a()";
	   switch (tokenActual) {
		case Globales.L_LLAVE:
			elementList();
			match(Globales.R_CORCHETE);
			break;
		case Globales.L_CORCHETE:
			elementList();
			match(Globales.R_CORCHETE);
			break;
		case Globales.R_CORCHETE:
			match(Globales.R_CORCHETE);
			break;
		default:
			error(tokenActual, funcion, Globales.L_LLAVE+", "+Globales.L_CORCHETE+", "+Globales.R_CORCHETE);
			getToken();
		}
   }
   
   public static void elementList() throws Exception{
   		element();
   		e();
   }
   
   public static void e() throws Exception{
   		switch (tokenActual) {
		case Globales.COMA:
			match(Globales.COMA);
			element();
			e();
			break;
		}
   }
   
   public static void error(String token, String funcion, String esperado) throws Exception{
	   error = true;
	   //System.out.println("Se detecto los siguientes errores sintacticos:  \n");
	   System.out.println("Se encontro "+token + ". Funcion: " + funcion + ". Se esperaba "+ esperado + "\n");
	   //throw new Exception("Ha ocurrido un error sintactico");
   }  
}