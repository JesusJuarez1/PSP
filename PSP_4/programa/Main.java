/*************************************************************/
/* Program Assignment:	Programa 4                           */
/* Name:                Jesús Manuel Juárez Pasillas         */
/* Date:                05/05/22                             */
/* Description:         Calcula el LCOM de las clases de un archivo    */
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
			Salida.consola("1.- Obtener cohesion.\n2.- Salir\n");
			opcion = Entrada.ingresarEntero();	// solicita alguna opcion
			if(opcion == 1){	// Determina si es una opcion valida
				Salida.consola("Ingresa el nombre del archivo:\n");
				String nombreArchivo = Entrada.ingresarTexto();		// Solicita el nombre del archivo a evaluar
				ArrayList<String> lineas = ArchivoTexto.leer(nombreArchivo);	// Ingresa lo que regreso el metodo leer a un ArrayList
				if(lineas != null){ //verifica que si encontro algo
					LCOM.cohesionArchivo(lineas);	// Llama al metodo que evalua el archivo
				}
			}
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

class LCOM{

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
		linea = linea.replace("}","");
		if(!linea.isEmpty()){
			if(linea.substring(0,2).equals("//") || linea.substring(0,2).equals("/*")){ //Si comienza con // o contiene /* es comentario
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/***************************************/
	/* recorre el archivo en busca de clases para obtener la cohesion de ellas */
	/***************************************/
	public static void cohesionArchivo(ArrayList<String> lineas){
		for(int posicion=0;posicion<lineas.size();posicion++){ //Recorre las lineas
			if(esClase(lineas.get(posicion))){
				posicion++;
				cohesionClase(lineas,posicion);
			}
		}
	}

	/********************************************/
	/* Obtiene la cohesion de una clase */
	/********************************************/
	private static void cohesionClase(ArrayList<String> lineas, int posicion){
		String nombreClase = obtenerNombreClase(lineas.get(posicion-1)); //Guarda el nombre de la clase
		Salida.consola(nombreClase+":\n");
		String[] atributos = obtenerAtributos(lineas, posicion);
		int[] metodoAtributo = new int[atributos.length]; // variable que guarda el numero de metodos en el que es utilizado una variable
		int numeroMetodos = 0; // variable que guardara el numero de metodos encontrados
		for(int pos=0;pos<atributos.length;pos++){ //Se rellena con 0 el arreglo
			metodoAtributo[pos] = 0;
		}
		while(posicion<lineas.size() && !esClase(lineas.get(posicion))){ //Recorre la clase hasta que encuentre otra
			String linea = lineas.get(posicion);
			if(esMetodo(linea)){ //es metodo?
				numeroMetodos++;
				boolean[] utilizados = cohesionMetodo(atributos,lineas,posicion); //guarda lo que regresa el metodo
				for(int pos=0;pos<atributos.length;pos++){ //se recorre el arreglo para ver que atributos fueron utilizados en un metodo
					if(utilizados[pos] == true){ // Si el atributo fue utilizado tendra un true
						metodoAtributo[pos] = metodoAtributo[pos]+1; //Se aumenta el uno las veces que ha sido utilizado en un metodo
					}
				}
			}
			posicion++; //Se aumenta la posicion
		}
		double promedioPorcentaje = 0.0;
		for(int pos=0;pos<atributos.length;pos++){ //Se imprimen los atributos
			double porcentajeAtributo = ((Double.parseDouble(metodoAtributo[pos]+""))/(Double.parseDouble(numeroMetodos+"")))*100;
			promedioPorcentaje += porcentajeAtributo;
			Salida.consola("\t"+atributos[pos]+" ("+metodoAtributo[pos]+"/"+numeroMetodos+")*100 = "+porcentajeAtributo+"%\n");
		}
		if(atributos.length == 0){
			Salida.consola("\tLCOM = 100 - 0 = 100\n");
		}else{
			Salida.consola("\tPromedio de porcentaje: "+promedioPorcentaje+"/"+atributos.length+" = "+(promedioPorcentaje/atributos.length)+"\n");
			Salida.consola("\tLCOM = 100 - "+(promedioPorcentaje/atributos.length)+" = "+(100-(promedioPorcentaje/atributos.length))+"\n");
		}
	}

	/*************************************/
	/* Obtiene si los atributos de una clase estan siendo utilizados por un metodo */
	/*************************************/
	private static boolean[] cohesionMetodo(String[] atributos, ArrayList<String> lineas, int posicion){
		posicion++; //Se aumenta la posicion
		boolean[] utilizados = new boolean[atributos.length]; //se hace el arreglo para determinar si los atributos son utilizados o no
		for(int pos=0;pos<atributos.length;pos++){ //Se recorre el arreglo 
			utilizados[pos] = false; // se rellena de false
		}
		while(posicion<lineas.size() && !esMetodo(lineas.get(posicion)) && !esClase(lineas.get(posicion)) && !esComentario(lineas.get(posicion))){ //mientras no sea metodo o clase o comentario
			for(int pos=0;pos<atributos.length;pos++){ //Se recorren los atributos
				if(lineas.get(posicion).contains(atributos[pos])){ //Si uno de ellos esta siendo utilizado 
					utilizados[pos] = true; // Se cambia a true en el arreglo de utilizados en la posicion del atributo evaluado
				}
			}
			posicion++;// Se aumenta la posicion
		}
		return utilizados;
	}

	/********************************************/
	/* Obtiene los atributos de una clase */
	/********************************************/
	private static String[] obtenerAtributos(ArrayList<String> lineas, int posicion){
		ArrayList<String> atributos = new ArrayList<String>();
		String linea = lineas.get(posicion); //Se obtiene la linea de la posicion indicada
		while(posicion<lineas.size() && !esMetodo(linea) && !esClase(linea)){ //Se recorren las lineas, hasta que encuentre un metodo o una clase o supere el tamaño
			if(!linea.isEmpty() && !esComentario(linea) && linea.contains(";")){ //Si la linea no esta vacia, no es un comentario, es un atributo
				linea = linea.replaceAll("\t",""); //se quitan las tabulaciones
				if(linea.contains("public") || linea.contains("private") || linea.contains("protected")){ //Si el atributo contiene alguna de estas palabras
					linea = linea.replaceFirst("public",""); //las quita
					linea = linea.replaceFirst("private","");
					linea = linea.replaceFirst("protected","");
					linea = linea.replaceFirst(" ",""); //Quita un espacio
				}
				linea = linea.substring(linea.indexOf(" ")+1,linea.length()-1); // obtiene el nombre del atributo
				atributos.add(linea); //Se agrega al arreglo
			}
			posicion++;// se aumenta la posicion
			linea = lineas.get(posicion); //Se obtiene la linea de la posicion indicada
		}
		String[] atrib = new String[atributos.size()];// Se hace un arreglo
		for(int pos=0;pos<atributos.size();pos++){ // se recorre el arreglo de atributos
			atrib[pos] = atributos.get(pos)+""; // Se agregan al nuevo arreglo
		}
		return atrib; // regresa los atributos encontrados
	}
}