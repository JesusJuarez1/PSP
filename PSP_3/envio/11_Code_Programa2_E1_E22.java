/*************************************************************/
/* Program Assignment:	Programa 2                           */
/* Name:                Jesús Manuel Juárez Pasillas         */
/* Date:                14/03/22                             */
/* Description:         Cuenta las lineas de un archivo, sus clases */
/* 			el numero de lineas y metodos por clase	*/
/*************************************************************/

import java.util.ArrayList;
import java.io.*;

/************************************************/
/* Metodo para iniciar el programa		*/
/************************************************/
class Main{
	
	public static void main(String args[]){
		iniciar(); //Llama al metodo que inicia el programa
	}
	
	/*******************************************/
	/* Inicia el programa pidiendo una opcion  */
	/*******************************************/
	public static void iniciar(){
		Integer opcion = 0;	// Variable que contendra la opcion solicitada por el usuario
		while(opcion != 2){	// ciclo con el cual no se acaba el programa hasta que el usuario asi lo quiera
			Salida.consola("\nEscoge el numero de una opcion:\n"); 
			Salida.consola("1.- Contar lineas.\n2.- Salir\n");
			opcion = Entrada.ingresarEntero();	// solicita alguna opcion
			if(opcion == 1){	// Determina si es una opcion valida
				Salida.consola("Ingresa el nombre del archivo:\n");
				String nombreArchivo = Entrada.ingresarTexto();		// Solicita el nombre del archivo a evaluar
				ArrayList<String> lineas = ArchivoTexto.leer(nombreArchivo);	// Ingresa lo que regreso el metodo leer a un ArrayList
				if(lineas != null){ //verifica que si encontro algo
					Lineas.contarLineas(lineas);	// Llama al metodo que evalua el archivo
				}
			}
		}
	}
}

/****************************************************/
/* Clase que determina las lineas de un archivo,    */
/* numero de clases y su numero de metodos y lineas */
/****************************************************/
class Lineas{
	
	/*************************************************/
	/* Cuenta las lineas para cada caso		*/
	/************************************************/
	public static void contarLineas(ArrayList<String> lineas){
		int lineasTotales = 0;	//Vaariable que contendra el numero total de lineas del archivo
		Salida.consola("Clase \t metodos \t tamaño \n");
		for(int numeroLinea=0;numeroLinea<lineas.size();numeroLinea++){	//recorre todas las lineas
			String linea = lineas.get(numeroLinea);	//mete cada linea a esta variable
			if(esClase(linea)){	// Determina si la linea es una clase
				String nombreClase = obtenerNombreClase(linea); //Si lo es, obtiene el nombre de la clase
				numeroLinea++;
				int lineasClase = 1;	// Variable que contendra el numero total de lineas de la clase
				int numeroMetodos = 0;	// Variable que contendra el numero total de metodos
				while(!esClase(lineas.get(numeroLinea)) && numeroLinea<lineas.size()){	//Recorre cada linea de la clase
					if(esMetodo(lineas.get(numeroLinea))){	//Evalua si la linea es un metodo
						numeroMetodos++;	//Si lo es aumenta el contador del los metodos
						lineasClase++;		//y el de las lineas de la clase
					}else if(esLineaLogica(lineas.get(numeroLinea))){	//Si no, determina si es una linea logica
						lineasClase++;		//Si lo es, aumenta el contador de lineas de la clase
					}	//Si no lo es no hace nada
					numeroLinea++;	//Aumenta el iterador para obtener la siguiente linea del archivo
				}
				lineasTotales += lineasClase;	//Agrega las lineas de la clase a las totales
				Salida.consola(nombreClase+" \t "+numeroMetodos+" \t "+lineasClase+"\n");	//Imprime el resultado
				numeroLinea--;
			}else if(esLineaLogica(linea)){	//Si no es linea de clase, determina si es linea logica
				lineasTotales++;	// Si lo es, aumenta el contador de lineas totales
			}
		}
		Salida.consola("Tamaño total: "+lineasTotales);	//Imprime el numero de lineas totales
	}

