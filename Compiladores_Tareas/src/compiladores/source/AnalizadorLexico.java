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


public class AnalizadorLexico {
	
    private static Map<String, String> tokensPermitidos = new HashMap<String, String>();
    private static List<Map<String, String>> listTokensValor = new ArrayList<Map<String, String>>();
    private static List<String> listTokens = new ArrayList<String>();
    private static List<TokenClass> listTokensClass = new ArrayList<TokenClass>();
    static boolean error = false;
    static String lineasError = "";
    static String mensajeError= "";

    
    
    public static Respuesta procesar(String archivoFuente, String archivoTokens, String archivoSalida) throws Exception {

    	Respuesta respuesta = new Respuesta();
    	
        String resultado;
        String resultado2 = "";
        String valor; 
        boolean esComilla = false;
        boolean esDosPuntos = false;
        boolean esNumero = false;
        boolean esComa = false;
        int comillaIni = 0;
        int comillaFin = 0;
        int linea = 0;
        
        String dato = "";
    	
    	obtenerTokens(archivoTokens);

        File file = new File(archivoSalida);

        // Si el archivo no existe es creado
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        String cadena;
        FileReader f = new FileReader(archivoFuente);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
        		linea = linea + 1;
                esComilla = false;
                esDosPuntos = false;
                esNumero = false;
                esComa = false;
                comillaIni = 0;
                comillaFin = 0;
                resultado = Integer.toString(linea);
                valor = ""; 
                dato = "";
                

                for (int n = 0; n <cadena.length(); n++){
                    char c = cadena.charAt (n);
                    //Se marcan las comillas inicial y final que forman un string
                    //if(c == "") continue;
                    if(c == Globales.tokenComilla){
                            esComilla = true;
                            if(comillaIni == 0){
                                    comillaIni = 1;
                            }else{
                                    comillaFin = 1;
                            }
                    }else if(c == Globales.tokenDosPuntos){
                            esDosPuntos = true;
                    }else if(c == Globales.tokenComa){
                            esComa = true;
                    }else if(Character.isDigit(c) && comillaIni==0){
                            esNumero = true;                	
                    }else if((Character.isLetter(c) || !Character.isLetter(c)) && esDosPuntos && !esComa){
                            valor = valor + c;
                    }

                    if(esDosPuntos && esNumero && esComa){
                            resultado = resultado +" "+analizarToken(Globales.tokenNumber, linea, 1);
                            resultado2 = resultado2 +" "+analizarToken2(Globales.tokenNumber, 1);
                            crearLista(analizarToken(Globales.tokenNumber, linea, 0), dato);
                            esDosPuntos = false;
                            esNumero = false;
                    }else if(esDosPuntos && !esNumero && esComa){
                            if(valor.trim().equalsIgnoreCase(Globales.tokenBooleanTrue)){
                                    resultado = resultado +" "+analizarToken(Globales.tokenBooleanTrue, linea, 1);
                                    resultado2 = resultado2 +" "+analizarToken2(Globales.tokenBooleanTrue, 1);
                                    crearLista(analizarToken(Globales.tokenBooleanTrue, linea, 0), dato);
                                    dato = "";
                            }else if(valor.trim().equalsIgnoreCase(Globales.tokenBooleanFalse)){
                                    resultado = resultado +" "+analizarToken(Globales.tokenBooleanFalse, linea, 1);
                                    resultado2 = resultado2 +" "+analizarToken2(Globales.tokenBooleanFalse, 1);
                                    crearLista(analizarToken(Globales.tokenBooleanFalse, linea, 0), dato);
                                    dato = "";
                            }
                            valor = "";
                            esDosPuntos = false;

                    }

                    if(((!esNumero && !Character.isLetter(c)) | (!esNumero && valor=="") | esComa) && !esComilla && comillaIni == 0 && comillaFin == 0){               	
                            resultado = resultado +" "+ analizarToken(String.valueOf(c), linea, 1);
                            resultado2 = resultado2 +" "+ analizarToken2(String.valueOf(c), 1);
                            
                            crearLista(analizarToken(String.valueOf(c), linea, 0),Character.toString(c));
                    }else if(esComilla && comillaIni == 1 && comillaFin == 1){
                            resultado = resultado +" "+analizarToken(Globales.tokenString, linea, 1);
                            resultado2 = resultado2 +" "+analizarToken2(Globales.tokenString, 1);
                            crearLista(analizarToken(Globales.tokenString, linea, 0), dato);
                            dato = "";
                            esComilla = false;
                            comillaIni = 0;
                            comillaFin = 0;
                    }
                    
                    if(!esDosPuntos && (esComilla && comillaIni == 1) || (esComilla && comillaIni == 1 && comillaFin == 1)){
                    	if (c != Globales.tokenComilla)
                    		dato = dato + c;
                    }
                    
                    if(esDosPuntos && esNumero ){//numero
                    	if (c != Globales.tokenComilla)
                    		dato = dato + c;
                    }
                    if(esDosPuntos && !esNumero){//boolean
                    	if (c != Globales.tokenDosPuntos && c != Globales.tokenComilla)
                    		dato = dato + c;
                    }

                }

                if(esDosPuntos && esNumero && !esComa){
                        resultado = resultado +" "+analizarToken(Globales.tokenNumber, linea, 1);
                        resultado2 = resultado2 +" "+analizarToken2(Globales.tokenNumber, 1);
                        crearLista(analizarToken(Globales.tokenNumber, linea, 0), dato);
                        dato = "";
                        esDosPuntos = false;
                        esNumero = false;
                }else if(esDosPuntos && !esNumero && !esComa){
                        if(valor.equalsIgnoreCase(Globales.tokenBooleanTrue)){
                                resultado = resultado +" "+analizarToken(Globales.tokenBooleanTrue, linea, 1);
                                resultado2 = resultado2 +" "+analizarToken2(Globales.tokenBooleanTrue, 1);
                                crearLista(analizarToken(Globales.tokenBooleanTrue, linea, 0), dato);
                                dato = "";
                        }else if(valor.equalsIgnoreCase(Globales.tokenBooleanFalse)){
                                resultado = resultado +" "+analizarToken(Globales.tokenBooleanFalse, linea, 1);
                                resultado2 = resultado2 +" "+analizarToken2(Globales.tokenBooleanFalse, 1);
                                crearLista(analizarToken(Globales.tokenBooleanFalse, linea, 0), dato);
                                dato = "";
                        }
                        valor = "";
                        esDosPuntos = false;

                }
                //Se analizan las comillas inicial y final que forman un string
                if(esComilla && comillaIni != 1 && comillaFin != 1) {
                	error = true;
                	resultado = resultado + " #errorString";
                    mensajeError = mensajeError + "#errorString" + " Linea " + linea + "\n";
                }
                if(esComilla && comillaIni == 1 && comillaFin != 1) {
                	error = true;
                	resultado = resultado + " #errorString";
                    mensajeError = mensajeError + "#errorString" + " Linea " + linea + "\n";
                }
                if(esComilla && comillaIni != 1 && comillaFin == 1) {
                	error = true;
                	resultado = resultado + " #errorString";
                    mensajeError = mensajeError + "#errorString" + " Linea " + linea + "\n";
                }

                System.out.println (resultado);

                bw.write(resultado);
                bw.newLine();
        }
        bw.write("EOF");
        System.out.println ("EOF");
        System.out.println("\n");
        
