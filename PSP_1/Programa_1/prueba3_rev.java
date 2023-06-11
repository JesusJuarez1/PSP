/*************************************************************************************/
/* Program Name:	the program name 					     */
/* Name:	Jesus	*/
/* Date: 		the date program development started 			     */
/* Description: 	a short description of the program function		     */
/*  										     */
/*************************************************************************************/

import java.util.ArrayList;
import java.io.*;

class Encabezado{
	
	public static void iniciar(){
		Integer opcion = 0;
		while(opcion != 2){
			Salida.consola("Escoge el numero de una opcion:\n");
			Salida.consola("1.- Verificar encabezado.\n2.- Salir\n");
			opcion = Entrada.ingresarEntero();
			if(opcion == 1){
				Salida.consola("Ingresa el nombre del archivo a verificar:\n");
				String nombreArchivo = Entrada.ingresarTexto();
				ArrayList<String> lineas = ArchivoTexto.leer(nombreArchivo);
				if(lineas != null){
					lineas = verificarEncabezado(lineas);
					if(lineas != null){
						String nuevoNombre = "";
						nuevoNombre = nombreArchivo.subString(0,nombreArchivo.length()-5)+"_rev";
						nuevoNombre += nombreArchivo.subString(nombreArchivo.length()-5,nombreArchivo.length());
						ArchivoTexto.escribir(lineas,nuevoNombre);
					}else{
						Salida.consola("Encabezado completo\n");
					}
				}
			}
		}
	}

	private static ArrayList<String> verificarEncabezado(ArrayList<String> lineas){
		boolean modificado = false;
		String[] encabezado = new String[]{"Program Name:","Name:","Date:","Description:"};
		int posicion=0;
		if(lineas.get(posicion).constains("/***")){
			if(lineas.get(posicion).contains(encabezado[0]){
				String linea = quitarSimbolos(lineas.get(posicion),encabezado[0]);
				if(linea.isEmpty()){
					Salida.consola("Ingresa el nombre del programa:\n");
					String nombreP = Entrada.ingresarTexto();
					lineas.set(posicion,"/* Program Name:\t"+nombreP+"\t*/");
					modificado = true;
				}
				posicion++;
				if(lineas.get(posicion).contains(encabezado[1]){
					linea = quitarSimbolos(lineas.get(posicion),encabezado[1]);
					if(linea.isEmpty()){
						Salida.consola("Ingresa tu nombre:\n");
						String nombre = Entrada.ingresarTexto();
						lineas.set(posicion,"/* Name:\t"+nombre+"\t*/");
						modificado = true;
					}
					posicion++;
					if(lineas.get(posicion).contains(encabezado[2]){
						linea = quitarSimbolos(lineas.get(posicion),encabezado[2]);
						if(linea.isEmpty()){
							Salida.consola("Ingresa la fecha:\n");
							String fecha = Entrada.ingresarTexto();
							lineas.set(posicion,"/* Date:\t"+fecha+"\t*/");
							modificado = true;
						}
						posicion++;
						if(lineas.get(posicion).contains(encabezado[3]){
							linea = quitarSimbolos(lineas.get(posicion),encabezado[3]);
							if(linea.isEmpty()){
								Salida.consola("Ingresa una descripcion:\n");
								String descripcion = Entrada.ingresarTexto();
								lineas.set(posicion,"/* Description:\t"+descripcion+"\t*/");
								modificado = true;
							}
						}else{
							Salida.consola("Ingresa una descripcion:\n");
							String descripcion = Entrada.ingresarTexto();
							lineas = agregarContenidoFaltante(lineas,"/* Description:\t"+descripcion+"\t*/",posicion);
							return lineas;
						}
					}else{
						Salida.consola("Ingresa la fecha:\n");
						String fecha = Entrada.ingresarTexto();
						lineas = agregarContenidoFaltante(lineas,"/* Date:\t"+fecha+"\t*/",posicion);
						return verificarEncabezado(lineas);
					}
				}else{
					Salida.consola("Ingresa tu nombre:\n");
					String nombre = Entrada.ingresarTexto();
					lineas = agregarContenidoFaltante(lineas,"/* Name:\t"+nombre+"\t*/",posicion);
					return verificarEncabezado(lineas);
				}
			}else{
				Salida.consola("Ingresa el nombre del programa:\n");
				String nombreP = Entrada.ingresarTexto();
				lineas = agregarContenidoFaltante(lineas,"/* Program Name:\t"+nombrep+"\t*/",posicion);
				return verificarEncabezado(lineas);
			}
		}else{
			lineas = agregarContenidoFaltante(lineas,"/*************************************************************************************/",posicion);
			return verificarEncabezado(lineas);
		}

		if(modificado == true){
			return lineas;
		}else{
			return null;
		}
	}

	private static String quitarSimbolos(String linea,String encabezado){
		linea.replace(encabezado,"");
		linea.replace("/*","");
		linea.replace("*/","");
		linea.replace("\t","");
		linea.replace(" ","");
		return linea;
	}

	private static ArrayList<String> agregarContenidoFaltante(ArrayList<String> lineas, String linea,int posicion){
		ArrayList<String> nuevo = new ArrayList<String>();
		for(int i=0;i<lineas.size();i++){
			if(i==posicion){
				i--;
				nuevo.add(linea);
			}else{
				nuevo.add(lineas.get(i));
			}
		}
		return nuevo;
	}
}


class ArchivoTexto{
	
	public static ArrayList<String> leer(String archivo){
        	FileReader input=null;
        	int registro=0;
        	ArrayList<String> datos = null;
        	BufferedReader buffer = null;
        	try {
        		String cadena=null;
        		input = new FileReader(archivo);
        		buffer = new BufferedReader(input);
        		datos = new ArrayList<String>();
        		while((cadena = buffer.readLine())!=null) {
                		datos.add(cadena);
        		}
        	}catch (FileNotFoundException e) {
        	   	e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	finally {
            		try{
                		input.close();
                		buffer.close();
            		}catch(IOException e){
                		e.printStackTrace();
            		}
        	}
        	return datos;
    	}

	public static void escribir(ArrayList datos, String nombre){
        	FileWriter output=null;
        	try {
        		output = new FileWriter(archivo);
        		for(int i=0;i<datos.size();i++){
        		        output.write(datos.get(i)+ "\n");
        		}
        	} catch (FileNotFoundException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	finally {
            		try{
                		output.close();
            		}catch(IOException e){
                		e.printStackTrace();
            		}
        	}
    	}
}


class Entrada{

	public static String ingresaTexto(){
        	InputStreamReader isr = new InputStreamReader(System.in);
        	BufferedReader buffer = new BufferedReader(isr);
        	String cadenaEntrada="";
        	try {
        	    cadenaEntrada = buffer.readLine();
        	}catch(IOException e){
        	    e.printStackTrace();
        	}finally{
        	    return cadenaEntrada;
        	}
    	}

	public static int ingresarEntero(){
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(isr);
        	int enteroEntrada = 0;
        	try{
        	    enteroEntrada = Integer.parseInt(buffer.readLine());
        	}catch(IOException e){
        	    e.printStackTrace();
        	}finally{
        	    return enteroEntrada;
        	}
	}
}


class Salida{
	public static void consola(String cadena){
		System.out.print(cadena);
	}
}

public class Prueba{
	public static void main(String args[]){
		Encabezado.iniciar();
	}
}
