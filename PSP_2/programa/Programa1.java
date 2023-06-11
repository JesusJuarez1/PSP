/*************************************************************/
/* Program Assignment:	Programa 1                           */
/* Name:                Jesús Manuel Juárez Pasillas         */
/* Date:                23/02/22                             */
/* Description:         Verifica que existe un encabezado en el archivo que se ingresa, si faltan datos los pide, los agrega y genera un nuevo archivo	*/
/*************************************************************/

import java.util.ArrayList;
import java.io.*;

/************************************************************************/
/* Clase principal la cual estara verificando si existe el encabezado,  */
/* ademas de pedir los datos si es que faltan,				*/
/* tambien crea un nuevo archivo con los datos agregados		*/
/************************************************************************/
class Encabezado{
	
	/**********************************/
	/* Metodo que inicia el programa  */
	/**********************************/
	public static void iniciar(){
		Integer opcion = 0; // Guardar la opcion elegida por el usuario 
		while(opcion != 2){
			Salida.consola("\nEscoge el numero de una opcion:\n");
			Salida.consola("1.- Verificar encabezado.\n2.- Salir\n");
			opcion = Entrada.ingresarEntero(); // Llama al metodo el cual regresa un entero
			if(opcion == 1){
				Salida.consola("Ingresa el nombre del archivo a verificar:\n");
				String nombreArchivo = Entrada.ingresarTexto(); // Variable que contendra el nombre del archivo
				ArrayList<String> lineas = ArchivoTexto.leer(nombreArchivo); // Llama al metodo para abrir el archivo y que este regrese las lineas en un ArrrayList
				if(lineas != null){
					lineas = verificarEncabezado(lineas);
					if(lineas != null){ // Si la variable linea es diferente de null, se modifico algo
						String nuevoNombre = nombreArchivo.substring(0,nombreArchivo.length()-5)+"_rev";// Variable que almacena el nombre del archivo que se va a crear
						nuevoNombre += nombreArchivo.substring(nombreArchivo.length()-5,nombreArchivo.length());
						ArchivoTexto.escribir(lineas,nuevoNombre); // Llama al metodo que crea el nuevo archivo
					}else{ // Si es null, el encabezado esta completo
						Salida.consola("Encabezado completo\n");
					}
				}
			}
		}
	}

	/********************************/
	/* Metod auxiliar para verificar el encabezado */
	/********************************/
	public static ArrayList<String> verificarEncabezado(ArrayList<String> lineas){
		return verificarEncabezado(lineas,false);
	}
	