        if(error){
        	System.out.println("Se detecto los siguientes errores lexicos:  \n");
        	System.out.println(mensajeError);
        	throw new Exception("Ha ocurrido un error lexico");
        }else{
        	System.out.println("LEXICAMENTE CORRECTO!!!!!!!!!");
        }
        
        System.out.println("\n");
        b.close();
        bw.close();

        resultado2 = resultado2 +" EOF";
        
        respuesta.setResultadoLexico(resultado2);
        respuesta.setListTokenClass(listTokensClass);
        
        return respuesta;
    }
    

    public static void obtenerTokens(String archivoTokens) throws FileNotFoundException, IOException {
        String cadena;
        FileReader f = new FileReader(archivoTokens);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
                String[] token = cadena.split(" ");
                tokensPermitidos.put(token[0] , token[1]);
                listTokensValor.add(tokensPermitidos);
                listTokens.add(token[0]);
        }
        b.close();
    }
    
    public static String buscarToken(String tokenAbuscar) throws FileNotFoundException, IOException {
            String tokenValue = null;
            int i = 0;
            boolean buscar = true;

            if(tokenAbuscar.equals(" ") || tokenAbuscar.equals("\t")){
                    buscar = false;
                    tokenValue = tokenAbuscar;
            }

            if(buscar){
                    for(Map<String, String> token : listTokensValor){
                if(listTokens.get(i).equals(tokenAbuscar)){            	
                    tokenValue = token.get(listTokens.get(i));
                    break;
                }
                i++;
                    }
            }

    return tokenValue;
    }

    public static String analizarToken(String token, int linea, int a) throws FileNotFoundException, IOException {

            String respuesta = buscarToken(String.valueOf(token));

            if(respuesta == null && a==1){
                    respuesta = "#error";
                    error = true;
                    mensajeError = mensajeError + "No se esperaba '" +token + "' linea " + linea + "\n";
                    if(Integer.toString(linea).indexOf(lineasError) != -1)
                    	lineasError = lineasError + " " + linea;
            }

            return respuesta;
    }
    public static String analizarToken2(String token, int a) throws FileNotFoundException, IOException {

        String respuesta = buscarToken(String.valueOf(token));

        return respuesta;
    }	
    
    public static void crearLista(String tipo, String valor) throws FileNotFoundException, IOException {

    	
    	TokenClass token = new TokenClass();
    	
    	token.setTipo(tipo);
    	token.setValor(valor);
    	
    	listTokensClass.add(token);

    }    
}