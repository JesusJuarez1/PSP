/*************************************************************/
/* Program Assignment:	Programa de prueba                          */
/* Name:                Jesús Manuel Juárez Pasillas         */
/* Date:                09/05/22                             */
/* Description:         Achivo para probar programa 4   */
/*************************************************************/
class ListaEncadenada{
	protected Nodo frente;
	protected Nodo fin;
	protected Nodo iterador;

	/**********************************/
	/* contructor			*/
	/********************************/
	public ListaEncadenada(){
		frente = null;
		fin = null;
		iterador = null;
	}

	/**********************************/
	/* verifica si esta vacia la lista			*/
	/********************************/
	public boolean vacia(){
		if(frente==null){
			return true;
		}else{
			return false;
		}
	}

	/************************************/
	/* Agrega un elemento	*/
	/***********************************/
	public int agregar(Object elemento){
		Nodo nuevoNodo=new Nodo(elemento); //1
		if(nuevoNodo!=null){ //si hay espacio
			if(vacia()==true){ //a
				frente=nuevoNodo;//2
				fin=nuevoNodo;
			}else{ //b y c
			fin.setDirMemDer(nuevoNodo);//2
			fin=nuevoNodo;//3
		}
		return 1;
		}else{ //no hay espacio
			return -1;
		}
	}

	/*******************************/
	/* Imprime la lista	*/
	/******************************/
	public void imprimir(){
		Nodo temp=frente;
		while(temp!=null){
			SalidaTerminal.consola(temp.getDato()+ " -> ");
			temp=temp.getDirMemDer();
		}
		SalidaTerminal.consola("null");
	}
}