	/********************************************/
	/* Evalua si la linea es una clase	    */
	/********************************************/
	private static boolean esClase(String linea){
		if(linea.contains("class") && (!linea.contains("(") || !linea.contains(")"))){ // Si la linea contiene la palabra reservada class es una clase
			if(!esComentario(linea)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/*******************************************/
	/* Extrae el nombre de la clase		   */
	/*******************************************/
	private static String obtenerNombreClase(String linea){
		linea = linea.replaceAll("class",""); // Se quita la palabra reservada class
		linea = linea.replaceAll(" ",""); // Se quitan los espacios
		linea = linea.replaceAll("\\t",""); // Se quitan las tabulaciones
		linea = linea.replace("{",""); // Se quitan las llaves que abren
		return linea; //Se regresa el resultado
	}

	/****************************************/
	/* Verifica si la linea es de un metodo */
	/****************************************/
	private static boolean esMetodo(String linea){
		if((linea.contains("public") || linea.contains("private") || linea.contains("protected")) && !linea.contains(";")){ // Si tiene public o private y no tiene un ; es un metodo
			if(!esComentario(linea)){
				return true;
			}else{
				return false;
			}
		}else{
			return false; // No es metodo
		}
	}

	/*****************************************/
	/* Verifica si una linea es logica	 */
	/*****************************************/
	private static boolean esLineaLogica(String linea){
		linea = linea.replaceAll(" ",""); // Quitamos los espacios
		linea = linea.replaceAll("\\t",""); // Quitamos las tabulaciones
		if(linea.isEmpty() && !linea.contains(";")){ // Si la linea contiene un comentario o es una linea vacia
			return false; // No es linea logica
		}else{
			if(!esComentario(linea)){
				return true;
			}else{
				return false;
			}
		}
		//hola a ver si sirve;
		//private static boolean esLineaLogica(String linea){
		//class ArchivoTexto{
	}

	/***************************************/
	/* Verifica si la linea es un comentario o no */
	/***************************************/
	private static boolean esComentario(String linea){
		linea = linea.replaceAll(" ","");
		linea = linea.replaceAll("\\t","");
		if(!linea.contains("}")){
			if(linea.substring(0,2).equals("//") || linea.substring(0,2).equals("/*")){ //Si comienza con // o contiene /* es comentario
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}

/**********************************/
/* Clase para manipular archivos  */
/**********************************/
class ArchivoTexto{
	
	/*************************************************************/
	/* Clase que contiene el metodo leer archivos   	     */
	/*************************************************************/
	public static ArrayList<String> leer(String archivo){
        	FileReader input=null;
        	int registro=0;
        	ArrayList<String> datos = null;
        	BufferedReader buffer = null;
        	try {
        		String cadena=null;
        		input = new FileReader(archivo); // Se abre el archivo
        		buffer = new BufferedReader(input);
        		datos = new ArrayList<String>(); // Se hace un arreglo donde se estaran colocando cada linea del archivo
        		while((cadena = buffer.readLine())!=null) { // Se recorre todo el archivo linea por linea
                		datos.add(cadena); // Mete la linea al arreglo
        		}
        	}catch (FileNotFoundException e) {
        	   	e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	finally {
            		try{
                		input.close(); // Cierra el archivo
                		buffer.close(); 
            		}catch(IOException e){
                		e.printStackTrace();
            		}
        	}
        	return datos; // Regresa los datos
    	}
}

/************************************************/
/* Clase que contiene los metodos de entrada por texto y enteros */
/************************************************/
class Entrada{

	/********************************************/
	/* Metodo para ingresar una cadena de texto */
	/********************************************/
	public static String ingresarTexto(){
        	InputStreamReader isr = new InputStreamReader(System.in);
        	BufferedReader buffer = new BufferedReader(isr);
        	String cadenaEntrada="";
        	try {
        	    cadenaEntrada = buffer.readLine(); // lee la linea
        	}catch(IOException e){
        	    e.printStackTrace();
        	}finally{
        	    return cadenaEntrada; // regresa la cadena ingresada
        	}
    	}

	/********************************************/
	/* Metodo para ingresar un numero entero    */
	/********************************************/
	public static int ingresarEntero(){
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(isr);
        	int enteroEntrada = 0;
        	try{
        	    enteroEntrada = Integer.parseInt(buffer.readLine()); // Lee la linea y lo convierte a entero
        	}catch(IOException e){
        	    e.printStackTrace();
        	}finally{
        	    return enteroEntrada; // Regresa el resultado
        	}
	}
}

/************************************************************/
/* Clase que contiene el metodo consola el cual permite impirmir en consola el texto mandado */
/************************************************************/
class Salida{
	public static void consola(String cadena){
		System.out.print(cadena); //Imprime en consola lo que se le pasa como parametro
	}
}