	/*****************************/
	/* Metodo el cual verifica si contiene el encabezado, que partes le faltan y pide los datos faltantes */
	/*****************************/
	private static ArrayList<String> verificarEncabezado(ArrayList<String> lineas,boolean modificado){
		String[] encabezado = new String[]{"Program Name:","Name:","Date:","Description:"};// Varaible que contiene las opciones que tiene el encabezado
		
		int posicion = 0; // Variable que almacena la posicion en a que se encuentra
		if(lineas.get(posicion).contains("/***")){ // Verifica si en la posicion actual existe "/***"
			posicion++; // Aumenta la posicion
			if(lineas.get(posicion).contains(encabezado[0])){ // Verifica si tiene el primer elemento del encabezado
				String linea;// Variable que contiene la linea de la posicion actual
				linea = quitarSimbolos(lineas.get(posicion),16); 
				if(linea.isEmpty()){ // la linea esta vacia?
					Salida.consola("Falta el nombre del programa!!! \nIngresa el nombre del programa: ");
					String nombreP = Entrada.ingresarTexto();// Variable que contiene el nombre del programa
					lineas.set(posicion,"/* Program Name:\t"+nombreP+"\t*/"); // Se agrega lo que falta a la linea
					modificado = true; // Se cambia a verdadero
				}
				posicion++; // Aumenta la posicion
				if(lineas.get(posicion).contains(encabezado[1])){ // Verifica si tiene el segundo elemento del encabezado
					linea = quitarSimbolos(lineas.get(posicion),8); // Se quitan los simbolos de la linea
					if(linea.isEmpty()){ // La linea esta vacia?
						Salida.consola("Falta tu nombre!!! \nIngresa tu nombre: ");
						String nombre = Entrada.ingresarTexto();
						lineas.set(posicion,"/* Name:\t"+nombre+"\t*/"); // Se agrega lo que falta a la linea
						modificado = true; // Se cambia a verdadero
					}
					posicion++;  // Aumenta la posicion
					if(lineas.get(posicion).contains(encabezado[2])){
						linea = quitarSimbolos(lineas.get(posicion),8);
						if(linea.isEmpty()){
							Salida.consola("Falta la fecha!!! \nIngresa la fecha: ");
							String fecha = Entrada.ingresarTexto();
							lineas.set(posicion,"/* Date:\t"+fecha+"\t*/");
							modificado = true;// Se cambia a verdadero
						}
						posicion++; // Aumenta la posicion
						if(lineas.get(posicion).contains(encabezado[3])){
							linea = quitarSimbolos(lineas.get(posicion),15);
							if(linea.isEmpty()){
								Salida.consola("Falta la descripción!!! \nIngresa una descripcion: ");
								String descripcion = Entrada.ingresarTexto();
								lineas.set(posicion,"/* Description:\t"+descripcion+"\t*/");
								modificado = true;// Se cambia a verdadero
							}
						}else{
							Salida.consola("Falta la descripcion!!! \nIngresa una descripcion: ");
							String descripcion = Entrada.ingresarTexto();
							lineas = agregarContenidoFaltante(lineas,"/* Description:\t"+descripcion+"\t*/",posicion);
							modificado = true;// Se cambia a verdadero
						}
						posicion++; // Aumenta la posicion
						if(!lineas.get(posicion).contains("/*") && !lineas.get(posicion).contains("*/")){
							lineas = agregarContenidoFaltante(lineas,"/* \t\t\t\t */",posicion);
						}
						posicion++; // Aumenta la posicion
						if(!lineas.get(posicion).contains("/***") && !lineas.get(posicion).contains("***/")){
							lineas = agregarContenidoFaltante(lineas,"/*************************************************************************************/",posicion);
						}
					}else{
						Salida.consola("Falta la fecha!!! \nIngresa la fecha:");
						String fecha = Entrada.ingresarTexto();
						lineas = agregarContenidoFaltante(lineas,"/* Date:\t"+fecha+"\t*/",posicion);
						lineas = verificarEncabezado(lineas,true);
						modificado = true;// Se cambia a verdadero
					}
				}else{
					Salida.consola("Falta tu nombre!!! \nIngresa tu nombre:");
					String nombre = Entrada.ingresarTexto();
					lineas = agregarContenidoFaltante(lineas,"/* Name:\t"+nombre+"\t*/",posicion);
					lineas = verificarEncabezado(lineas,true);
					modificado = true;// Se cambia a verdadero
				}
			}else{
				Salida.consola("Falta el nombre del programa!!! \nIngresa el nombre del programa:");
				String nombreP = Entrada.ingresarTexto();
				lineas = agregarContenidoFaltante(lineas,"/* Program Name:\t"+nombreP+"\t*/",posicion);
				lineas = verificarEncabezado(lineas,true);
				modificado = true;// Se cambia a verdadero
			}
		}else{
			lineas = agregarContenidoFaltante(lineas,"/*************************************************************************************/",posicion);
			lineas = verificarEncabezado(lineas,true);
			modificado = true;// Se cambia a verdadero
		}
		if(modificado == true){ // esta modificado?
			return lineas; // Si, se regresan las lineas
		}else{
			return null; // No, se regresa null
		}
	}

	/***********************************************/
	/* Metodo que quita los simbolos de una cadena */
	/***********************************************/
	public static String quitarSimbolos(String linea,int posFin){
		linea = linea.substring(posFin,linea.length()-2); // Quita una parte de la linea
		linea = linea.replaceAll("\\t",""); // Quita las tabulaciones
		linea = linea.replaceAll(" ",""); // Quita los espacios
		return linea; // regresa la linea
	}

	/*****************************/
	/* Metodo que agrega el contenido faltante al arreglo en la posicion indicada */
	/******************************/
	public static ArrayList<String> agregarContenidoFaltante(ArrayList<String> lineas, String linea,int posicion){
		ArrayList<String> nuevoArreglo = new ArrayList<String>(); Se crea un nuevo arreglo
		for(int i=0;i<lineas.size();i++){ // Se recorre el arreglo que contiene los datos
			if(i==posicion){ Si esta en la posicion donde se van agregar los datos nuevos
				nuevoArreglo.add(linea);//los agrega
			}
			nuevoArreglo.add(lineas.get(i)); // mete la linea del arreglo anterior al nuevo 
		}
		return nuevoArreglo; // Regresa el arreglo nuevo
	}
	
	/**************************************/
	/* Inicia el programa		      */
	/**************************************/
	public static void main(String args[]){
		Encabezado.iniciar();
	}
}

/**********************************/
/* Clase para manipular archivos  */
/**********************************/
class ArchivoTexto{
	
	/*************************************************************/
	/* Metodo que lee un archivo y mete las lineas a un areglo   */
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

	/*************************************************************************/
	/* Metodo que escribe un archivo con las lineas ingresadas en el arreglo */
	/*************************************************************************/
	public static void escribir(ArrayList datos, String nombre){
        	FileWriter output=null;
        	try {
        		output = new FileWriter(nombre); // Se crea el archivo
        		for(int i=0;i<datos.size();i++){ // Se recorre el arreglo
        		        output.write(datos.get(i)+ "\n"); // Se ingresan un dato del arreglo para cada linea del archivo
        		}
        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	finally {
            		try{
                		output.close(); // Cierra el archivo
            		}catch(IOException e){
                		e.printStackTrace();
            		}
        	}
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