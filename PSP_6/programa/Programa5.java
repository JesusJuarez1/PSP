/*************************************************************/
/* Program Assignment:	Programa 5                           */
/* Name:                Jesús Manuel Juárez Pasillas         */
/* Date:                20/05/22                             */
/* Description:         Obtiene el CBO de las clases de un programa    */
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
			Salida.consola("1.- Calcular CBO.\n2.- Salir\n");
			opcion = Entrada.ingresarEntero();	// solicita alguna opcion
			if(opcion == 1){	// Determina si es una opcion valida
				Salida.consola("Ingresa el nombre del archivo:\n");
				String nombreArchivo = Entrada.ingresarTexto();		// Solicita el nombre del archivo a evaluar
				ArrayList<String> lineas = ArchivoTexto.leer(nombreArchivo);	// Ingresa lo que regreso el metodo leer a un ArrayList
				if(lineas != null){ //verifica que si encontro algo
					CBO.CBOArchivo(lineas);	// Llama al metodo que evalua el archivo
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
	/* Clase que contiene el metod leer archivos   	     */
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

class HerramientasClase{

	/*******************************************/
	/* Extrae el nombre de la clase		   */
	/*******************************************/
	public static String obtenerNombreClase(String linea){
		linea = linea.replaceAll("class",""); // Se quita la palabra reservada class
		linea = linea.replaceAll(" ",""); // Se quitan los espacios
		linea = linea.replaceAll("\\t",""); // Se quitan las tabulaciones
		linea = linea.replace("{",""); // Se quitan las llaves que abren
		return linea; //Se regresa el resultado
	}

	/****************************************/
	/* Verifica si la linea es de un metodo */
	/****************************************/
	public static boolean esMetodo(String linea){
		if(((linea.contains("public") && !linea.contains("\"public")) || (linea.contains("private") && !linea.contains("\"private")) || (linea.contains("protected") && !linea.contains("\"protected"))) && !linea.contains(";")){ // es metodo?
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
	public static boolean esClase(String linea){
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
	public static boolean esComentario(String linea){
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

	/********************************************/
	/* Obtiene los atributos de una clase */
	/********************************************/
	public static ArrayList<String> obtenerAtributos(ArrayList<String> lineas, int posicion){
		ArrayList<String> atrib = new ArrayList<String>();
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
				linea = linea.substring(linea.indexOf(" ")+1,linea.indexOf(";")); // obtiene el nombre del atributo
				atrib.add(linea); //Se agrega al arreglo
			}
			posicion++;// se aumenta la posicion
			if(posicion>=lineas.size()){
				break;
			}
			linea = lineas.get(posicion); //Se obtiene la linea de la posicion indicada
		}
		return atrib;
	}

	/********************************************/
	/* Obtiene los metodos de una clase */
	/********************************************/
	public static ArrayList<String> obtenerMetodos(ArrayList<String> lineas, int posicion){
		ArrayList<String> metod = new ArrayList<String>();
		String linea = lineas.get(posicion); //Se obtiene la linea de la posicion indicada
		while(posicion<lineas.size() && !esClase(linea)){ //Se recorren las lineas, hasta que encuentre un metodo o una clase o supere el tamaño
			if(!linea.isEmpty() && esMetodo(linea)){ //Si la linea no esta vacia y es metodo
				linea = linea.replaceAll("\t",""); //se quitan las tabulaciones
				if(linea.contains("public") || linea.contains("private") || linea.contains("protected")){ //Si el atributo contiene alguna de estas palabras
					linea = linea.replaceFirst("public",""); //las quita
					linea = linea.replaceFirst("private","");
					linea = linea.replaceFirst("protected","");
					linea = linea.replaceFirst(" ",""); //Quita un espacio
					
				}
				if(linea.contains("static")){
					linea = linea.replaceFirst("static","");
					linea = linea.replaceFirst(" ","");
				}
				if(linea.indexOf(" ") > linea.indexOf("(")){ //Caso si es un contructor
					linea = "new "+linea.substring(0,linea.indexOf("("));
				}else{
					linea = linea.substring(linea.indexOf(" ")+1,linea.indexOf("(")+1); // obtiene el nombre del atributo
				}
				metod.add(linea); //Se agrega al arreglo
			}
			posicion++;// se aumenta la posicion
			if(posicion>=lineas.size()){
				break;
			}
			linea = lineas.get(posicion); //Se obtiene la linea de la posicion indicada
		}
		return metod;
	}
}

/**********************************/
/* Clase que almacena el nombre, los atributos y los metodos de una clase */
/**********************************/
class Clase{
	protected String nombreClase; //Nombre de la clase
	protected ArrayList<String> atributos; // Atributos de la clase
	protected ArrayList<String> metodos; // Metodos de la clase

	/******************/
	/* constructor */
	/******************/
	public Clase(String nombreClase,ArrayList<String> atributos, ArrayList<String> metodos){
		this.nombreClase = nombreClase; // Se asigna el nombre
		this.atributos = atributos; // Se asingan los atributos
		this.metodos = metodos; // Se asignan los metodos
	}

	/***********************/
	/* Obtiene el nombre de la clase */
	/***********************/
	public String getNombreClase(){
		return nombreClase;
	}

	/******************************/
	/* Obtiene los atributos de la clase */
	/*******************************/
	public ArrayList<String> getAtributos(){
		return atributos;
	}

	/******************************/
	/* Obtiene los metodos de la clase */
	/*******************************/
	public ArrayList<String> getMetodos(){
		return metodos;
	}
}

/***************************/
/* Clase que calcula el cbo de las clases de un programa */
/***************************/
class CBO{

	/**************************/
	/* Obtiene el CBO de un archivo */
	/**************************/
	public static void CBOArchivo(ArrayList<String> lineas){
		ArrayList<Clase> elementosClases = obtenerElementosClases(lineas); //Se obtienen los elementos de las clases
		ArrayList<boolean[]> acopladas = new ArrayList<boolean[]>(); //Se hace un arreglo que almacenara las clases que estan acopladas
		for(int posicion=0;posicion<elementosClases.size();posicion++){ //Se recorren los elementos de las clases
			acopladas.add(CBOClase(lineas,posicion,elementosClases));// Se añade el arreglo que regresa el metodo
		}
		for(int posicion=0;posicion<acopladas.size();posicion++){//Se recorre el arreglo de acopladas
			Salida.consola(elementosClases.get(posicion).getNombreClase()+":\n");
			for(int pos=0;pos<acopladas.get(posicion).length;pos++){ //Se recorre el arreglo de la clase
				if(acopladas.get(posicion)[pos] == true){ // Si esta acoplada con la clase
					Salida.consola("\t"+elementosClases.get(pos).getNombreClase()+"\n"); //Se imprime la clase
				}
			}
		}
	}

	/********************************/
	/* Obtiene el CBO de una clase */
	/********************************/
	private static boolean[] CBOClase(ArrayList<String> lineas, int posicion,ArrayList<Clase> elementosClases){
		boolean[] acoplado = new boolean[elementosClases.size()];
		for(int pos=0;pos<acoplado.length;pos++){ //Se rellena de false
			acoplado[pos] = false;
		}
		for(int pos=0;pos<lineas.size();pos++){ //Se recorre el archivo
			if(HerramientasClase.esClase(lineas.get(pos))){ // Hasta encontrar una clase
				String nomClase = HerramientasClase.obtenerNombreClase(lineas.get(pos)); //Se obtiene el nombre de la clase
				if(nomClase.equalsIgnoreCase(elementosClases.get(posicion).getNombreClase())){ // Se verifica si es que coincide con la clase a evaluar
					pos++;
					while(pos<lineas.size() && !HerramientasClase.esClase(lineas.get(pos))){ //Se recorre la clase
						if(!HerramientasClase.esMetodo(lineas.get(pos)) && !HerramientasClase.esClase(lineas.get(pos)) && !HerramientasClase.esComentario(lineas.get(pos))){
							for(int clase=0;clase<elementosClases.size();clase++){ // Se recorren los elementos de las clases
								Clase cla = elementosClases.get(clase); //Se obtiene la clase
								if(clase != posicion){ //Si la clase que se quiere evaluar es la misma con la que se sta evaluando se omite
									for(int atrib=0;atrib<cla.getAtributos().size();atrib++){// Se verifica si los atributos son usados en la linea
										if(lineas.get(pos).contains(cla.getAtributos().get(atrib))){//Si son usados
											acoplado[clase] = true; //Se cambia a true
										}
									}
									for(int metod=0;metod<cla.getMetodos().size();metod++){//Se verifica si los metodos son utilizados en la linea
										if(lineas.get(pos).contains(cla.getMetodos().get(metod))){//Si son usados
											acoplado[clase] = true; //Se cambia a true
										}
									}
								}
							}
						}
						pos++;
					}
				}
			}
		}
		return acoplado; //Se regresa el arreglo con true en las clases que estan acopladas
	}

	/******************************/
	/* Obtiene los elementos de una clase */
	/******************************/
	private static ArrayList<Clase> obtenerElementosClases(ArrayList<String> lineas){
		ArrayList<Clase> elementosClases = new ArrayList<Clase>();
		for(int posicion=0;posicion<lineas.size();posicion++){//Se recorren las lineas hasta encontrar una clase
			if(HerramientasClase.esClase(lineas.get(posicion))){
				posicion++;
				String nombreClase = HerramientasClase.obtenerNombreClase(lineas.get(posicion-1)); //Guarda el nombre de la clase
				ArrayList<String> atributos = HerramientasClase.obtenerAtributos(lineas, posicion); //Se obtienen los atributos
				ArrayList<String> metodos = HerramientasClase.obtenerMetodos(lineas,posicion); //Se obtiene los metodos
				elementosClases.add(new Clase(nombreClase,atributos,metodos));
			}
		}
		return elementosClases;
	}
}