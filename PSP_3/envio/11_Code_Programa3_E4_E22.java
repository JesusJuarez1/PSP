/*************************************************************/
/* Program Assignment:	Programa 3                           */
/* Name:                Jesús Manuel Juárez Pasillas         */
/* Date:                07/04/22                             */
/* Description:         Obtiene la complejidad ciclomática   */
/* 			de las clases de un programa   	     */
/*************************************************************/

import java.util.ArrayList;
import java.io.*;

/************************************************/
/* Clase para iniciar el programa		*/
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
			Salida.consola("1.- Obtener complejidad ciclomatica.\n2.- Salir\n");
			opcion = Entrada.ingresarEntero();	// solicita alguna opcion
			if(opcion == 1){	// Determina si es una opcion valida
				Salida.consola("Ingresa el nombre del archivo:\n");
				String nombreArchivo = Entrada.ingresarTexto();		// Solicita el nombre del archivo a evaluar
				ArrayList<String> lineas = ArchivoTexto.leer(nombreArchivo);	// Ingresa lo que regreso el metodo leer a un ArrayList
				if(lineas != null){ //verifica que si encontro algo
					Complejidad.obtenerComplejidad(lineas);	// Llama al metodo que evalua el archivo
				}
			}
		}
	}
}

/*********************************/
/* Clase que obtiene la complejidad ciclomatica */
/*********************************/
class Complejidad{
	
	/************************************/
	/* Obtiene la complejidad de */
	/************************************/
	public static void obtenerComplejidad(ArrayList<String> lineas){
		for(int posicion=0;posicion<lineas.size();posicion++){ //Recorre las lineas
			if(esClase(lineas.get(posicion))){
				posicion++;
				complejidadClase(lineas,posicion);
			}
		}
	}

	/****************************/
	/* Obtiene la complejidad de una clase */
	/***************************/
	public static void complejidadClase(ArrayList<String> lineas, int posicion){
		int complejidad = 0; // guarda la complejidad de la clase
		String nombreClase = obtenerNombreClase(lineas.get(posicion-1)); //Guarda el nombre de la clase
		Salida.consola(nombreClase+":\n");
		int numeroMetodo = 1; // Guarda el numero de metodo en el que va
		while(!esClase(lineas.get(posicion)) && posicion < lineas.size()){ // Recorre las lineas hasta que termine la clase
			if(esMetodo(lineas.get(posicion))){ // es metodo?
				int complejidadMetodo = complejidadMetodo(lineas,posicion); // Aumenta la complejidad del metodo evaluado
				complejidad += complejidadMetodo;
				Salida.consola("\t Metodo"+numeroMetodo+": "+complejidadMetodo+"\n");
				numeroMetodo++; // Aumenta el numero de metodo en el que va
			}
			posicion++;//Aumenta la posicion
			if(posicion >= lineas.size()){
				break;
			}
		}
		Salida.consola("Total: "+complejidad);
		if(complejidad > -1 && complejidad <= 10){
			Salida.consola(", El programa es simple, sin mucho riesgo.\n");
		}else if(complejidad > 10 && complejidad <= 20){
			Salida.consola(", Riesgo moderado, se recomienda pensar en metodos de simplificar el codigo.\n");
		}else{
			Salida.consola(", Programa complejo con alto riesgo.\n");
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

	/****************************/
	/* Obtiene la complejidad de un metodo */
	/***************************/
	public static int complejidadMetodo(ArrayList<String> lineas, int posicion){
		int complejidad = 1; // inicia la complejidad del metodo en 1
		posicion++;
		while(!esMetodo(lineas.get(posicion)) && !esClase(lineas.get(posicion)) && posicion < lineas.size()){ //recorre las lineas hasta que acaba el metodo
			if(contieneExpreciones(lineas.get(posicion))){ //la linea contiene expresiones?
				complejidad += complejidadLinea(lineas.get(posicion)); //Aumenta la complejidad en base a la complejidad de la linea
			}
			posicion++; //Aumenta la posicion
			if(posicion >= lineas.size()){
				break;
			}
		}
		return complejidad;
	}

	/********************************/
	/* Evalua si tiene alguna expresion de complejidad */
	/*********************************/
	private static boolean contieneExpreciones(String linea){
		// Evalua si la linea contiene alguna de las expresiones
		if(linea.contains("while") || linea.contains("for") || linea.contains("foreach") || linea.contains("case") || linea.contains("default") || linea.contains("continue") || linea.contains("goto") || linea.contains("&&") || linea.contains("||") || linea.contains("catch") || linea.contains("?:") || linea.contains("??")){
			return true;
		}else{
			return false;
		}
	}

	/********************************/
	/* obtiene la complejidad de una linea */
	/*******************************/
	private static int complejidadLinea(String linea){
		//Quita los elementos con los que se hace la evaluacion
		int complejidadLinea = 0;
		linea = linea.replace("\"&&","");
		linea = linea.replace("\"||","");
		linea = linea.replace("\"foreach","");
		linea = linea.replace("\"for","");
		while(true){ // recorre la linea infinitamente, hasta que no contenga ninguna expresion
			if(linea.contains("while") && !linea.contains("\"while\"")){
				complejidadLinea++;
				linea = linea.replace("while",""); //quita la expresion con la que aumento la complejidad
			}else if(linea.contains("for") && !linea.contains("\"for\"")){
				complejidadLinea++;
				linea = linea.replace("for","");
			}else if(linea.contains("foreach") && !linea.contains("\"foreach\"")){
				complejidadLinea++;
				linea = linea.replace("foreach","");
			}else if(linea.contains("case") && !linea.contains("\"case\"")){
				complejidadLinea++;
				linea = linea.replace("case","");
			}else if(linea.contains("default") && !linea.contains("\"default\"")){
				complejidadLinea++;
				linea = linea.replace("default","");
			}else if(linea.contains("continue") && !linea.contains("\"continue\"")){
				complejidadLinea++;
				linea = linea.replace("continue","");
			}else if(linea.contains("goto") && !linea.contains("\"goto\"")){
				complejidadLinea++;
				linea = linea.replace("goto","");
			}else if(linea.contains("&&")){
				complejidadLinea++;
				linea = linea.replaceFirst("&&","");
			}else if(linea.contains("||")){
				complejidadLinea++;
				linea = linea.substring(0,linea.indexOf("||"))+linea.substring(linea.indexOf("||")+2,linea.length());
			}else if(linea.contains("catch") && !linea.contains("\"catch\"")){
				complejidadLinea++;
				linea = linea.replace("catch","");
			}else if(linea.contains("?:") && !linea.contains("\"?:\"")){
				complejidadLinea++;
				linea = linea.replace("?:","");
			}else if(linea.contains("??") && !linea.contains("\"??\"")){
				complejidadLinea++;
				linea = linea.replace("??","");
			}else{
				return complejidadLinea;
			}
		}
	}
	
	/****************************************/
	/* Verifica si la linea es de un metodo */
	/****************************************/
	private static boolean esMetodo(String linea){
		if((linea.contains("public") || linea.contains("private") || linea.contains("protected")) && !linea.contains(";")){ // es metodo?
			if(!esComentario(linea)){
				return true;
			}else{
				return false;
			}
		}else{
			return false; // No es metodo
		}
	}

	/********************************************/
	/* Evalua si la linea es una clase	    */
	/********************************************/
	private static boolean esClase(String linea){
		if(linea.contains("class") && (!linea.contains("(") || !linea.contains(")"))){ // contiene la palabra?
			if(!esComentario(linea)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
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